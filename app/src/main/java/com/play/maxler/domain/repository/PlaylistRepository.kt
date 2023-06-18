package com.play.maxler.domain.repository

import android.database.Cursor
import com.play.maxler.domain.models.Playlist
import com.play.maxler.domain.models.Song

interface PlaylistRepository {

     suspend  fun playlist(cursor: Cursor?): Playlist

     suspend   fun searchPlaylist(query: String): List<Playlist>

     suspend fun playlist(playlistName: String): Playlist

     suspend fun playlists(): List<Playlist>

     suspend fun playlists(cursor: Cursor?): List<Playlist>

     suspend fun favoritePlaylist(playlistName: String): List<Playlist>

     suspend fun deletePlaylist(playlistId: Long)

     suspend fun playlist(playlistId: Long): Playlist

     suspend fun playlistSongs(playlistId: Long): List<Song>
}