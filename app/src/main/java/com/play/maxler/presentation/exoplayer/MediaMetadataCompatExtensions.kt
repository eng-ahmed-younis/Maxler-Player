package com.play.maxler.presentation.exoplayer

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.support.v4.media.MediaBrowserCompat.MediaItem

import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import android.util.Log
import androidx.core.net.toUri
import androidx.room.ColumnInfo
import androidx.room.PrimaryKey

import com.google.android.exoplayer2.source.ConcatenatingMediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource


import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSource
import com.play.maxler.R
import com.play.maxler.common.utils.ImageUtils

import com.play.maxler.common.utils.Utils
import com.play.maxler.domain.entities.HistoryEntity
import com.play.maxler.domain.models.Song

/**
 * Useful extensions for [MediaMetadataCompat].
 */

inline val MediaDescriptionCompat.duration : Long
    get() = extras?.getLong(MediaMetadataCompat.METADATA_KEY_DURATION, 0L) ?: 0L

inline val MediaMetadataCompat.id: String?
    get() = getString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID)

inline val MediaMetadataCompat.title: String?
    get() = getString(MediaMetadataCompat.METADATA_KEY_TITLE)

inline val MediaMetadataCompat.artist: String?
    get() = getString(MediaMetadataCompat.METADATA_KEY_ARTIST)

inline val MediaMetadataCompat.duration
    get() = getLong(MediaMetadataCompat.METADATA_KEY_DURATION)

inline val MediaMetadataCompat.album: String?
    get() = getString(MediaMetadataCompat.METADATA_KEY_ALBUM)

inline val MediaMetadataCompat.albumId: Long
    get() = getLong(METADATA_KEY_ALBUM_ID)

inline val MediaMetadataCompat.author: String?
    get() = getString(MediaMetadataCompat.METADATA_KEY_AUTHOR)

inline val MediaMetadataCompat.writer: String?
    get() = getString(MediaMetadataCompat.METADATA_KEY_WRITER)

inline val MediaMetadataCompat.composer: String?
    get() = getString(MediaMetadataCompat.METADATA_KEY_COMPOSER)

inline val MediaMetadataCompat.compilation: String?
    get() = getString(MediaMetadataCompat.METADATA_KEY_COMPILATION)

inline val MediaMetadataCompat.date: String?
    get() = getString(MediaMetadataCompat.METADATA_KEY_DATE)

inline val MediaMetadataCompat.year: String?
    get() = getString(MediaMetadataCompat.METADATA_KEY_YEAR)

inline val MediaMetadataCompat.genre: String?
    get() = getString(MediaMetadataCompat.METADATA_KEY_GENRE)

inline val MediaMetadataCompat.trackNumber
    get() = getLong(MediaMetadataCompat.METADATA_KEY_TRACK_NUMBER)

inline val MediaMetadataCompat.trackCount
    get() = getLong(MediaMetadataCompat.METADATA_KEY_NUM_TRACKS)

inline val MediaMetadataCompat.discNumber
    get() = getLong(MediaMetadataCompat.METADATA_KEY_DISC_NUMBER)

inline val MediaMetadataCompat.albumArtist: String?
    get() = getString(MediaMetadataCompat.METADATA_KEY_ALBUM_ARTIST)

inline val MediaMetadataCompat.art: Bitmap
    get() = getBitmap(MediaMetadataCompat.METADATA_KEY_ART)

inline val MediaMetadataCompat.artUri: Uri
    get() = this.getString(MediaMetadataCompat.METADATA_KEY_ART_URI).toUri()

inline val MediaMetadataCompat.albumArt: Bitmap?
    get() = getBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART)

inline val MediaMetadataCompat.albumArtUri: Uri
    get() = this.getString(MediaMetadataCompat.METADATA_KEY_ALBUM_ART_URI).toUri()

inline val MediaMetadataCompat.userRating
    get() = getLong(MediaMetadataCompat.METADATA_KEY_USER_RATING)

inline val MediaMetadataCompat.rating
    get() = getLong(MediaMetadataCompat.METADATA_KEY_RATING)

inline val MediaMetadataCompat.displayTitle: String?
    get() = getString(MediaMetadataCompat.METADATA_KEY_DISPLAY_TITLE)

