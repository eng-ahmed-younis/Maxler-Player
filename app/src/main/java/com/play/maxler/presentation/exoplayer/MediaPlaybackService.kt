package com.play.maxler.presentation.exoplayer

import android.app.Notification
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.app.PendingIntent.FLAG_MUTABLE
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Build

import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.media.MediaBrowserServiceCompat
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.Timeline
import com.play.maxler.common.data.Constants
import com.play.maxler.common.data.Constants.NOTIFICATION_LARGE_ICON_SIZE
import com.play.maxler.data.local.preferences.Storage
import com.play.maxler.domain.entities.HistoryEntity
import com.play.maxler.domain.repository.HistoryRepository
import com.play.maxler.domain.repository.MainRepository
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.qualifiers.ApplicationContext
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector.PlaybackPreparer
import com.google.android.exoplayer2.ext.mediasession.TimelineQueueNavigator
import com.google.android.exoplayer2.upstream.DefaultDataSource
import com.play.maxler.common.data.Constants.NETWORK_ERROR
import com.play.maxler.presentation.exoplayer.playback.MusicPlayerEventListener

import kotlinx.coroutines.*
import javax.inject.Inject
/**
 * Note: The recommended implementation of MediaBrowserService is MediaBrowserServiceCompat.
 * which is defined in the media-compat support library.
 * Throughout this page the term "MediaBrowserService" refers to an instance of of MediaBrowserServiceCompat.
 * */
@AndroidEntryPoint
class MediaPlaybackService : MediaBrowserServiceCompat() {

    @Inject
    lateinit var packageValidator: PackageValidator
    @Inject
    lateinit var storage: Storage
    @Inject
    lateinit var glide: RequestManager
    @Inject
    lateinit var repository: MainRepository
    @Inject
    lateinit var mediaSource: MediaStoreSource
    @Inject
    lateinit var browseTree: BrowseTree
    @Inject
    lateinit var dataSourceFactory: DefaultDataSource.Factory
    @Inject
    lateinit var playbackPreparer: com.play.maxler.presentation.exoplayer.PlaybackPreparer

    @Inject
    lateinit var exoPlayer: ExoPlayer

    private lateinit var mediaSession: MediaSessionCompat
    private lateinit var mediaSessionConnector: MediaSessionConnector
    private lateinit var mediaController: MediaControllerCompat
    private lateinit var notificationBuilder: NotificationBuilder
    private lateinit var notificationManager: NotificationManagerCompat
    private lateinit var becomingNoisyReceiver: BecomingNoisyReceiver
    private lateinit var musicPlayerEventListener: MusicPlayerEventListener

    private val serviceJob = SupervisorJob()
    private val serviceScope = CoroutineScope(Dispatchers.Main + serviceJob)

    private var isPlayerInitialized = false


    /*if(packageName != null){
        PackageManager packageManager = this.packageManager
        PendingIntent pendingIntent = pm.getLaunchIntentForPackage(packageName);
    }*/

    // Build a PendingIntent that can be used to launch the UI.
    /*  private val sessionActivityPendingActivity: PendingIntent? =
       this.packageManager?.getLaunchIntentForPackage(this.packageName)?.let {
        PendingIntent.getActivity(this, 0, it, 0)
    }*/
    private lateinit var sessionActivityPendingActivity: PendingIntent
    private var isForegroundService = false

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate() {
        super.onCreate()

        /** Targeting S+ (version 31 and above) requires that one of FLAG_IMMUTABLE or FLAG_MUTABLE be specified when creating
         *  a PendingIntent. Strongly consider using FLAG_IMMUTABLE, only use FLAG_MUTABLE if some functionality
         *  depends on the PendingIntent being mutable, e.g.if it needs to be used with inline replies or bubbles.
         */
        if (packageName != null) {
            sessionActivityPendingActivity =
                this.packageManager?.getLaunchIntentForPackage(this.packageName)?.let {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        PendingIntent.getActivity(this, 0, it, FLAG_IMMUTABLE)
                    } else {
                        PendingIntent.getActivity(this, 0, it, 0)
                    }
                }!!
        }
        // Create a MediaSession
        // active in on create and disable in on destroy
        mediaSession = MediaSessionCompat(
            this,
            this.javaClass.name,
        ).apply {
            // Enable callbacks from MediaButtons and TransportControls
            setSessionActivity(sessionActivityPendingActivity)
            isActive = true
        }


