package com.play.maxler.common.data

import android.annotation.SuppressLint
import android.os.Build
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import com.play.maxler.R
import com.play.maxler.domain.models.Screen
import com.play.maxler.domain.models.Song
import com.play.maxler.presentation.album.AlbumFragment
import com.play.maxler.presentation.artists.ArtistsFragment
import com.play.maxler.presentation.screens.folder.FoldersFragment
import com.play.maxler.presentation.screens.onBoarding.Board
import com.play.maxler.presentation.screens.songs.SongsFragment

object Constants {


    const val SONG_SORT_ORDER = "song_sort_order"
    const val HAS_SEEN_ON_BOARDING = "maxler.player.onBoarding.hasSeenOnBoarding"
    const val SONGS_ROOT = "_SONGS__"
    const val PLAY_RANDOM = "com.playback.PLAY_RANDOM"
    const val LAST_PARENT_ID = "com.playback.LAST_PARENT_ID"






    const val networkPermission = android.Manifest.permission.ACCESS_NETWORK_STATE
    const val cameraPermission = android.Manifest.permission.CAMERA


    @SuppressLint("InlinedApi")
    @RequiresApi(Build.VERSION_CODES.Q)
    val projection = arrayOf(
        MediaStore.Audio.Media._ID,
        MediaStore.Audio.Media.TITLE,
        MediaStore.Audio.Media.TRACK,
        MediaStore.Audio.Media.YEAR,
        MediaStore.Audio.Media.DURATION,
        MediaStore.Audio.Media.DATE_TAKEN,
        MediaStore.Audio.Media.DATE_MODIFIED,
        MediaStore.Audio.Media.ALBUM_ID,
        MediaStore.Audio.Media.ALBUM,
        MediaStore.Audio.Media.ARTIST_ID,
        MediaStore.Audio.Media.ARTIST,
        MediaStore.Audio.Media.COMPOSER,
        MediaStore.Audio.Media.ALBUM_ARTIST
    )


    const val IS_MUSIC = "${MediaStore.Audio.AudioColumns.IS_MUSIC} =1 AND ${MediaStore.Audio.AudioColumns.TITLE} !=''"


        val emptySong = Song(
            id = -1,
            title = "",
            trackNumber = -1,
            year = "",
            duration = -1,
            date = "",
            dataModified = -1,
            albumId = -1,
            albumName = "",
            artistId = -1,
            artistName = "",
            composer = "",
            albumArtist = ""
        )

    val emptyListSongs: List<Song> = mutableListOf<Song>(
        emptySong
    )


     val boards = mutableListOf<Board>(
        Board(R.drawable.on_boarding1, R.string.board_title_1, R.string.board_description_1),
        Board(R.drawable.on_boarding2, R.string.board_title_2, R.string.board_description_2),
        Board(R.drawable.on_boarding3, R.string.board_title_3, R.string.board_description_3)
    )

    val screens= mutableListOf(
        Screen(R.string.songs, SongsFragment()),
        Screen(R.string.artists, ArtistsFragment()),
        Screen(R.string.albums, AlbumFragment()),
        Screen(R.string.folders, FoldersFragment())
    )

    // val labels  = arrayOf(R.id.1,R.id.board_2,R.id.board_3)

}

const val FILTER_SONG = "filter_song"
