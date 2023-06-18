package com.play.maxler.presentation.exoplayer.playback

import android.media.session.PlaybackState
import android.util.Log
import android.widget.Toast
import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.PlaybackException
import com.google.android.exoplayer2.Player
import com.play.maxler.presentation.exoplayer.MediaPlaybackService
import com.play.maxler.presentation.exoplayer.MediaSessionConnection

class MusicPlayerEventListener(
    private val musicService: MediaPlaybackService,
    private val exoPlayer: ExoPlayer,
) : Player.Listener{


    override fun onPlayerError(error: PlaybackException) {
        super.onPlayerError(error)
        Log.i("maxler_player","An unknown error occured ${error.message.toString()}")

    }

/*    override fun onPlayWhenReadyChanged(playWhenReady: Boolean, reason: Int) {
        super.onPlayWhenReadyChanged(playWhenReady, reason)
        if (playWhenReady == )

    }
    override fun onPlaybackStateChanged(playbackState: Int) {
        super.onPlaybackStateChanged(playbackState)
        if (playbackState == Player.STATE_READY && mediaSessionConnection.playbackState.value!!.playbackState == PlaybackState.STATE_NONE ){
            musicService.stopForeground(false)
        }
    }

    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
        super.onPlayerStateChanged(playWhenReady, playbackState)
    }*/
    /*
    onPlayer
    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
        super.onPlayerStateChanged(playWhenReady, playbackState)
        if(playbackState == Player.STATE_READY && !playWhenReady) {
            musicService.stopForeground(false)
        }
    }*/

}