inline val MediaMetadataCompat.displaySubtitle: String?
    get() = getString(MediaMetadataCompat.METADATA_KEY_DISPLAY_SUBTITLE)

inline val MediaMetadataCompat.displayDescription: String?
    get() = getString(MediaMetadataCompat.METADATA_KEY_DISPLAY_DESCRIPTION)

inline val MediaMetadataCompat.displayIcon: Bitmap
    get() = getBitmap(MediaMetadataCompat.METADATA_KEY_DISPLAY_ICON)

inline val MediaMetadataCompat.displayIconUri: Uri
    get() = this.getString(MediaMetadataCompat.METADATA_KEY_DISPLAY_ICON_URI).toUri()

inline val MediaMetadataCompat.mediaUri: Uri
    get() = this.getString(MediaMetadataCompat.METADATA_KEY_MEDIA_URI).toUri()

inline val MediaMetadataCompat.downloadStatus
    get() = getLong(MediaMetadataCompat.METADATA_KEY_DOWNLOAD_STATUS)

/**
 * Custom property for storing whether a [MediaMetadataCompat] item represents an
 * item that is [MediaItem.FLAG_BROWSABLE] or [MediaItem.FLAG_PLAYABLE].
 */
inline val MediaMetadataCompat.flag
    get() = this.getLong(METADATA_KEY_FLAGS).toInt()


/**
 * Useful extensions for [MediaMetadataCompat.Builder].
 */

// These do not have getters, so create a message for the error.
const val NO_GET = "Property does not have a 'get'"

inline var MediaMetadataCompat.Builder.id: String
    @Deprecated(NO_GET, level = DeprecationLevel.ERROR)
    get() = throw IllegalAccessException("Cannot get from MediaMetadataCompat.Builder")
    set(value) {
        putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, value)
    }

inline var MediaMetadataCompat.Builder.title: String?
    @Deprecated(NO_GET, level = DeprecationLevel.ERROR)
    get() = throw IllegalAccessException("Cannot get from MediaMetadataCompat.Builder")
    set(value) {
        putString(MediaMetadataCompat.METADATA_KEY_TITLE, value)
    }

inline var MediaMetadataCompat.Builder.artist: String?
    @Deprecated(NO_GET, level = DeprecationLevel.ERROR)
    get() = throw IllegalAccessException("Cannot get from MediaMetadataCompat.Builder")
    set(value) {
        putString(MediaMetadataCompat.METADATA_KEY_ARTIST, value)
    }

inline var MediaMetadataCompat.Builder.album: String?
    @Deprecated(NO_GET, level = DeprecationLevel.ERROR)
    get() = throw IllegalAccessException("Cannot get from MediaMetadataCompat.Builder")
    set(value) {
        putString(MediaMetadataCompat.METADATA_KEY_ALBUM, value)
    }

inline var MediaMetadataCompat.Builder.albumId: Long
    @Deprecated(NO_GET, level = DeprecationLevel.ERROR)
    get() = throw IllegalAccessException("Cannot get from MediaMetadataCompat.Builder")
    set(value) {
        putLong(METADATA_KEY_ALBUM_ID, value)
    }

inline var MediaMetadataCompat.Builder.duration: Long
    @Deprecated(NO_GET, level = DeprecationLevel.ERROR)
    get() = throw IllegalAccessException("Cannot get from MediaMetadataCompat.Builder")
    set(value) {
        putLong(MediaMetadataCompat.METADATA_KEY_DURATION, value)
    }

inline var MediaMetadataCompat.Builder.genre: String?
    @Deprecated(NO_GET, level = DeprecationLevel.ERROR)
    get() = throw IllegalAccessException("Cannot get from MediaMetadataCompat.Builder")
    set(value) {
        putString(MediaMetadataCompat.METADATA_KEY_GENRE, value)
    }

inline var MediaMetadataCompat.Builder.mediaUri: String?
    @Deprecated(NO_GET, level = DeprecationLevel.ERROR)
    get() = throw IllegalAccessException("Cannot get from MediaMetadataCompat.Builder")
    set(value) {
        putString(MediaMetadataCompat.METADATA_KEY_MEDIA_URI, value)
    }

