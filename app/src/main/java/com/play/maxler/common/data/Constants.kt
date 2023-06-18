package com.play.maxler.common.data

import android.Manifest
import android.annotation.SuppressLint
import android.os.Build
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import com.play.maxler.R
import com.play.maxler.domain.models.Screen
import com.play.maxler.domain.models.Song
import com.play.maxler.presentation.screens.album.AlbumFragment
import com.play.maxler.presentation.artists.ArtistsFragment
import com.play.maxler.presentation.screens.folder.FoldersFragment
import com.play.maxler.presentation.screens.onBoarding.Board
import com.play.maxler.presentation.screens.songs.SongsFragment

object Constants {


    const val SONG_SORT_ORDER = "song_sort_order"
    const val HAS_SEEN_ON_BOARDING = "maxler.player.onBoarding.hasSeenOnBoarding"
    const val ALBUM_SORT_ORDER = "ALBUM_SORT_ORDER"
    const val ALBUM_SONG_SORT_ORDER = "ALBUM_SONG_SORT_ORDER"
    const val ALBUM_DETAIL_SONG_SORT_ORDER = "ALBUM_DETAIL_SONG_SORT_ORDER"
    const val PLAY_FIRST = "com.playback.PLAY_FIRST"
    const val PLAY_RANDOM = "com.playback.PLAY_RANDOM"
    const val MY_MEDIA_ROOT_ID = "media_root_id"
    const val MY_EMPTY_MEDIA_ROOT_ID = "empty_root_id"
    const val SORT_ORDER_ITEM_SELECTED = "sortOrderItemSelected"



    // Media playback
    const val MEDIA_SEARCH_SUPPORTED = "android.media.browse.SEARCH_SUPPORTED"
    const val CONTENT_STYLE_BROWSABLE_HINT = "android.media.browse.CONTENT_STYLE_BROWSABLE_HINT"
    const val CONTENT_STYLE_PLAYABLE_HINT = "android.media.browse.CONTENT_STYLE_PLAYABLE_HINT"
    const val CONTENT_STYLE_SUPPORTED = "android.media.browse.CONTENT_STYLE_SUPPORTED"
    const val CONTENT_STYLE_LIST = 1
    const val CONTENT_STYLE_GRID = 2
    const val BROWSABLE_ROOT = "/"
    const val SONGS_ROOT = "_SONGS__"
    const val EMPTY_ROOT = "@empty@"
    const val ALBUMS_ROOT = "__ALBUMS__"
    const val ARTISTS_ROOT = "_ARTISTS__"
    const val NETWORK_FAILURE = "com.playback.NETWORK_FAILURE"
    const val LAST_ID = "com.playback.LAST_ID"
    const val LAST_POSITION = "com.playback.LAST_POSITION"
    const val LAST_PARENT_ID = "com.playback.LAST_PARENT_ID"
    const val LAST_REPEAT_MODE = "com.playback.LAST_REPEAT_MODE"
    const val LAST_SHUFFLE_MODE = "com.playback.LAST_SHUFFLE_MODE"
    const val NETWORK_ERROR = "NETWORK_ERROR"





    const val networkPermission = android.Manifest.permission.ACCESS_NETWORK_STATE
    @RequiresApi(Build.VERSION_CODES.P)
    val permissions = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.FOREGROUND_SERVICE
    )
    const val cameraPermission = android.Manifest.permission.CAMERA



    // music sources
    const val EXTRA_MEDIA_FOCUS = "android.intent.extra.focus"

    @SuppressLint("InlinedApi")
    @RequiresApi(Build.VERSION_CODES.Q)
    val projection = arrayOf(
        MediaStore.Audio.Media._ID,
        MediaStore.Audio.Media.TITLE,
        MediaStore.Audio.Media.TRACK,
        MediaStore.Audio.Media.YEAR,
        MediaStore.Audio.Media.DURATION,
        MediaStore.Audio.Media.DATA,
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
            title = "lolo",
            trackNumber = -1,
            year = "",
            duration = -1,
            dateTaken = " ",
            data = "",
            dateModified = -1,
            albumId = -1,
            albumName = "",
            artistId = -1,
            artistName = "",
            composer = "",
            albumArtist = "",
            count = 0L,
            songNumber = 0L
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


    val WHITESPACE_REGEX = "\\s|\\n".toRegex()
    const val MAX_RECENTLY_PLAYED = 100
    const val PLAYBACK_NOTIFICATION: Int = 0xb2017
    const val NOTIFICATION_LARGE_ICON_SIZE = 144 // px
    const val IMAGE_URI_ROOT = "android.resource://com.play.maxler/drawable/"
    const val POSITION_UPDATE_INTERVAL_MILLIS = 100L




}

const val FILTER_SONG = "filter_song"

