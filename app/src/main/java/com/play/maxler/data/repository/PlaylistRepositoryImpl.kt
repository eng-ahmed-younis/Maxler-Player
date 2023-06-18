package com.play.maxler.data.repository

import android.content.Context
import android.database.Cursor
import android.os.Build
import android.provider.BaseColumns
import android.provider.CalendarContract.Attendees.query
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import androidx.core.database.getStringOrNull
import com.play.maxler.common.data.Constants
import com.play.maxler.domain.models.Playlist
import com.play.maxler.domain.models.PlaylistSong
import com.play.maxler.domain.models.Song
import com.play.maxler.domain.repository.PlaylistRepository
import dagger.hilt.android.qualifiers.ApplicationContext


import javax.inject.Inject

class PlaylistRepositoryImpl @Inject constructor(
    @ApplicationContext val context: Context
) : PlaylistRepository {

    override suspend fun playlist(cursor: Cursor?): Playlist {
        return cursor.use {
            if (cursor?.moveToFirst() == true) {
                getPlaylistFromCursorImpl(cursor)
            } else {
                Playlist.empty
            }
        }
    }


    override suspend fun playlist(playlistName: String): Playlist {
        val selection: String = MediaStore.Audio.PlaylistsColumns.NAME + "=?"
        val selectionArgs = arrayOf(playlistName)
        return playlist(makePlaylistCursor(selection = selection, selectionArgs ))
    }

    override suspend fun playlist(playlistId: Long): Playlist {
        return playlist(
            makePlaylistCursor(
                BaseColumns._ID + "=?",
                arrayOf(playlistId.toString())
            )
        )
    }



    /* search in Playlist by name */
    override suspend fun searchPlaylist(query: String): List<Playlist> {
        return playlists(makePlaylistCursor(MediaStore.Audio.PlaylistsColumns.NAME + "=?", arrayOf(query)))
    }

    /* get all playlists */
    override suspend fun playlists(): List<Playlist> {
        return playlists(makePlaylistCursor(null, null))
    }


    @RequiresApi(Build.VERSION_CODES.R)
    override suspend fun playlistSongs(playlistId: Long): MutableList<Song> {
        val songs = mutableListOf<Song>()
        val cursor = makePlaylistSongCursor(playlistId)

        if (cursor != null && cursor.moveToFirst()) {
            do {
                songs.add(getPlaylistSongFromCursorImpl(cursor, playlistId))
            } while (cursor.moveToNext())
        }
        cursor?.close()
        return songs
    }

    override suspend fun playlists(cursor: Cursor?): List<Playlist> {
        val playlists = mutableListOf<Playlist>()
        if (cursor != null && cursor.moveToFirst()) {
            do {
                playlists.add(getPlaylistFromCursorImpl(cursor))
            } while (cursor.moveToNext())
        }
        cursor?.close()
        return playlists
    }

    override suspend fun favoritePlaylist(playlistName: String): List<Playlist> {
        return playlists(
            makePlaylistCursor(
                MediaStore.Audio.PlaylistsColumns.NAME + "=?",
                arrayOf(playlistName)
            )
        )
    }

    override suspend fun deletePlaylist(playlistId: Long) {
        val localUri = MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI
        val localStringBuilder = StringBuilder()
        localStringBuilder.append("_id IN (")
        localStringBuilder.append(playlistId)
        localStringBuilder.append(")")
        context.contentResolver.delete(localUri, localStringBuilder.toString(), null)
    }




    private fun getPlaylistFromCursorImpl(
        cursor: Cursor
    ): Playlist {
        val  id: Long = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns._ID))
        val  name = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Playlists.NAME))
        val  modified: Long = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATE_MODIFIED))
        return Playlist(id, name ,fetchSongCount(id), modified)
    }


    fun fetchSongCount(playlistId: Long): Int {
        val cursor: Cursor? = makePlaylistCursor(
            BaseColumns._ID + "=?",
            arrayOf(playlistId.toString())
        )
        return cursor?.count ?: 0
    }

    @RequiresApi(Build.VERSION_CODES.R)
    private fun getPlaylistSongFromCursorImpl(cursor: Cursor, playlistId: Long): PlaylistSong {
        val id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Playlists.Members.AUDIO_ID))
        val title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.TITLE))
        val trackNumber = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.TRACK))
        val year = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.YEAR))
        val duration = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.DURATION))
        val data = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.DATA))
        val dateModified = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.DATE_MODIFIED))
        val albumId = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.ALBUM_ID))
        val albumName = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.ALBUM))
        val artistId = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.ARTIST_ID))
        val artistName = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.ARTIST))
        val idInPlaylist = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Playlists.Members._ID))
        val composer = cursor.getStringOrNull(cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.COMPOSER))
        val albumArtist = cursor.getStringOrNull(cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.ALBUM_ARTIST))
        return PlaylistSong(
            id,
            title,
            trackNumber,
            year.toString(),
            duration,
            data,
            dateModified,
            albumId,
            albumName,
            artistId,
            artistName,
            playlistId,
            idInPlaylist,
            composer ?: "",
            albumArtist
        )
    }


    private fun makePlaylistCursor(
        selection: String?,
        values: Array<String>?
    ): Cursor? {
        return context.contentResolver.query(
            /* uri = */ MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI,
            /* projection = */ arrayOf(
                BaseColumns._ID, /* 0 */
                MediaStore.Audio.PlaylistsColumns.NAME /* 1 */
            ),
            /* selection = */ selection,
            /* selectionArgs = */ values,
            /* sortOrder = */ MediaStore.Audio.Playlists.DEFAULT_SORT_ORDER
        )
    }




    private fun makePlaylistSongCursor(playlistId: Long): Cursor? {
        return context.contentResolver.query(
            MediaStore.Audio.Playlists.Members.getContentUri("external", playlistId),
            arrayOf(
                MediaStore.Audio.Playlists.Members.AUDIO_ID, // 0
                MediaStore.Audio.AudioColumns.TITLE, // 1
                MediaStore.Audio.AudioColumns.TRACK, // 2
                MediaStore.Audio.AudioColumns.YEAR, // 3
                MediaStore.Audio.AudioColumns.DURATION, // 4
                MediaStore.Audio.AudioColumns.DATA, // 5
                MediaStore.Audio.AudioColumns.DATE_MODIFIED, // 6
                MediaStore.Audio.AudioColumns.ALBUM_ID, // 7
                MediaStore.Audio.AudioColumns.ALBUM, // 8
                MediaStore.Audio.AudioColumns.ARTIST_ID, // 9
                MediaStore.Audio.AudioColumns.ARTIST, // 10
                MediaStore.Audio.Playlists.Members._ID,//11
                MediaStore.Audio.AudioColumns.COMPOSER,//12
                "album_artist"//13
            ), Constants.IS_MUSIC, null, MediaStore.Audio.Playlists.Members.DEFAULT_SORT_ORDER
        )
    }

}