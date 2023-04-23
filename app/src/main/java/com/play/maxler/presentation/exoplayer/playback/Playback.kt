package com.play.maxler.presentation.exoplayer.playback

/**
 * @author ahmed ali
 */
interface Playback {

    fun setDataSource(path: String?): Boolean
    fun setNextDataSource(path: String?)
    fun setCallbacks(callbacks: PlaybackCallbacks?)
    val isInitialized: Boolean
    fun start(): Boolean
    fun stop()
    fun release()
    fun pause(): Boolean
    val isPlaying: Boolean
    fun duration(): Int
    fun position(): Int
    fun seek(whereto: Int): Int

    /*    boolean setVolume(float vol);*/
    fun setVolume(l: Float, r: Float): Boolean
    fun setAudioSessionId(sessionId: Int): Boolean
    val audioSessionId: Int

    interface PlaybackCallbacks {
        fun onTrackWentToNext()
        fun onTrackEnded()
    }
}