        // In order for [MediaBrowserCompat.ConnectionCallback.onConnected] to be called,
        // a [MediaSessionCompat.Token] needs to be set on the [MediaBrowserServiceCompat].
        // Note that this must be set by the time [onGetRoot] returns otherwise the connection will fail silently
        // and the system will not even call [MediaBrowserCompat.ConnectionCallback.onConnectionFailed]
        sessionToken = mediaSession.sessionToken
        // Because ExoPlayer will manage the MediaSession, add the service as a callback for
        // state changes.
        mediaController = MediaControllerCompat(this, mediaSession).also {
            it.registerCallback(MediaControllerCallback())
        }
        notificationBuilder = NotificationBuilder(this)
        notificationManager = NotificationManagerCompat.from(this)
        becomingNoisyReceiver = BecomingNoisyReceiver(this, mediaSession.sessionToken)
        // The media library is built from the MediaStore.
        // We'll create the source here, and then use
        // a suspend function to perform the query and initialization off the main thread

        serviceScope.launch {
            /** sed to get songs from repository and convert it into MediaMetadataCompat that playback can play*/
            mediaSource.load()
        }


        // ExoPlayer will manage the MediaSession for us.
        mediaSessionConnector = MediaSessionConnector(mediaSession).also {
            // Produces DataSource instances through which media data is loaded.
            // Create the PlaybackPreparer of the media session connector.
            it.setPlayer(exoPlayer)
            it.setPlaybackPreparer(playbackPreparer)
            it.setQueueNavigator(MusicQueueNavigator())
        }

        musicPlayerEventListener = MusicPlayerEventListener(this, exoPlayer)
        exoPlayer.addListener(musicPlayerEventListener)


    }


    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        exoPlayer.stop()
    }

    /* onGetRoot() controls access to the service
    * To allow "clients" to connect to your service and browse its media content,
    * onGetRoot() must return a non-null BrowserRoot which is a root ID that represents your content hierarchy.
    * headset buttons , android audio , yourApp
    * */
/*    override fun onGetRoot(
        clientPackageName: String,
        clientUid: Int,
        rootHints: Bundle?
    ): BrowserRoot? {
      //  return BrowserRoot(Constants.MY_MEDIA_ROOT_ID,null)
        // By default, all known clients are permitted to search, but only tell unknown callers about search if
        // permitted by the BrowserTree
        Log.i("maxler_player","onGetRoot:  id $clientUid")

        val isKnownCaller = packageValidator.isKnownCaller(clientPackageName, clientUid)
        val rootExtras = Bundle().apply {
           putBoolean(Constants.MEDIA_SEARCH_SUPPORTED, isKnownCaller || browseTree.searchableByUnknownCaller)
            putBoolean(Constants.CONTENT_STYLE_SUPPORTED, true)
            putInt(Constants.CONTENT_STYLE_BROWSABLE_HINT, Constants.CONTENT_STYLE_GRID)
            putInt(Constants.CONTENT_STYLE_PLAYABLE_HINT, Constants.CONTENT_STYLE_LIST)
        }
        return if (isKnownCaller) {
            // The caller is allowed to browse, so return the root
            BrowserRoot(Constants.BROWSABLE_ROOT, rootExtras)
        } else {
            // Unknown caller. Return a root without any content so the system doesn't disconnect the app
            BrowserRoot(Constants.EMPTY_ROOT, rootExtras)
        }
    }*/

/*

    override fun onLoadChildren(parentId: String, result: Result<List<MediaBrowserCompat.MediaItem>>) {
        // If the media source is ready, the results will be set synchronously here.
        val resultsSent = mediaSource.whenReady { initialized ->
            Log.i("maxler_player","onLoadChildren:  id $parentId")

            if(initialized) {
                // this will receive it into mediaBrowser.subscribe(parentId, callback)
                val children: List<MediaBrowserCompat.MediaItem>? = browseTree[parentId]?.map {
                    MediaBrowserCompat.MediaItem(it.description, it.flag)
                }

                result.sendResult(children)
                if(!isPlayerInitialized && mediaSource.mediaSongs.isNotEmpty()) {
                    isPlayerInitialized = true
                }
            } else {
                mediaSession.sendSessionEvent(NETWORK_ERROR, null)
                result.sendResult(null)
            }

            
         */
