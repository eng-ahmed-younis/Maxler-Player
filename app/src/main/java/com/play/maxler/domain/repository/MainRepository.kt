package com.play.maxler.domain.repository

import androidx.lifecycle.LiveData
import com.play.maxler.domain.entities.HistoryEntity
import com.play.maxler.domain.entities.SongEntity
import com.play.maxler.domain.models.Album
import com.play.maxler.domain.models.Artist
import com.play.maxler.domain.models.Playlist
import com.play.maxler.domain.models.Song

interface MainRepository {


    /*songs */
    suspend fun songs(): List<Song>
    suspend fun songById(songId: Long): Song

    /* albums */
    suspend fun albums(): List<Album>
    suspend fun albumById(albumId: Long): Album
    suspend fun fetchAlbums(): List<Album>
    suspend fun albumArtists(): List<Artist>




    suspend fun artists(): List<Artist>
    suspend fun playlists(): List<Playlist>
 /*
    fun genresFlow(): Flow<Result<List<Genre>>>
    fun historySong(): List<HistoryEntity>
    fun favorites(): LiveData<List<SongEntity>>*/
   // fun observableHistorySongs(): LiveData<List<Song>>
   fun playlistSongs(playListId: Long): List<SongEntity>
    suspend fun albumByIdAsync(albumId: Long): Album
    suspend fun fetchArtists(): List<Artist>
    //suspend fun fetchLegacyPlaylist(): List<Playlist>

    /* history played */

    val recentlyPlayed: LiveData<List<HistoryEntity>>
    suspend fun insert(historyEntity: HistoryEntity)
    suspend fun trim()
    suspend fun fetchFirst(): HistoryEntity?






  /*  suspend fun fetchGenres(): List<Genre>
    suspend fun search(query: String?): MutableList<Any>
    suspend fun getPlaylistSongs(playlist: Playlist): List<Song>
    suspend fun getGenre(genreId: Long): List<Song>
    suspend fun artistInfo(name: String, lang: String?, cache: String?): Result<LastFmArtist>
    suspend fun albumInfo(artist: String, album: String): Result<LastFmAlbum>
    suspend fun artistById(artistId: Long): Artist
    suspend fun recentArtists(): List<Artist>
    suspend fun topArtists(): List<Artist>
    suspend fun topAlbums(): List<Album>
    suspend fun recentAlbums(): List<Album>
    suspend fun recentArtistsHome(): Home
    suspend fun topArtistsHome(): Home
    suspend fun topAlbumsHome(): Home
    suspend fun recentAlbumsHome(): Home
    suspend fun favoritePlaylistHome(): Home
    suspend fun suggestionsHome(): Home
    suspend fun genresHome(): Home
    suspend fun playlists(): Home
    suspend fun homeSections(): List<Home>
    suspend fun homeSectionsFlow(): Flow<Result<List<Home>>>
    suspend fun playlist(playlistId: Long): Playlist
    suspend fun fetchPlaylistWithSongs(): List<PlaylistWithSongs>
    suspend fun playlistSongs(playlistWithSongs: PlaylistWithSongs): List<Song>
    suspend fun insertSongs(songs: List<SongEntity>)
    suspend fun checkPlaylistExists(playlistName: String): List<PlaylistEntity>
    suspend fun createPlaylist(playlistEntity: PlaylistEntity): Long
    suspend fun fetchPlaylists(): List<PlaylistEntity>
    suspend fun deleteRoomPlaylist(playlists: List<PlaylistEntity>)
    suspend fun renameRoomPlaylist(playlistId: Long, name: String)
    suspend fun deleteSongsInPlaylist(songs: List<SongEntity>)
    suspend fun removeSongFromPlaylist(songEntity: SongEntity)
    suspend fun deletePlaylistSongs(playlists: List<PlaylistEntity>)
    suspend fun favoritePlaylist(): PlaylistEntity
    suspend fun isFavoriteSong(songEntity: SongEntity): List<SongEntity>
    suspend fun addSongToHistory(currentSong: Song)
    suspend fun songPresentInHistory(currentSong: Song): HistoryEntity?
    suspend fun updateHistorySong(currentSong: Song)
    suspend fun favoritePlaylistSongs(): List<SongEntity>
    suspend fun recentSongs(): List<Song>
    suspend fun topPlayedSongs(): List<Song>
    suspend fun insertSongInPlayCount(playCountEntity: PlayCountEntity)
    suspend fun updateSongInPlayCount(playCountEntity: PlayCountEntity)
    suspend fun deleteSongInPlayCount(playCountEntity: PlayCountEntity)
    suspend fun checkSongExistInPlayCount(songId: Long): List<PlayCountEntity>
    suspend fun playCountSongs(): List<PlayCountEntity>
    suspend fun blackListPaths(): List<BlackListStoreEntity>
    suspend fun deleteSongs(songs: List<Song>)
    suspend fun contributor(): List<Contributor>
    suspend fun searchArtists(query: String): List<Artist>
    suspend fun searchSongs(query: String): List<Song>
    suspend fun searchAlbums(query: String): List<Album>*/

}