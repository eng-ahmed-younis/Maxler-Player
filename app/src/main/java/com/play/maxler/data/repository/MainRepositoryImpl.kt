package com.play.maxler.data.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import com.play.maxler.domain.entities.HistoryEntity
import com.play.maxler.domain.entities.SongEntity
import com.play.maxler.domain.models.Album
import com.play.maxler.domain.models.Artist
import com.play.maxler.domain.models.Playlist
import com.play.maxler.domain.models.Song
import com.play.maxler.domain.repository.*
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MainRepositoryImpl @Inject constructor(
    @ApplicationContext private  var context: Context,
    private var songsRepository: SongsRepository,
    private var albumRepository: AlbumRepository,
    private val playlistRepository: PlaylistRepository,
    private val historyRepository: HistoryRepository
) : MainRepository {

    /* songs */
    override suspend fun songs(): List<Song>  = songsRepository.songs()
    override suspend fun songById(songId: Long): Song  = songsRepository.song(songId)
    override fun playlistSongs(playListId: Long): List<SongEntity> {
        TODO("Not yet implemented")
    }


    override suspend fun artists(): List<Artist> {
        TODO("Not yet implemented")
    }

    /* playlists */
    override suspend fun playlists(): List<Playlist>  = playlistRepository.playlists()


    /* album */
    override suspend fun albums(): List<Album>  = albumRepository.albums()
    override suspend fun albumById(albumId: Long): Album  = albumRepository.album(albumId)


    override suspend fun fetchAlbums(): List<Album> {
        TODO("Not yet implemented")
    }

    override suspend fun albumByIdAsync(albumId: Long): Album {
        TODO("Not yet implemented")
    }



    override suspend fun fetchArtists(): List<Artist> {
        TODO("Not yet implemented")
    }


    /* history played */
    override val recentlyPlayed: LiveData<List<HistoryEntity>>
        get() = historyRepository.historyPlayed
    override suspend fun insert(historyEntity: HistoryEntity) { historyRepository.insert(historyEntity) }
    override suspend fun trim() { historyRepository.trim() }
    override suspend fun fetchFirst(): HistoryEntity? { return historyRepository.fetchFirst() }






    override suspend fun albumArtists(): List<Artist> {
        TODO("Not yet implemented")
    }


}