/*   if (initialized) {
                // this will receive it into mediaBrowser.subscribe(parentId, callback)
                val children: List<MediaBrowserCompat.MediaItem>? = browseTree[parentId]?.map {
                    MediaBrowserCompat.MediaItem(it.description, it.flag)
                }
            //    Log.i("maxler_player","onLoadChildren:  id ${children!![0].description.title.toString()}")

                result.sendResult(children)
            } else {
                mediaSession.sendSessionEvent(Constants.NETWORK_FAILURE, null)
                result.sendResult(null)
            }*//*

        }
        // If the results are not ready, the service must "detach" the results before
        // the method returns. After the source is ready, the lambda above will run,
        // and the caller will be notified that the results are ready.
        if (!resultsSent) {
            result.detach()
        }
    }
*/





   override fun onGetRoot(
        clientPackageName: String,
        clientUid: Int,
        rootHints: Bundle?
    ): BrowserRoot? {
       return BrowserRoot(Constants.MY_MEDIA_ROOT_ID, null)
    }

    override fun onLoadChildren(
        parentId: String,
        result: Result<MutableList<MediaBrowserCompat.MediaItem>>
    ) {
        Log.i("maxler_player","parentId: is ${parentId} ")
        when(parentId) {
            Constants.MY_MEDIA_ROOT_ID -> {
                val resultsSent = mediaSource.whenReady { isInitialized ->
                    Log.i("maxler_player","isInitialized: is $isInitialized ")
                    if(isInitialized) {
                        Log.i("maxler_player","onLoadChildren: is ${mediaSource.asMediaItems().first().description.title} ")
                        Log.i("maxler_player","onLoadChildren: songs  is ${mediaSource.asMediaItems().size} ")
                        result.sendResult(mediaSource.asMediaItems())
                        serviceScope.launch {
                            if (!isPlayerInitialized && repository.songs().isNotEmpty()) {
                                isPlayerInitialized = true
                            }
                        }
                    } else {
                        mediaSession.sendSessionEvent(NETWORK_ERROR, null)
                        result.sendResult(null)
                    }
                }
                if(!resultsSent) {
                    result.detach()
                }
            }
        }
    }





    override fun onDestroy() {
        super.onDestroy()
        exoPlayer.removeListener(musicPlayerEventListener)
        mediaSession.isActive = false
        // clean up mediaSession
        mediaSession.release()
        exoPlayer.release()
        // Cancel coroutines when the service is going away.
        serviceScope.cancel()

    }

    // Removes the playback notification.
    // Since `stopForeground(false)` has already been called in [MediaControllerCallback.onPlaybackStateChanged],
    // it's possible to cancel the notification to cancel the notification with
    // `notificationManager.cancel(PLAYBACK_NOTIFICATION)` if minSdkVersion is >= [Build.VERSION_CODES.LOLLIPOP].
    //
    // Prior to [Build.VERSION_CODES.LOLLIPOP], notifications associated with a foreground service remained marked
    // "ongoing" even after calling [Service.stopForeground], and cannot be cancelled normally.
    //
    // Fortunately, it's possible to simple call [Service.stopForeground] a second time, this time with `true`.
    // his won't change anything about the service's state, but will simply remove the notification.
    private fun removePlaybackNotification() = stopForeground(STOP_FOREGROUND_REMOVE)

    // Returns a list of [MediaItem]s that match the given search query
    override fun onSearch(query: String, extras: Bundle?, result: Result<List<MediaBrowserCompat.MediaItem>>) {
        val resultSent = mediaSource.whenReady { initialized ->
            if (initialized) {
                val resultList =
                    mediaSource.search(query, extras ?: Bundle.EMPTY).map {
                        MediaBrowserCompat.MediaItem(
                            it.description,
                            it.flag
                        )
                    }
                result.sendResult(resultList)
            }
        }
        if (!resultSent) {
            result.detach()
        }
    }




    private inner class MediaControllerCallback : MediaControllerCompat.Callback() {
        val notificationTarget = NotificationTarget()
        var largeBitmap: Bitmap? = null
        // item played
        override fun onMetadataChanged(metadata: MediaMetadataCompat?) {
            updateNotification(mediaController.playbackState)
            addToRecentlyPlayed(metadata, mediaController.playbackState)
            // so we can access to last position of media item played duration when MediaMetadata changed
            persistPosition()
        }
        // state of item played
        override fun onPlaybackStateChanged(state: PlaybackStateCompat?) {
            updateNotification(state)
            addToRecentlyPlayed(mediaController.metadata, state)
            // so we can access to last position of media item played duration when PlaybackState changed if playing and pause it
            // save last position media item stopped se when restart play media play from last song duration stopped
            persistPosition()
        }

        private fun updateNotification(state: PlaybackStateCompat?) {
            if (state == null) return

            when (val updatedState = state.state) {
                PlaybackStateCompat.STATE_PLAYING,
                PlaybackStateCompat.STATE_BUFFERING -> initiatePlayback(updatedState)
                else -> terminatePlayback(updatedState)
            }
        }

        private fun terminatePlayback(state: Int) {
            becomingNoisyReceiver.unregister()
            if (isForegroundService) {
                stopForeground(STOP_FOREGROUND_REMOVE)
                isForegroundService = false
                // If playback has ended, also stop the service
                if (state == PlaybackStateCompat.STATE_NONE) {
                    stopSelf()
                }
                val notification = buildNotification(state)
                if (notification != null) {
                    notificationManager.notify(Constants.PLAYBACK_NOTIFICATION, notification)
                } else {
                    removePlaybackNotification()
                }
            }
        }

        private fun initiatePlayback(state: Int) {
            becomingNoisyReceiver.register()
            // This may look strange, but the documentation for [Service.startForeground]
            // notes that "calling this method does *not* put the service in the started
            // state itself, even though the name sounds like it."
            buildNotification(state)?.let {
                notificationManager.notify(Constants.PLAYBACK_NOTIFICATION, it)
                loadLargeIcon()
                // service is not start
                if (!isForegroundService) {
                    ContextCompat.startForegroundService(
                        applicationContext, Intent(applicationContext, this@MediaPlaybackService.javaClass)
                    )
                    startForeground(Constants.PLAYBACK_NOTIFICATION, it)
                    isForegroundService = true
                }
            }
        }

        private fun loadLargeIcon() {
            glide.asBitmap()
                .skipMemoryCache(false)
                .load(mediaController.metadata.description.iconUri)
                .into(notificationTarget)
        }

        private fun buildNotification(state: Int): Notification? {
            // Skip building a notification when state is "none" and metadata is null
            return if (mediaController.metadata != null
                && state != PlaybackStateCompat.STATE_NONE
            ) {
                notificationBuilder.buildNotification(mediaSession.sessionToken, largeBitmap)
            } else {
                null
            }
        }

        private fun addToRecentlyPlayed(metadata: MediaMetadataCompat?, state: PlaybackStateCompat?) {
            if (metadata?.id != null && state?.isPlaying == true) {
                serviceScope.launch {
                    val played: HistoryEntity = metadata.toHistoryEntity()
                    repository.insert(played)
                    repository.trim()
                    // last id of media item olayed
                    storage.sharedPreferences.edit().putString(Constants.LAST_ID, metadata.id).apply()
                }}
        }

        private fun persistPosition() {
            // default playback state is STATE_NONE
            // this indicates that { no media has been added yet },
            // or the [performer has been reset] and has [no content to play].
            // this check that state is not STATE_NONE
            if (mediaController.playbackState.started) {
              storage.sharedPreferences.edit().putLong(Constants.LAST_POSITION, exoPlayer.contentPosition).apply()
            }
        }



        private inner class NotificationTarget :
            CustomTarget<Bitmap>(NOTIFICATION_LARGE_ICON_SIZE, NOTIFICATION_LARGE_ICON_SIZE) {
            override fun onStart() { largeBitmap = null }
            override fun onLoadCleared(placeholder: Drawable?) { largeBitmap = null }
            override fun onLoadFailed(errorDrawable: Drawable?) { largeBitmap = null }
            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                notificationManager.notify(
                    Constants.PLAYBACK_NOTIFICATION, notificationBuilder.buildNotification
                        (mediaSession.sessionToken, resource)
                )
                largeBitmap = resource
            }
        }
    }

    // Helper class to retrieve the the Metadata necessary for the ExoPlayer MediaSession connection
    // extension to call [MediaSessionCompat.setMetadata].
    private inner class MusicQueueNavigator : TimelineQueueNavigator(mediaSession) {
        override fun getMediaDescription(player: Player, windowIndex: Int): MediaDescriptionCompat {
            return mediaSource.mediaSongs[windowIndex].description
        }
    }



}
