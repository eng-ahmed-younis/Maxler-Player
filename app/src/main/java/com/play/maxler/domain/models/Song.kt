package com.play.maxler.domain.models

import android.os.Parcelable
import com.play.maxler.presentation.screens.songs.SongUiState
import kotlinx.parcelize.Parcelize

/* to send data class from destination to other
convert it to Parcelable by add annotation @Parcelize
to implement Parcelable interface.**/

@Parcelize
open class Song(
    open val  id:Long,
    open val  title: String?,
    open val  trackNumber:Int,
    open val  year:String?,
    open val  duration:Long,
    open val  data: String?,
    open val  dateTaken: String?,
    open val  dateModified:Long,
    open val  albumId:Long,
    open val  albumName: String?,
    open val  artistId:Long,
    open val  artistName: String?,
    open val  composer:String?,
    open val  albumArtist:String?,
    open val count:Long?,
    open val songNumber : Long?

): Parcelable {


// need to override manually because is open and cannot be a data class
override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as Song

    if (id != other.id) return false
    if (title != other.title) return false
    if (trackNumber != other.trackNumber) return false
    if (year != other.year) return false
    if (duration != other.duration) return false
    if (dateTaken != other.dateTaken) return false
    if (data != other.data) return false
    if (dateModified != other.dateModified) return false
    if (albumId != other.albumId) return false
    if (albumName != other.albumName) return false
    if (artistId != other.artistId) return false
    if (artistName != other.artistName) return false
    if (composer != other.composer) return false
    if (albumArtist != other.albumArtist) return false
    if (count != other.count) return false
    if (songNumber != other.songNumber) return false


    return true
}

override fun hashCode(): Int {
    var result = id.hashCode()
    result = 31 * result + title.hashCode()
    result = 31 * result + trackNumber
    result = 31 * result + year!!.toInt()
    result = 31 * result + duration.hashCode()
    result = 31 * result + data.hashCode()
    result = 31 * result + dateTaken.hashCode()
    result = 31 * result + dateModified.hashCode()
    result = 31 * result + albumId.hashCode()
    result = 31 * result + albumName.hashCode()
    result = 31 * result + artistId.hashCode()
    result = 31 * result + artistName.hashCode()
    result = 31 * result + (composer?.hashCode() ?: 0)
    result = 31 * result + (albumArtist?.hashCode() ?: 0)
    result = 31 * result + (count?.hashCode() ?: 0)
    result = 31 * result + (songNumber?.hashCode() ?: 0)

    return result
}




}

