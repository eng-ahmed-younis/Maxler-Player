package com.play.maxler.presentation.exoplayer

import android.content.Context
import android.media.MediaMetadata
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaBrowserCompat.MediaItem
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.net.toUri
import com.google.android.exoplayer2.MediaItem.fromUri
import com.google.android.exoplayer2.source.ConcatenatingMediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource

import com.google.android.exoplayer2.upstream.DefaultDataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.play.maxler.R
import com.play.maxler.common.data.Constants
import com.play.maxler.common.utils.ImageUtils
import com.play.maxler.domain.models.Song
import com.play.maxler.domain.repository.MainRepository
import com.play.maxler.presentation.exoplayer.State.STATE_CREATED
import com.play.maxler.presentation.exoplayer.State.STATE_ERROR
import com.play.maxler.presentation.exoplayer.State.STATE_INITIALIZED
import com.play.maxler.presentation.exoplayer.State.STATE_INITIALIZING
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 *  Interface used by [MediaPlaybackService] for looking up [MediaMetadataCompat] objects
 *  convert "songs" to format that need from service
 *  Because Kotlin provides methods such [Iterable.find] and [Iterable.filter],
 *  this is a convenient interface to have on sources.
 */
interface MusicSource : Iterable<MediaMetadataCompat> {
    /**
     *  Begins loading the data for this music source.
     */
    suspend fun load()
    /**
     *  Method which will perform a given action after this [MusicSource] is ready to be used
     *  @param function A lambda expression to be called with a boolean parameter when the source is ready.
     *  `true indicates the source was successfully prepared., `false` indicates an error occurred.
     */
    fun whenReady(function: (Boolean) -> Unit): Boolean
    /**
     * Handles searching a [MusicSource] from a focused voice search, often coming
     * from the Google Assistant.
     */
    fun search(query: String, bundle: Bundle): List<MediaMetadataCompat>
}






/**
 * Base class for music sources
 */
