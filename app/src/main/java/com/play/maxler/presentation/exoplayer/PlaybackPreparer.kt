package com.play.maxler.presentation.exoplayer

import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.os.ResultReceiver
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector
import com.google.android.exoplayer2.source.ConcatenatingMediaSource
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSource
import com.play.maxler.common.data.Constants
import com.play.maxler.data.local.preferences.Storage
import javax.inject.Inject


class PlaybackPreparer @Inject constructor(
    private val musicSource: MediaStoreSource,
    private val exoPlayer: ExoPlayer,
    private val dataSourceFactory: DefaultDataSource.Factory,
    private val storage: Storage
    ) : MediaSessionConnector.PlaybackPreparer {


    /**
     *  Handles callbacks to both [MediaSessionCompat.Callback.onPrepareFromSearch] *AND*
     *  [MediaSessionCompat.Callback.onPlayFromSearch] when using [MediaSessionConnector].
     *  (See above for details)
     *
     *  This method is used by the Google Assistant to respond to requests such as
     *  - Play Geisha from Wake Up on Jade Player
     *  - Play electronic music on Jade Player
     *  - Play music on Jade Player
     */
    override fun onPrepareFromSearch(query: String, playWhenReady: Boolean, extras: Bundle?) {
        musicSource.whenReady {
            val metadataList: List<MediaMetadataCompat> = musicSource.search(query ?: "", extras ?: Bundle.EMPTY)
            if (metadataList.isNotEmpty()) {
                val mediaSource = metadataList.asMediaSource(dataSourceFactory,metadataList)
                exoPlayer.setMediaSource(mediaSource)
            }
        }
    }



    override fun onCommand(
        player: Player,
        command: String,
        extras: Bundle?,
        cb: ResultReceiver?
    ): Boolean  = false

    // We are only supporting preparing and playing from [search] and [ media id]
    // TODO: Add support for ACTION_PREPARE and ACTION_PLAY, which mean "prepare/play something".
    // preparing so player can play it
    override fun getSupportedPrepareActions() =
        PlaybackStateCompat.ACTION_PREPARE_FROM_MEDIA_ID or
                PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID or
                PlaybackStateCompat.ACTION_PREPARE_FROM_SEARCH or
                PlaybackStateCompat.ACTION_PLAY_FROM_SEARCH

    /**
     *  Handles callbacks to both [MediaSessionCompat.Callback.onPrepareFromMediaId] *AND*
     *  [MediaSessionCompat.Callback.onPlayFromMediaId] when using [MediaSessionConnector].
     *  This is done with expectation that "play" is just "prepare" + "play".
     *  prepare MediaMetadataCompat get from mediaId so player can play it
     */
   override fun onPrepareFromMediaId(mediaId: String, playWhenReady: Boolean, extras: Bundle?) {
        musicSource.whenReady {
            // mediaMetaDataCompat that stored from convert songs to MediaSessionCompat
            // if id that want to play in mediaMetaData
            // find exist because
            val itemToPlay: MediaMetadataCompat? = musicSource.mediaSongs.find {
                it.id.equals( mediaId)
            }
            if (itemToPlay == null) {
                Log.i("maxler_player","onPrepareFromMediaId: Song with id $mediaId not found")
                return@whenReady
            }
            Log.i("maxler_player","onPrepareFromMediaId: Song is ${itemToPlay.title}")

            // come from mediaSongs.iterator()
            val metadataList: List<MediaMetadataCompat> = musicSource.mediaSongs.toList()
            val mediaSource: ConcatenatingMediaSource = metadataList.asMediaSource(
                dataSourceFactory ,
                musicSource.mediaSongs
            )

            val positionMs = if (itemToPlay.id == storage.sharedPreferences.getString(Constants.LAST_ID, null)) {
                storage.sharedPreferences.getLong(Constants.LAST_POSITION, 0)
            } else {
                0
            }
            Log.i("maxler_player","ConcatenatingMediaSource: is ${mediaSource.size} ")
            Log.i("maxler_player","positionMs: is ${positionMs.toString()} ")

            // Since the playlist was probably based on some ordering (such as tracks
            // on an album), find which window index to play first so that the song the
            // user actually wants to hear plays first.
            val initialWindowIndex = metadataList.indexOf(itemToPlay)
            Log.i("maxler_player","itemToPlay: is ${itemToPlay.title} ")
            Log.i("maxler_player","playWhenReady: is ${playWhenReady.toString()} ")

            exoPlayer.playWhenReady = playWhenReady
            exoPlayer.setMediaSource(mediaSource)
            exoPlayer.seekTo(initialWindowIndex, positionMs)
            if (playWhenReady){
                exoPlayer.play()
            }
            Log.i("maxler_player","audio formats: is ${exoPlayer.audioFormat.toString()} ")



        }
    }

    /**
     * Builds a playlist based on a [MediaMetadataCompat]
     * TODO: Support building a playlist by artist, genre, etc
     * @param itemToPlay Item to base the playlist on
     * @return a [List] of [MediaMetadataCompat] objects representing a playlist
     */
    private fun buildPlaylist(itemToPlay: MediaMetadataCompat): List<MediaMetadataCompat> =
        musicSource.filter { it.album == itemToPlay.album }.sortedBy { it.trackNumber }



    override fun onPrepare(playWhenReady: Boolean) = Unit

   /* override fun onPrepareFromMediaId(mediaId: String, playWhenReady: Boolean, extras: Bundle?) {
        val itemToPlay: MediaMetadataCompat? = musicSource.mediaSongs.find { mediaId == it.id }

    }*/

//    override fun onPrepareFromSearch(query: String, playWhenReady: Boolean, extras: Bundle?)  = Unit
    override fun onPrepareFromUri(uri: Uri, playWhenReady: Boolean, extras: Bundle?)  = Unit
}




