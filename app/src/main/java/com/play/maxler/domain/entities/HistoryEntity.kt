
package com.play.maxler.domain.entities

import android.support.v4.media.MediaMetadataCompat
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.play.maxler.common.data.Model
import com.play.maxler.presentation.exoplayer.duration
import com.play.maxler.presentation.exoplayer.id
import com.play.maxler.presentation.exoplayer.mediaUri
import com.play.maxler.presentation.exoplayer.title
@Entity(tableName = "history_table")
data class HistoryEntity(
    @PrimaryKey
    val id: Long,
    val title: String,
    @ColumnInfo(name = "track_number")
    val trackNumber: Int,
    val year: Int,
    val duration: Long,
    val data: String,
    @ColumnInfo(name = "date_taken")
    val dateTaken : String,
    @ColumnInfo(name = "date_modified")
    val dateModified: Long,
    @ColumnInfo(name = "album_id")
    val albumId: Long,
    @ColumnInfo(name = "album_name")
    val albumName: String,
    @ColumnInfo(name = "artist_id")
    val artistId: Long,
    @ColumnInfo(name = "artist_name")
    val artistName: String,
    val composer: String?,
    @ColumnInfo(name = "album_artist")
    val albumArtist: String?,
    @ColumnInfo(name = "time_played")
    val timePlayed: Long
)



/*

@Entity(tableName = "recently_played_table")
data class HistoryEntity(
    @PrimaryKey override val id: String, // media id
    val path: String,
    val artist: String,
    val album: String,
    val title: String,
    val duration: Long,
    val entryDate: Long,
    @Ignore
    var isPlaying: Boolean = false
) : Model() {

    constructor(
        id: String,
        path: String,
        artist: String,
        album: String,
        title: String,
        duration: Long,
        entryDate: Long
    ) : this(id, path, artist, album, title, duration, entryDate, false)

    constructor(meta: MediaMetadataCompat) : this(
        id = meta.id!!,
        path = meta.mediaUri.toString(),
        artist = meta.description.subtitle?.toString() ?: "",
        title = meta.title ?: "",
        album = meta.description.description?.toString() ?: "",
        duration = meta.duration,
        entryDate = System.currentTimeMillis(),
        isPlaying = false
    )
}


*/
