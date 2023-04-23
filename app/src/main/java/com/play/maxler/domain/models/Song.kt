package com.play.maxler.domain.models

import android.os.Parcelable
import com.play.maxler.presentation.screens.songs.SongUiState
import kotlinx.android.parcel.Parcelize
/* to send data class from destination to other
convert it to Parcelable by add annotation @Parcelize
to implement Parcelable interface.**/

@Parcelize
data class Song(
    val id:Long,
    val title: String?,
    val trackNumber:Int,
    val year:String?,
    val duration:Long,
    val date: String?,
    val dataModified:Long,
    val albumId:Long,
    val albumName: String?,
    val artistId:Long,
    val artistName: String?,
    val composer:String?,
    val albumArtist:String?
): Parcelable



/*
fun Song.toSongUiState() : SongUiState{
    return SongUiState(
        id = id,
        title = title,
        duration = duration,
        artistName = artistName
    )
}*/
