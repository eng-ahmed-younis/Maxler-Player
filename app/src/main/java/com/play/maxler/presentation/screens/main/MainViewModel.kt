package com.play.maxler.presentation.screens.main

import android.os.Handler
import android.os.Looper
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import androidx.lifecycle.*
import com.play.maxler.common.data.Constants
import com.play.maxler.common.data.Constants.LAST_PARENT_ID
import com.play.maxler.common.data.Resource
import com.play.maxler.data.local.preferences.Storage
import com.play.maxler.domain.models.Album
import com.play.maxler.domain.models.Song
import com.play.maxler.domain.use_case.albums.GetAlbumByIdUseCase
import com.play.maxler.domain.use_case.albums.GetAlbumsUseCase
import com.play.maxler.domain.use_case.history.FetchFirstUseCase
import com.play.maxler.domain.use_case.songs.GetSongsByIdUseCase
import com.play.maxler.domain.use_case.songs.GetSongsUseCase
import com.play.maxler.presentation.exoplayer.*
import com.play.maxler.presentation.exoplayer.playback.MediaItemData
import com.play.maxler.presentation.screens.songs.SongMenuItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val getSongsUseCase: GetSongsUseCase,
    private val getAlbumsUseCase: GetAlbumsUseCase,
    private val getSongsByIdUseCase: GetSongsByIdUseCase,
    private val getAlbumByIdUseCase: GetAlbumByIdUseCase,
    private val fetchFirstUseCase: FetchFirstUseCase,
    private val mediaSessionConnection: MediaSessionConnection,
    val storage: Storage
) : ViewModel() {

    /* MediaPlayback fields */

 //   private lateinit var _mediaSessionConnection  : MediaSessionConnection

    private var _mediaItems = MutableLiveData<List<MediaItemData>>()
    val mediaItems = _mediaItems.asLiveData()

    private val _currentItem = MutableLiveData<MediaItemData?>()
    val currentItem = _currentItem.asLiveData()

    private val _repeatMode = MutableLiveData<Int>().apply {
        value = storage.sharedPreferences.getInt(Constants.LAST_REPEAT_MODE , PlaybackStateCompat.REPEAT_MODE_NONE) //default
    }
    val repeatMode = _repeatMode.asLiveData()

    private val _shuffleMode = MutableLiveData<Int>().apply {
        value = storage.sharedPreferences.getInt(
            Constants.LAST_SHUFFLE_MODE,
            PlaybackStateCompat.SHUFFLE_MODE_NONE
        )
    }
    val shuffleMode = _shuffleMode.asLiveData()

    private val handler = Handler(Looper.getMainLooper())

    private val lastParentId: String
        get() = storage.sharedPreferences.getString(Constants.LAST_PARENT_ID, Constants.MY_MEDIA_ROOT_ID)!!

    private var playMediaAfterLoad: String? = null

    private val _mediaPosition =
        MutableLiveData<Long>().apply { value = storage.sharedPreferences.getLong(Constants.LAST_POSITION, 0) }
    val mediaPosition = _mediaPosition.asLiveData()

    private val _playbackState = MutableLiveData<PlaybackStateCompat>().apply { value = EMPTY_PLAYBACK_STATE }
    val playbackState = _playbackState.asLiveData()

    private var updatePosition = true

    /**
     *  Because there's a complex dance between this [AndroidViewModel] and the [MediaSessionConnection]
     *  (which is wrapping a [MediaBrowserCompat] object), the usual guidance of using [Transformations]
     *  doesn't quite work.
     *
     *  Specifically there's three things that are watched that will cause the single piece of [LiveData]
     *  exposed from this class to be updated
     *
     *  [subscriptionCallback] (defined above) is called if/when the children of this ViewModel's [mediaId] changes
     *
     *  [MediaSessionConnection.playbackState] changes state based on the playback state of
     *  the player, which can change the [MediaItemData.isPlaying]s in the list.
     *
     *  [MediaSessionConnection.nowPlaying] changes based on the item that's being played,
     *  which can also change [MediaItemData.isPlaying]s in the list.
     */
/*    private val mediaSessionConnection: MediaSessionConnection = mediaSessionConnection.also {
      //  this.Playback().apply {
            it.subscribe(lastParentId , subscriptionCallback)
            it.playbackState.observeForever(playbackStateObserver)
            it.nowPlaying.observeForever(mediaMetadataObserver)
            it.repeatMode.observeForever(repeatObserver)
            it.shuffleMode.observeForever(shuffleObserver)
     //   }

    }*/




    /* songs fields */
    private val _songs = MutableStateFlow<Resource<List<Song>>>(Resource.Loading)
    val songs = _songs.asSharedFlow()

    private val _songById = MutableStateFlow<Song>(Constants.emptySong)
    val songById = _songById.asStateFlow()


    private val _songMenuItem  = MutableLiveData<SongMenuItem>()
    val songMenuItem = _songMenuItem.asLiveData()


    private var _songsSortOrderClicked = MutableLiveData<Boolean>()
    val songsSortOrderClicked = _songsSortOrderClicked.asLiveData()


    private var _sortOrderItemClickedValue = MutableLiveData<String>()
    val sortOrderItemClickedValue = _sortOrderItemClickedValue.asLiveData()



    /* albums fields */
    private val _albums = MutableStateFlow<Resource<List<Album>>>(Resource.Loading)
    val album = _albums.asSharedFlow()

    private val _albumById = MutableStateFlow<Resource<Album>>(Resource.Loading)
    val albumById = _albums.asSharedFlow()


    /* navigation fields */
    private var _navigateToPlayFragment = MutableLiveData<Boolean>()
    val navigateToPlayFragment = _navigateToPlayFragment.asLiveData()

    private var _navigateToRecentFragment = MutableLiveData<Boolean>()
    val navigateToRecentFragment = _navigateToRecentFragment.asLiveData()

    private var _navigateToFavoriteFragment = MutableLiveData<Boolean>()
    val navigateToFavoriteFragment = _navigateToFavoriteFragment.asLiveData()

    private var _navigateToPlaylistFragment = MutableLiveData<Boolean>()
    val navigateToPlaylistFragment = _navigateToPlaylistFragment.asLiveData()

    private var _navigateToSearchFragment = MutableLiveData<Boolean>()
    val navigateToSearchFragment = _navigateToSearchFragment.asLiveData()

    private var _navigateToSettingFragment = MutableLiveData<Boolean>()
    val navigateToSettingFragment = _navigateToSettingFragment.asLiveData()

    private var _navigateToAlbumDetailsFragment = MutableLiveData<Boolean>()
    val navigateToAlbumDetailsFragment = _navigateToAlbumDetailsFragment.asLiveData()


    /* playing filed */
    private var _playOverFlowMenu = MutableLiveData<Boolean>()
    val playOverFlowMenu = _playOverFlowMenu.asLiveData()



    init {
        initMainViewModelField()
        getSongs()
        getAlbums()
      //  initMediaSessionSubscription()
        mediaSessionConnection.subscribe(lastParentId , object :MediaBrowserCompat.SubscriptionCallback(){
            override fun onChildrenLoaded(parentId: String, children: MutableList<MediaBrowserCompat.MediaItem>) {
                val items: List<MediaItemData> = children.map {
                MediaItemData(it, isItemPlaying(it.mediaId!!), isItemBuffering(it.mediaId!!))
                }
                val current: MediaItemData? = if (!playMediaAfterLoad.isNullOrBlank()) {
                    getItemFromPlayId(playMediaAfterLoad!!, items)
                } else {
                    (items.firstOrNull {
                        it.isPlaying
                    }
                        ?: items.firstOrNull { it.id == storage.sharedPreferences.getString(Constants.LAST_ID, null) }
                        ?: items.firstOrNull())
                }
             /*   viewModelScope.launch {
                    // Let's get the duration of the current playing song if it's the same as our filter above
                    val currentValue = currentItem.value
                    if (current != null) {
                        if (currentValue != null && (current.id == currentValue.id)) {
                            current.duration = currentValue.duration
                        } else {
                            val value: Song =  fetchFirstUseCase()
                            current.duration = value.duration ?: 0
                        }
                    }
                    _mediaItems.postValue(items)
                    _currentItem.postValue(current)
                    // Re-post the media position so views like SeekBars can pickup the new view
                    _mediaPosition.postValue(mediaPosition.value)

                    if (!playMediaAfterLoad.isNullOrBlank() && current != null) {
                        playMediaId(current.id)
                        playMediaAfterLoad = null
                    }
                }*/
        }
        })

      /*  mediaSessionConnection.playbackState.observeForever(playbackStateObserver)
        mediaSessionConnection.nowPlaying.observeForever(mediaMetadataObserver)
        mediaSessionConnection.repeatMode.observeForever(repeatObserver)
        mediaSessionConnection.shuffleMode.observeForever(shuffleObserver)*/

    }

/*

    private fun initMediaSessionSubscription(){
        _mediaSessionConnection = musicServiceConnection.also {
            it.subscribe(lastParentId , subscriptionCallback)
            it.playbackState.observeForever(playbackStateObserver)
            it.nowPlaying.observeForever(mediaMetadataObserver)
            it.repeatMode.observeForever(repeatObserver)
            it.shuffleMode.observeForever(shuffleObserver)
        }
    }
*/

    private fun getSongs(){
        viewModelScope.launch {
            getSongsUseCase().collect {
                _songs.emit(Resource.Loading)
                _songs.emit(it)
            }
        }
    }

    private fun getSongById(songId : Long){
        viewModelScope.launch {
            getSongsByIdUseCase(songId).collect {
                _songById.emit(it)
            }
        }
    }

    private fun getAlbums(){
        viewModelScope.launch {
            getAlbumsUseCase().collect {

                _albums.emit(Resource.Loading)
                _albums.emit(it)
            }
        }
    }

    private fun getAlbumById(albumId : Long){
        viewModelScope.launch {
            getAlbumByIdUseCase(albumId).collect {
                _albumById.emit(Resource.Loading)
                _albumById.emit(it)
            }
        }
    }



    private fun initMainViewModelField(){
        initNavigationFields()
        initSongsFields()
        initPlayFields()
    }

    private fun initNavigationFields(){
        _navigateToPlayFragment.value = false
        _navigateToRecentFragment.value = false
        _navigateToFavoriteFragment.value = false
        _navigateToPlaylistFragment.value = false
        _navigateToSearchFragment.value = false
        _navigateToSettingFragment.value = false
        _navigateToAlbumDetailsFragment.value = false
    }
    private fun initSongsFields(){
        false.also { _songsSortOrderClicked.value = it }
        _sortOrderItemClickedValue.value = storage.sortOrderItemSelected ?: ""
        _songMenuItem.value = SongMenuItem(false, null)
    }

    private fun initPlayFields(){
        _playOverFlowMenu.value = false
    }

    /* handel songs menu item clicked */
    // call in songs menu item item_song.xml
    fun songsMenuItemClicked(currentSong : Song)  {
        _songMenuItem.postValue(SongMenuItem(menuItemClicked = true, clickedSong = currentSong))
    }

    fun songsMenuItemClickedComplete() =
        false.also { _songMenuItem.value!!.menuItemClicked = it }


    /* handel songs sort order clicked */
    fun songsSortOrderClicked() = true.also { _songsSortOrderClicked.value = it }
    fun songsSortOrderClickedComplete() = false.also { _songsSortOrderClicked.value = it }


    fun setSortOrderItemClickedValue(itemName : String){
        _sortOrderItemClickedValue.value = itemName
        storage.sortOrderItemSelected = itemName
    }

    fun playOverFlowMenuClicked() = true.also { _playOverFlowMenu.value = it }
    fun playOverFlowMenuClickedComplete() = false.also { _playOverFlowMenu.value = it }



    inner class Navigation {
        //playing
        fun navigateToPlayScreen() = true.also {_navigateToPlayFragment.value = it}
        fun navigateToPlayScreenComplete() = false.also {_navigateToPlayFragment.value = it}
        //favorite
        fun navigateToFavoriteScreen()  = true.also{_navigateToFavoriteFragment.value = it}
        fun navigateToFavoriteScreenComplete() = false.also{_navigateToFavoriteFragment.value = it}
        //recent
        fun navigateToRecentScreen() = true.also{_navigateToRecentFragment.value = it}
        fun navigateToRecentScreenComplete() = false.also{_navigateToRecentFragment.value = it}
        //playlist
        fun navigateToPlaylistScreen() = true.also { _navigateToPlaylistFragment.value = it }
        fun navigateToPlaylistScreenComplete() = false.also { _navigateToPlaylistFragment.value = it }
        // search
        fun navigateToSearchScreen() = true.also { _navigateToSearchFragment.value = it }
        fun navigateToSearchScreenComplete() = false.also { _navigateToSearchFragment.value = it }
        // setting
        fun navigateToSettingScreen() = true.also { _navigateToSettingFragment.value = it }
        fun navigateToSettingScreenComplete() = false.also { _navigateToSettingFragment.value = it }
        // album details
        fun navigateToAlbumDetailsScreen() = true.also { _navigateToAlbumDetailsFragment.value = it }
        fun navigateToAlbumDetailsScreenComplete() = false.also { _navigateToAlbumDetailsFragment.value = it }
    }

    /* Playback service */
    //  inner class Playback{

    // click to button in play screen but now not know play or pause
    fun playPause() {
        // if media is playing get transportControls from media controller that that translate
        // ui controller action [play , pause , skip , fast-forward ......] into callbacks to the  media session
        // MediaController receives callbacks from media session when ever session state changed

        /** get isPlayingOrBuffering value from  PlaybackStateCompatExtensions
         * if state equal "STATE_PLAYING" || "STATE_BUFFERING"
         * */
        if (mediaSessionConnection.playbackState.value?.isPlayingOrBuffering == true) {
            mediaSessionConnection.transportControls.pause()
        } else {
            // if state is "ACTION_PAUSE" now play media
            playMediaId(currentItem.value?.id)
        }
    }


    fun playMediaId(mediaId: String?) {
        Log.i("maxler_player"," MainViewModel playMediaId() : Song with id $mediaId")

        if (mediaId == null) return
        // last playing media
        val nowPlaying: MediaMetadataCompat? = mediaSessionConnection.nowPlaying.value
        val transportControls = mediaSessionConnection.transportControls
        /* check if state == STATE_BUFFERING || STATE_PLAYING || STATE_PAUSED
         * and return true if state equal one of three states */
        val isPrepared = mediaSessionConnection.playbackState.value?.isPrepared ?: false
        // equal one of three but "id" equal nowPlaying "id"
        // NowPlaying can be 'Playing' OR 'Pause' Song
        // playing running or pause and visible in play screen
        // clicked to last item to pause it or play it
        // if (pause)
        //     change to play
        // if (play)
        //      nothing continue playing
        if (isPrepared && mediaId == nowPlaying?.id) {
            if (mediaSessionConnection.playbackState.value?.isPauseEnabled == true) {
                mediaSessionConnection.transportControls.play()
            }
        } else {
            transportControls.playFromMediaId(mediaId, null)
            transportControls.setRepeatMode(repeatMode.value!!)
            transportControls.setShuffleMode(shuffleMode.value!!)
        }
    }


    fun playAlbum(album: Album, playId: String = Constants.PLAY_FIRST) {
        val parentId = lastParentId
        val list = mediaItems.value
        if (parentId == album.id.toString() && list != null) {
            playMediaId(getItemFromPlayId(playId, list)?.id)
        } else {
            playMediaAfterLoad = playId
            mediaSessionConnection.unsubscribe(parentId, subscriptionCallback)
            mediaSessionConnection.subscribe(album.id.toString(), subscriptionCallback)
        }
    }


    fun playAll(playId: String = Constants.PLAY_RANDOM) {
        /** user play music and click play again not start playing again */
        val parentId = lastParentId
        val list: List<MediaItemData>? = mediaItems.value
        if (parentId == Constants.SONGS_ROOT && list != null) {
            playMediaId(getItemFromPlayId(playId, list)?.id)
        } else {
            playMediaAfterLoad = playId
            mediaSessionConnection.unsubscribe(parentId, subscriptionCallback)
            mediaSessionConnection.subscribe(Constants.SONGS_ROOT, subscriptionCallback)
        }
    }



    fun seek(time: Long) {
        val transportControls = mediaSessionConnection.transportControls
        transportControls.seekTo(time)
        storage.sharedPreferences.edit().putLong(Constants.LAST_POSITION, time).apply()
    }

    fun setShuffleMode() {
        val newValue = when (shuffleMode.value) {
            PlaybackStateCompat.SHUFFLE_MODE_ALL -> PlaybackStateCompat.SHUFFLE_MODE_NONE
            else -> PlaybackStateCompat.SHUFFLE_MODE_ALL
        }
        mediaSessionConnection.transportControls.setShuffleMode(newValue)
    }

    fun setRepeatMode() {
        val newValue = when (repeatMode.value) {
            PlaybackStateCompat.REPEAT_MODE_NONE -> PlaybackStateCompat.REPEAT_MODE_ONE
            PlaybackStateCompat.REPEAT_MODE_ONE -> PlaybackStateCompat.REPEAT_MODE_ALL
            PlaybackStateCompat.REPEAT_MODE_ALL -> PlaybackStateCompat.REPEAT_MODE_NONE
            else -> PlaybackStateCompat.REPEAT_MODE_NONE
        }
        mediaSessionConnection.transportControls.setRepeatMode(newValue)
    }


    fun skipToNext() {
        if (mediaSessionConnection.playbackState.value?.started == true) {
            mediaSessionConnection.transportControls.skipToNext()
        } else {
            _mediaItems.value?.let {
                val i = it.indexOf(currentItem.value)
                // Only skip to the next item if the current item is not the last item in the list
                if (i != (it.size - 1)) _currentItem.postValue(it[(i + 1)])
            }
        }
    }

    fun skipToPrevious() {
        if (mediaSessionConnection.playbackState.value?.started == true) {
            mediaSessionConnection.transportControls.skipToPrevious()
        } else {
            _mediaItems.value?.let {
                val i = it.indexOf(currentItem.value)
                // Only skip to the previous item if the current item is not first item in the list
                if (i > 1) _currentItem.postValue(it[(i - 1)])
            }
        }
    }

    // When the session's [PlaybackStateCompat] changes, the [mediaItems] needs to be updated
    val playbackStateObserver = Observer<PlaybackStateCompat> {
        val state = it ?: EMPTY_PLAYBACK_STATE
        val metadata: MediaMetadataCompat = mediaSessionConnection.nowPlaying.value ?: NOTHING_PLAYING
        if (metadata.getString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID) != null) {
            _mediaItems.postValue(updateState(state, metadata))
        }
    }

    // When the session's [MediaMetadataCompat] changes, the [mediaItems] needs to be updated
    val mediaMetadataObserver = Observer<MediaMetadataCompat> {
        val playbackState: PlaybackStateCompat = mediaSessionConnection.playbackState.value ?: EMPTY_PLAYBACK_STATE
        val metadata = it ?: NOTHING_PLAYING
        if (metadata.getString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID) != null) {
            _mediaItems.postValue(updateState(playbackState, metadata))
        }
    }


    val shuffleObserver = Observer<Int>(_shuffleMode::postValue)

    val repeatObserver = Observer<Int>(_repeatMode::postValue)


    private fun updateState(state: PlaybackStateCompat, metadata: MediaMetadataCompat):
            List<MediaItemData>? {
        val items = (_mediaItems.value?.map { it.copy(isPlaying = it.id == metadata.id && state.isPlayingOrBuffering) }
            ?: emptyList())

        val currentItem = if (items.isEmpty()) {
            // Only update media item if playback has started
            if (state.started) {
                MediaItemData(metadata, state.isPlaying, state.isBuffering)
            } else {
                null
            }
        } else {
            // Only update media item once we have duration available
            if (metadata.duration != 0L && items.isNotEmpty()) {
                val matchingItem = items.firstOrNull { it.id == metadata.id }
                matchingItem?.apply {
                    isPlaying = state.isPlaying
                    isBuffering = state.isBuffering
                    duration = metadata.duration
                }
            } else null
        }

        // Update synchronously so addToRecentlyPlayed can pick up a valid currentItem
        if (currentItem != null) _currentItem.value = currentItem
        _playbackState.postValue(state)
        if (state.started) updatePlaybackPosition()
        return items
    }


    /**
     * Internal function that recursively calls itself every [POSITION_UPDATE_INTERVAL_MILLIS] ms
     * to check the current playback position and updates the corresponding LiveData object when it
     * has changed.
     */
    private fun updatePlaybackPosition(): Boolean = handler.postDelayed({
        val currPosition = _playbackState.value?.currentPlayBackPosition
        if (_mediaPosition.value != currPosition) {
            _mediaPosition.postValue(currPosition)
        }
        if (updatePosition)
            updatePlaybackPosition()
    }, Constants.POSITION_UPDATE_INTERVAL_MILLIS)


    // MediaBrowserCompat Activity
    // Callbacks for subscription related events.
    val subscriptionCallback = object : MediaBrowserCompat.SubscriptionCallback() {
        /**
        Called when the list of children is loaded or updated.
        Params:
        parentId – The media id of the parent media item.
        children – The children which were loaded.
         **/
        override fun onChildrenLoaded(parentId: String, children: MutableList<MediaBrowserCompat.MediaItem>) {
            val items: List<MediaItemData> = children.map {
                MediaItemData(it, isItemPlaying(it.mediaId!!), isItemBuffering(it.mediaId!!))
            }
            val current: MediaItemData? = if (!playMediaAfterLoad.isNullOrBlank()) {
                getItemFromPlayId(playMediaAfterLoad!!, items)
            } else {
                (items.firstOrNull {
                    it.isPlaying
                }
                    ?: items.firstOrNull { it.id == storage.sharedPreferences.getString(Constants.LAST_ID, null) }
                    ?: items.firstOrNull())
            }

            viewModelScope.launch {
                // Let's get the duration of the current playing song if it's the same as our filter above
                val currentValue = currentItem.value
                if (current != null) {
                    if (currentValue != null && (current.id == currentValue.id)) {
                        current.duration = currentValue.duration
                    } else {
                        val value: Song =  fetchFirstUseCase()
                        current.duration = value.duration ?: 0
                    }
                }
                _mediaItems.postValue(items)
                _currentItem.postValue(current)
                // Re-post the media position so views like SeekBars can pickup the new view
                _mediaPosition.postValue(mediaPosition.value)

                if (!playMediaAfterLoad.isNullOrBlank() && current != null) {
                    playMediaId(current.id)
                    playMediaAfterLoad = null
                }
            }
        }
    }


    private fun isItemPlaying(mediaId: String): Boolean {
        val isActive = mediaId == mediaSessionConnection.nowPlaying.value?.id
        val isPlaying = mediaSessionConnection.playbackState.value?.isPlaying ?: false
        return isActive && isPlaying
    }

    private fun isItemBuffering(mediaId: String): Boolean {
        val isActive = mediaId == mediaSessionConnection.nowPlaying.value?.id
        val isBuffering = mediaSessionConnection.playbackState.value?.isBuffering ?: false
        return isActive && isBuffering
    }

    private fun getItemFromPlayId(playId: String, items: List<MediaItemData>): MediaItemData? {
        return when (playId) {
            Constants.PLAY_FIRST -> items.firstOrNull()
            Constants.PLAY_RANDOM -> items.random()
            else -> items.firstOrNull { it.id == playId }
        }
    }




    inner class SortOrderUpdate{
        fun songsOrderUpdate() = this@MainViewModel.getSongs()
    }

    /**
     * Since we use [LiveData.observeForever] above (in [mediaSessionConnection]), we want
     * to call [LiveData.removeObserver] here to prevent leaking resources when the [ViewModel]
     * is not longer in use.
     *
     * For more details, see the kdoc on [mediaSessionConnection] above.
     */
    override fun onCleared() {
        super.onCleared()

        //    this.Playback().apply {
        // Remove the permanent observers from the MediaSessionConnection.
        mediaSessionConnection.playbackState.removeObserver(playbackStateObserver)
        mediaSessionConnection.nowPlaying.removeObserver(mediaMetadataObserver)
        mediaSessionConnection.repeatMode.removeObserver(repeatObserver)
        mediaSessionConnection.shuffleMode.removeObserver(shuffleObserver)
        // And then, finally, unsubscribe the media ID that was being watched.
        mediaSessionConnection.unsubscribe(mediaSessionConnection.rootMediaId, subscriptionCallback)

        //   }



        // Stop updating the position
        updatePosition = false

        handler.removeCallbacksAndMessages(null)
        //  }


    }

}

// represent this mutable livedata as livedata
fun <T> MutableLiveData<T>.asLiveData() : LiveData<T> {
    return this
}