class MediaStoreSource @Inject constructor(
    @ApplicationContext val context: Context,
    private val repository: MainRepository
    ) : MusicSource {


    private var _mediaSongs = mutableListOf<MediaMetadataCompat>()
    var mediaSongs = _mediaSongs

    private val onReadyListeners = mutableListOf<(Boolean) -> Unit>()



    private var state: State = STATE_CREATED
        set(value) {
            // if music source is [initialized] or ["error"]  it is finished nothing will happened
            if (value == STATE_INITIALIZED || value == STATE_ERROR) {
                 /** call onReadyListeners we want to change state with safe thread do with synchronized
                  * every thing do in synchronized{.....} block will accessed with the same thread
                  * no other thread can accessed onReadyListeners list at the same time
                  */
                synchronized(onReadyListeners) {
                    field = value
                    // change all lambada functions in all classes
                    onReadyListeners.forEach { listeners ->
                        // if not STATE_INITIALIZED will be STATE_ERROR and set false
                        listeners(state == STATE_INITIALIZED)
                    }
                }
            } else {
                field = value
            }
        }


    init {
        state = STATE_INITIALIZING
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun load() {
     /*   fetchMediaData().let {
            _mediaSongs.addAll(it)
            Log.i("maxler_player"," _mediaSongs  is ${ _mediaSongs.size.toString()}")
            Log.i("maxler_player"," mediaSongs  is ${ _mediaSongs.size.toString()}")

            state = STATE_INITIALIZED
        } ?: run {
            _mediaSongs = emptyList<MediaMetadataCompat>().toMutableList()
            state = STATE_ERROR
        }
        Log.i("maxler_player"," mediaSongs out   is ${ _mediaSongs.size.toString()}")*/
       // state = STATE_INITIALIZING

        fetchMediaData().let {
            _mediaSongs.addAll( it)
            state = STATE_INITIALIZED
        } ?: run {
            _mediaSongs.addAll( emptyList())
            state = STATE_ERROR
        }
        Log.i("maxler_player"," mediaSongs out   is ${ mediaSongs.size.toString()}")

    }


    /**
     * Perform an action when this MusicSource is ready.
     * This method is *not* thread safe. Ensure actions and state changes are only performed on a single thread
     */
    override fun whenReady(function: (Boolean) -> Unit): Boolean {
        return when (state) {
            // source not fully loading
            STATE_CREATED, STATE_INITIALIZING -> {
                // add this action "function" to list of "onReadyListeners"
                onReadyListeners += function
                false
            }
            else -> {
                // state STATE_INITIALIZED and set true
                function(state != STATE_ERROR)
                true
            }
        }
    }

    override fun iterator(): Iterator<MediaMetadataCompat>  =  _mediaSongs.iterator()

    /** this function use to convert songs into [MediaMetadataCompat] using by [MediaPlaybackService]
     */
  /*  private suspend fun fetchMediaData() : MutableList<MediaMetadataCompat>{

        return withContext(Dispatchers.IO){
            val results = mutableListOf<MediaMetadataCompat>()
            val mediaMetadata: MediaMetadataCompat.Builder = MediaMetadataCompat.Builder().songToMediaMetadata(context , repository.songs())
          // Log.i("maxler_player","fetchMediaData is ${mediaMetadata.build().description.mediaId.toString()}")
            val build: MediaMetadataCompat = mediaMetadata.build()
         //    Log.i("maxler_player"," MediaMetadataCompat is ${build.description.title}")
            results.add(build)
            return@withContext results
        }
    }*/

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun fetchMediaData(): MutableList<MediaMetadataCompat> = withContext(Dispatchers.IO) {
        val allSongs = repository.songs()
        val art = ImageUtils.getBitmapFromVectorDrawable(context, drawableId = R.drawable.maxler_img)
        val allMediaSongs: MutableList<MediaMetadataCompat> = allSongs.map { song ->
            MediaMetadataCompat.Builder()
                .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, song.id.toString())
                .putString(MediaMetadataCompat.METADATA_KEY_TITLE, song.title)
                .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, song.artistName)
                .putString(MediaMetadataCompat.METADATA_KEY_ALBUM, song.albumName)
                .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, song.duration)
                .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_URI, song.data)
                .putLong(MediaMetadataCompat.METADATA_KEY_TRACK_NUMBER, song.trackNumber.toLong())
                .putLong(MediaMetadataCompat.METADATA_KEY_NUM_TRACKS, song.count!!)
                .putBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART, art)
                .putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_TITLE, song.title)
                .putString(MediaMetadataCompat.METADATA_KEY_ALBUM_ART_URI,ImageUtils.getAlbumArtUri(song.albumId).toString())
                .putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_SUBTITLE, song.artistName)
                .putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_DESCRIPTION, song.albumName)
                .build()
        }.toMutableList()
       // this@MediaStoreSource._mediaSongs.addAll(allMediaSongs)

            Log.i("maxler_player","list  allSongs is ${_mediaSongs.size}")

        return@withContext allMediaSongs
    }



