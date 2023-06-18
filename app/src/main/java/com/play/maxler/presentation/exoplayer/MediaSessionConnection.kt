package com.play.maxler.presentation.exoplayer

import android.content.ComponentName
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.ResultReceiver
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.media.MediaBrowserServiceCompat
import com.play.maxler.common.data.Constants
import com.play.maxler.data.local.preferences.Storage
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
/**
 * Class that manages a connection to a [MediaBrowserServiceCompat] instance.
 *
 */
class MediaSessionConnection @Inject constructor(
    @ApplicationContext context: Context,
    serviceComponent: ComponentName,
    private val storage: Storage
) {

    private val connectionCallback = MediaBrowserConnectionCallback(context)
    // media Browser Service
    private val mediaBrowser =
        MediaBrowserCompat(
            context,
            serviceComponent,
            connectionCallback,
            null
        ).apply {
            connect()
        }
    private lateinit var mediaController: MediaControllerCompat

    val isConnected = MutableLiveData<Boolean>().apply {
        postValue(false)
    }
    val networkFailure = MutableLiveData<Boolean>().apply {
        postValue(false)
    }
    val rootMediaId get() = mediaBrowser.root

    /* set state nothing when empty playing */
    val playbackState = MutableLiveData<PlaybackStateCompat>().apply { postValue(EMPTY_PLAYBACK_STATE) }
    /* set nothing when empty playing */
    val nowPlaying = MutableLiveData<MediaMetadataCompat>().apply {
        postValue(NOTHING_PLAYING)
    }

    val shuffleMode = MutableLiveData<Int>().apply {
        postValue(storage.sharedPreferences.getInt(Constants.LAST_SHUFFLE_MODE, PlaybackStateCompat.SHUFFLE_MODE_NONE))
    }
    val repeatMode = MutableLiveData<Int>().apply {
        postValue(storage.sharedPreferences.getInt(Constants.LAST_REPEAT_MODE, PlaybackStateCompat.REPEAT_MODE_NONE))
    }

    //
    val transportControls: MediaControllerCompat.TransportControls get() = mediaController.transportControls


    fun subscribe(parentId: String, callback: MediaBrowserCompat.SubscriptionCallback) {
        Log.i("maxler_player","MediaSessionConnection subscribe to  ${parentId.toString()}")

        mediaBrowser.subscribe(Constants.MY_MEDIA_ROOT_ID, callback)
        storage.sharedPreferences.edit().putString(Constants.LAST_PARENT_ID, parentId).apply()
    }

    fun unsubscribe(parentId: String, callback: MediaBrowserCompat.SubscriptionCallback) {
        mediaBrowser.unsubscribe(parentId, callback)
    }

    fun sendCommand(command: String, parameters: Bundle?) = sendCommand(command, parameters) { _, _ -> }

    private fun sendCommand(
        command: String, parameters: Bundle?, resultCallback: ((Int, Bundle?) -> Unit)
    ) = if (mediaBrowser.isConnected) {
        mediaController.sendCommand(command, parameters, object : ResultReceiver(Handler()) {
            override fun onReceiveResult(resultCode: Int, resultData: Bundle?) {
                resultCallback(resultCode, resultData)
            }
        })
        true
    } else {
        false
    }


    /* you must create an instance of ConnectionCallback. Modify its onConnected()
     method to retrieve the media session token from the MediaBrowserService
     and use the token to create a MediaControllerCompat.
    **/
    private inner class MediaBrowserConnectionCallback constructor(
        private val context: Context
        ) : MediaBrowserCompat.ConnectionCallback() {

        // Invoked when MediaBrowser connection succeeds
        override fun onConnected() {
            // Get a MediaController for the MediaSession.
            // get MediaController from that session token of mediaBrowser
            mediaController = MediaControllerCompat(
                context,
                mediaBrowser.sessionToken
            )
            mediaController.registerCallback(MediaControllerCallback())
            isConnected.postValue(true)
        }

        // Invoked when the client is disconnected from the media browser.
        override fun onConnectionSuspended() {
            isConnected.postValue(false)
        }

        // Invoked when the connection to the media browser failed.
        override fun onConnectionFailed() {
            isConnected.postValue(false)
        }
    }

    private inner class MediaControllerCallback : MediaControllerCompat.Callback() {

        override fun onRepeatModeChanged(repeatMode: Int) {
            this@MediaSessionConnection.repeatMode.postValue(repeatMode)
            storage.sharedPreferences.edit().putInt(Constants.LAST_REPEAT_MODE, repeatMode).apply()
        }

        override fun onPlaybackStateChanged(state: PlaybackStateCompat?) {
            playbackState.postValue(state ?: EMPTY_PLAYBACK_STATE)
        }

        override fun onMetadataChanged(metadata: MediaMetadataCompat?) {
            nowPlaying.postValue(metadata ?: NOTHING_PLAYING)
        }

        override fun onShuffleModeChanged(shuffleMode: Int) {
            this@MediaSessionConnection.shuffleMode.postValue(shuffleMode)
            storage.sharedPreferences.edit().putInt(Constants.LAST_SHUFFLE_MODE, shuffleMode).apply()
        }

        /**
         * Normally if a [MediaBrowserServiceCompat] drops its connection the callback comes via
         * [MediaControllerCompat.Callback] (here). But since other connection status events
         * are sent to [MediaBrowserCompat.ConnectionCallback], we catch the disconnect here and
         * send it on to the other callback.
         */
        override fun onSessionDestroyed() {
            connectionCallback.onConnectionSuspended()
        }

        override fun onSessionEvent(event: String?, extras: Bundle?) {
            when (event) {
                Constants.NETWORK_FAILURE -> networkFailure.postValue(true)
            }
        }
    }

    companion object {
        // For Singleton instantiation.
        @Volatile
        private var instance: MediaSessionConnection? = null

        fun getInstance(context: Context, serviceComponent: ComponentName, storage: Storage) =
            instance ?: synchronized(this) {
                instance ?: MediaSessionConnection(context, serviceComponent, storage =  storage).also { instance = it }
            }
    }
}

// set when started at first not play or pause
// state of what playing
val EMPTY_PLAYBACK_STATE: PlaybackStateCompat = PlaybackStateCompat.Builder()
    .setState(PlaybackStateCompat.STATE_NONE, 0, 0f)
    .build()

// set when nothing playing
// info about what media playing
val NOTHING_PLAYING: MediaMetadataCompat = MediaMetadataCompat.Builder()
    .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, "")
    .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, 0)
    .build()