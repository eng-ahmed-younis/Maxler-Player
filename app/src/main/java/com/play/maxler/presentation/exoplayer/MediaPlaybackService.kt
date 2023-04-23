package com.play.maxler.presentation.exoplayer/*
package com.play.maxler.presentation.service

import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.media.MediaBrowserServiceCompat
import android.service.media.MediaBrowserService
import com.play.maxler.presentation.service.playback.Playback

*/
/**
 * Note: The recommended implementation of MediaBrowserService is MediaBrowserServiceCompat. which is defined in the media-compat support library.
 * Throughout this page the term "MediaBrowserService" refers to an instance of of MediaBrowserServiceCompat.*//*


private const val MY_MEDIA_ROOT_ID = "media_root_id"
private const val MY_EMPTY_MEDIA_ROOT_ID = "empty_root_id"


private const val LOG_TAG = "mediaSession"

class MediaPlaybackService : MediaBrowserServiceCompat() ,Playback.PlaybackCallbacks{

    private var mediaSession: MediaSessionCompat? = null
    private lateinit var stateBuilder: PlaybackStateCompat.Builder


    */
/*
* When the service receives the onCreate() lifecycle callback method it should perform these steps:
    Create and initialize the media session
    Set the media session callback
    Set the media session token
*//*

    override fun onCreate() {
        super.onCreate()
    // Create a MediaSessionCompat
        mediaSession = MediaSessionCompat(baseContext, LOG_TAG).apply {
            // Enable callbacks from MediaButtons and TransportControls
            setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS
                    or MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS
            )
            // Set an initial PlaybackState with ACTION_PLAY, so media buttons can start the player
            stateBuilder = PlaybackStateCompat.Builder()
                .setActions(PlaybackStateCompat.ACTION_PLAY
                        or PlaybackStateCompat.ACTION_PLAY_PAUSE
                )
            setPlaybackState(stateBuilder.build())
            // MySessionCallback() has methods that handle callbacks from a media controller
            setCallback(MySessionCallback())
            // Set the session's token so that client activities can communicate with it.
            setSessionToken(sessionToken)
        }

    }


    */
/*onGetRoot() controls access to the service
    *To allow ""clients"" to connect to your service and browse its media content,
    * onGetRoot() must return a non-null BrowserRoot which is a root ID that represents your content hierarchy.
    *
    *
    *
    * *//*

    override fun onGetRoot(
        clientPackageName: String,
        clientUid: Int,
        rootHints: Bundle?,
    ): MediaBrowserServiceCompat.BrowserRoot? {

        return if ( allowBrowsing())
    }


    override fun onLoadChildren(
        parentId: String,
        result: Result<MutableList<MediaBrowserCompat.MediaItem>>,
    ) {

    }

    override fun onTrackWentToNext() {
        TODO("Not yet implemented")
    }

    override fun onTrackEnded() {
        TODO("Not yet implemented")
    }
}*/