/*
   suspend fun asMediaItems()  : MutableList<MediaItem> {
      return repository.songs().map { song ->
           val desc = MediaDescriptionCompat.Builder()
               .setMediaUri(song.data!!.toUri())
               .setTitle(song.title)
               .setSubtitle(song.artistName)
               .setMediaId(song.id.toString())
               .setIconUri(ImageUtils.getAlbumArtUri(song.albumId))
               .build()
           MediaBrowserCompat.MediaItem(desc, MediaItem.FLAG_PLAYABLE)
       }.toMutableList()

   }*/

 /*  suspend fun asMediaItems() = repository.songs().map { song ->
        val desc = MediaDescriptionCompat.Builder()
            .setMediaUri(song.data!!.toUri())
            .setTitle(song.title)
            .setSubtitle(song.artistName)
            .setMediaId(song.id.toString())
            .setIconUri(ImageUtils.getAlbumArtUri(song.albumId))
            .build()
        MediaBrowserCompat.MediaItem(desc, MediaItem.FLAG_PLAYABLE)
    }.toMutableList()
    */

    // To make things easier for *displaying* these, set the display properties as well.


    override fun search(query: String, bundle: Bundle): List<MediaMetadataCompat> {
        // First attempt to search with the "focus" that's provided in the bundle
        val focusSearchResult = when (bundle[Constants.EXTRA_MEDIA_FOCUS]) {
            MediaStore.Audio.Genres.ENTRY_CONTENT_TYPE -> {
                // For a Genre focused search, only genre is set.
                val genre = bundle[MediaStore.EXTRA_MEDIA_GENRE]
                filter {
                    it.genre == genre
                }
            }
            MediaStore.Audio.Artists.ENTRY_CONTENT_TYPE -> {
                // For an Artist focused search, only the artist is set.
                val artist = bundle[MediaStore.EXTRA_MEDIA_ARTIST]
                filter {
                    (it.artist == artist || it.albumArtist == artist)
                }
            }
            MediaStore.Audio.Albums.ENTRY_CONTENT_TYPE -> {
                // For an Album focused search, album and artist are set.
                val artist = bundle[MediaStore.EXTRA_MEDIA_ARTIST]
                val album = bundle[MediaStore.EXTRA_MEDIA_ALBUM]
                filter {
                    (it.artist == artist || it.albumArtist == artist) && it.album == album
                }
            }
            MediaStore.Audio.Media.ENTRY_CONTENT_TYPE -> {
                // For a Song (aka Media) focused search, title, album, and artist are set.
                val title = bundle[MediaStore.EXTRA_MEDIA_TITLE]
                val album = bundle[MediaStore.EXTRA_MEDIA_ALBUM]
                val artist = bundle[MediaStore.EXTRA_MEDIA_ARTIST]
                filter {
                    (it.artist == artist || it.albumArtist == artist) && it.album == album
                            && it.title == title
                }
            }
            else -> {
                // There isn't a focus, so no results yet.
                emptyList()
            }
        }

        // Check if we found any results from the focused search
        if (focusSearchResult.isNotEmpty()) return focusSearchResult

        // The query can be null if the user asked to "play music", or something similar.
        // Let's just return them all, shuffled as something to play
        if (query.isBlank()) return shuffled()

        // Let's check check the query against a few fields
        return filter {
            it.title!!.contains(query)
                    || it.genre!!.contains(query)
                    || it.artist!!.contains(query)
                    || it.album!!.contains(query)
                    || it.author!!.contains(query)
                    || it.composer!!.contains(query)
                    || it.composer!!.contains(query)
        }
    }

    /**
     * Extension method for building an [ProgressiveMediaSource] from a [MediaMetadataCompat] object.
     *
     * For convenience, place the [MediaDescriptionCompat] into the tag so it can be retrieved later.
     */

    fun asMediaSource(dataSourceFactory: DefaultDataSource.Factory): ConcatenatingMediaSource {
        val concatenatingMediaSource = ConcatenatingMediaSource()
        _mediaSongs.forEach {
            val mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(fromUri(it.getString(MediaMetadataCompat.METADATA_KEY_MEDIA_URI).toUri()))
            concatenatingMediaSource.addMediaSource(mediaSource)
        }
        return concatenatingMediaSource
    }

    fun asMediaItems() = _mediaSongs.map { song ->
        val desc = MediaDescriptionCompat.Builder()
            .setMediaUri(song.getString(MediaMetadataCompat.METADATA_KEY_MEDIA_URI).toUri())
            .setTitle(song.description.title)
            .setSubtitle(song.description.subtitle)
            .setMediaId(song.description.mediaId)
            .setIconUri(song.description.iconUri)
            .build()
        MediaBrowserCompat.MediaItem(desc, MediaItem.FLAG_PLAYABLE)
    }.toMutableList()



}



enum class State{
    //State indicating the source was created, but no initialization has performed.
    STATE_CREATED,
    // State indicating initialization of the source is in progress
    STATE_INITIALIZING,
    //State indicating the source has been initialized and is ready to be used
    STATE_INITIALIZED,
    // State indicating an error has occurred
    STATE_ERROR
}