inline var MediaMetadataCompat.Builder.albumArtUri: String?
    @Deprecated(NO_GET, level = DeprecationLevel.ERROR)
    get() = throw IllegalAccessException("Cannot get from MediaMetadataCompat.Builder")
    set(value) {
        putString(MediaMetadataCompat.METADATA_KEY_ALBUM_ART_URI, value)
    }

inline var MediaMetadataCompat.Builder.albumArt: Bitmap?
    @Deprecated(NO_GET, level = DeprecationLevel.ERROR)
    get() = throw IllegalAccessException("Cannot get from MediaMetadataCompat.Builder")
    set(value) {
        putBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART, value)
    }

inline var MediaMetadataCompat.Builder.trackNumber: Long
    @Deprecated(NO_GET, level = DeprecationLevel.ERROR)
    get() = throw IllegalAccessException("Cannot get from MediaMetadataCompat.Builder")
    set(value) {
        putLong(MediaMetadataCompat.METADATA_KEY_TRACK_NUMBER, value)
    }

inline var MediaMetadataCompat.Builder.trackCount: Long
    @Deprecated(NO_GET, level = DeprecationLevel.ERROR)
    get() = throw IllegalAccessException("Cannot get from MediaMetadataCompat.Builder")
    set(value) {
        putLong(MediaMetadataCompat.METADATA_KEY_NUM_TRACKS, value)
    }

inline var MediaMetadataCompat.Builder.displayTitle: String?
    @Deprecated(NO_GET, level = DeprecationLevel.ERROR)
    get() = throw IllegalAccessException("Cannot get from MediaMetadataCompat.Builder")
    set(value) {
        putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_TITLE, value)
    }

inline var MediaMetadataCompat.Builder.displaySubtitle: String?
    @Deprecated(NO_GET, level = DeprecationLevel.ERROR)
    get() = throw IllegalAccessException("Cannot get from MediaMetadataCompat.Builder")
    set(value) {
        putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_SUBTITLE, value)
    }

inline var MediaMetadataCompat.Builder.displayDescription: String?
    @Deprecated(NO_GET, level = DeprecationLevel.ERROR)
    get() = throw IllegalAccessException("Cannot get from MediaMetadataCompat.Builder")
    set(value) {
        putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_DESCRIPTION, value)
    }

inline var MediaMetadataCompat.Builder.displayIconUri: String?
    @Deprecated(NO_GET, level = DeprecationLevel.ERROR)
    get() = throw IllegalAccessException("Cannot get from MediaMetadataCompat.Builder")
    set(value) {
        putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_ICON_URI, value)
    }

inline var MediaMetadataCompat.Builder.downloadStatus: Long
    @Deprecated(NO_GET, level = DeprecationLevel.ERROR)
    get() = throw IllegalAccessException("Cannot get from MediaMetadataCompat.Builder")
    set(value) {
        putLong(MediaMetadataCompat.METADATA_KEY_DOWNLOAD_STATUS, value)
    }

/**
 * Custom property for storing whether a [MediaMetadataCompat] item represents an
 * item that is [MediaItem.FLAG_BROWSABLE] or [MediaItem.FLAG_PLAYABLE].
 */

inline var MediaMetadataCompat.Builder.flag: Int
    @Deprecated(NO_GET, level = DeprecationLevel.ERROR)
    get() = throw IllegalAccessException("Cannot get from MediaMetadataCompat.Builder")
    set(value) {
        putLong(METADATA_KEY_FLAGS, value.toLong())
    }


/**
 * Extension method for [MediaMetadataCompat.Builder] to set the fields with
 * [MediaStore] result cursor
 */

fun MediaMetadataCompat.Builder.songToMediaMetadata(context: Context , songs: List<Song>) : MediaMetadataCompat.Builder{
    // convert songs that get from local MediaMetadata that player can play
    val art = ImageUtils.getBitmapFromVectorDrawable(context, drawableId = R.drawable.maxler_img)

    songs.map {
        id = it.id.toString()
        title = it.title
        artist = it.artistName
        album = it.albumName
        albumId = it.albumId
        duration = it.duration
        mediaUri = it.data
        trackNumber = it.songNumber!!
        trackCount = it.count!!
        flag = MediaItem.FLAG_PLAYABLE
        albumArt = art
        albumArtUri = ImageUtils.getAlbumArtUri(it.albumId).toString()
        // To make things easier for *displaying* these, set the display properties as well.
        displayTitle = it.title
        displaySubtitle = it.artistName
        displayDescription = it.albumName
        downloadStatus = MediaDescriptionCompat.STATUS_DOWNLOADED
    }

    // Allow it to be used in the typical builder style.
    return this
}

