package com.play.maxler.presentation.screens.play

import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.play.maxler.common.data.Constants
import com.play.maxler.data.local.preferences.Storage
import com.play.maxler.presentation.exoplayer.*
import com.play.maxler.presentation.exoplayer.playback.MediaItemData
import com.play.maxler.presentation.screens.main.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
/*


@HiltViewModel
class PlaybackViewModel @Inject constructor(
    private val  mediaSessionConnection: MediaSessionConnection,
    val storage: Storage,
) : ViewModel(){

    */
/* MediaPlayback fields *//*

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

    private val lastParentId: String get() = storage.sharedPreferences.getString(Constants.LAST_PARENT_ID, Constants.SONGS_ROOT)!!
    private var playMediaAfterLoad: String? = null


        // click to button in play screen but now not know play or pause
        fun playPause() {
            // if media is playing get transportControls from media controller that that translate
            // ui controller action [play , pause , skip , fast-forward ......] into callbacks to the  media session
            // MediaController receives callbacks from media session when ever session state changed

            */
/** get isPlayingOrBuffering value from  PlaybackStateCompatExtensions
             * if state equal "STATE_PLAYING" || "STATE_BUFFERING"
             * *//*

            if (mediaSessionConnection.playbackState.value?.isPlayingOrBuffering == true) {
                mediaSessionConnection.transportControls.pause()
            } else {
                // if state is "ACTION_PAUSE" now play media
                playMediaId(currentItem.value?.id)
            }
        }


        private fun playMediaId(mediaId: String?) {
            if (mediaId == null) return
            // last playing media
            val nowPlaying = mediaSessionConnection.nowPlaying.value
            val transportControls = mediaSessionConnection.transportControls
            */
/* check if state == STATE_BUFFERING || STATE_PLAYING || STATE_PAUSED
             * and return true if state equal one of three states *//*

            val isPrepared = mediaSessionConnection.playbackState.value?.isPrepared ?: false
            // equal one of three but "id" equal nowPlaying "id"
            // NowPlaying can be 'Playing' OR 'Pause' Song
            // playing running or pause and visible in play screen
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



        fun playAll(playId: String = Constants.PLAY_RANDOM) {
            val parentId = lastParentId
            val list = mediaItems.value
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


    // MediaBrowserCompat Activity
    private val subscriptionCallback = object : MediaBrowserCompat.SubscriptionCallback() {
        override fun onChildrenLoaded(parentId: String, children: MutableList<MediaBrowserCompat.MediaItem>) {
            val items = children.map { MediaItemData(it, isItemPlaying(it.mediaId!!), isItemBuffering(it.mediaId!!)) }
            val current: MediaItemData? = if (!playMediaAfterLoad.isNullOrBlank()) {
                getItemFromPlayId(playMediaAfterLoad!!, items)
            } else {
                (items.firstOrNull { it.isPlaying }
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
                        val value = withContext(Dispatchers.IO) {
                            playedRepository.fetchFirst()
                        }
                        current.duration = value?.duration ?: 0
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




    private fun getItemFromPlayId(playId: String, items: List<MediaItemData>): MediaItemData? {
        return when (playId) {
            Constants.PLAY_FIRST -> items.firstOrNull()
            Constants.PLAY_RANDOM -> items.random()
            else -> items.firstOrNull { it.id == playId }
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


}*/