fun MediaMetadataCompat.Builder.from(cursor: Cursor, count: Long, art: Bitmap?): MediaMetadataCompat.Builder {
    val durationMs = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION))
    val songTitle = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE))
    val songArtist = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST))
    val songAlbum = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM))
    val songAlbumId = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID))
    val artWorkUri: Uri = ContentUris.withAppendedId(
        Utils.artworkUri,
        cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID))
    )
    val songNumber: Long =
        Utils.getTrackNumber(cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Playlists.Members.TRACK))).toLongOrNull()
            ?: 0

    id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)).toString()
    title = songTitle
    artist = songArtist
    album = songAlbum
    albumId = songAlbumId
    duration = durationMs
    mediaUri = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA))
    trackNumber = songNumber
    trackCount = count
    flag = MediaItem.FLAG_PLAYABLE
    albumArt = art
    albumArtUri = artWorkUri.toString()
    // To make things easier for *displaying* these, set the display properties as well.
    displayTitle = songTitle
    displaySubtitle = songArtist
    displayDescription = songAlbum
    downloadStatus = MediaDescriptionCompat.STATUS_DOWNLOADED


    // Allow it to be used in the typical builder style.
    return this
}



fun List<MediaMetadataCompat>.toHistoryEntityes() : List<HistoryEntity>{
    return map {
        it.toHistoryEntity()
    }

}

fun MediaMetadataCompat.toHistoryEntity() : HistoryEntity{
    return HistoryEntity(
        id = id!!.toLong(),
        title = title!!,
        trackNumber = trackNumber.toInt(),
        year = year!!.toInt(),
        duration = duration,
        data = mediaUri.toString(),
        dateTaken = "",
        dateModified = 0L,
        albumId = albumId,
        albumName = album!!,
        artistId = 0L,
        artistName = artist!!,
        composer = composer,
        albumArtist = albumArtist,
        timePlayed = System.currentTimeMillis()
    )
}


/**
 * Extension method for building an [ProgressiveMediaSource] from a [MediaMetadataCompat] object.
 *
 * For convenience, place the [MediaDescriptionCompat] into the tag so it can be retrieved later.
 */

/*
fun MediaMetadataCompat.toMediaSource(dataSourceFactory: DataSource.Factory): ProgressiveMediaSource =
    ProgressiveMediaSource.Factory(dataSourceFactory)
        .setTag(description)
        .createMediaSource(mediaUri)

 //* Extension method for building a [ConcatenatingMediaSource] given a [List]
// * of [MediaMetadataCompat] objects.
fun List<MediaMetadataCompat>.toMediaSource(
    dataSourceFactory: DataSource.Factory
): ConcatenatingMediaSource {

    val concatenatingMediaSource = ConcatenatingMediaSource()
    forEach {
        concatenatingMediaSource.addMediaSource(it.toMediaSource(dataSourceFactory))
    }
    return concatenatingMediaSource
}
*/

/**
 * Custom property that holds whether an item is [MediaItem.FLAG_BROWSABLE] or
 * [MediaItem.FLAG_PLAYABLE].
 */
 */



fun List<MediaMetadataCompat>.asMediaSource(
    dataSourceFactory: DefaultDataSource.Factory ,
    metaData: List<MediaMetadataCompat>
): ConcatenatingMediaSource {
    val concatenatingMediaSource = ConcatenatingMediaSource()
    metaData.forEach {
        val mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
            .createMediaSource(
                com.google.android.exoplayer2.MediaItem.fromUri(
                    it.getString(
                        MediaMetadataCompat.METADATA_KEY_MEDIA_URI
                    ).toUri()
                )
            )
        concatenatingMediaSource.addMediaSource(mediaSource)
    }
    return concatenatingMediaSource
}



const val METADATA_KEY_FLAGS = "com.playback.METADATA_KEY_FLAGS"
const val METADATA_KEY_ALBUM_ID = "com.playback.METADATA_KEY_ALBUM_ID"
