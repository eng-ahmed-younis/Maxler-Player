package com.play.maxler.data.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import com.play.maxler.domain.models.Album
import com.play.maxler.domain.models.Artist
import com.play.maxler.domain.models.Song
import com.play.maxler.domain.repository.Repository
import com.play.maxler.domain.repository.SongsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MainRepository @Inject constructor(
    private  var context: Context,
    private var songsRepository: SongsRepository,
) : Repository{

   /* private val songsRepository by lazy {
        SongsRepositoryImpl(context = context!!)
    }*/

    fun getAllSongs(): Flow<List<Song>> = flow {
        songsRepository.songs().let {
            withContext(Dispatchers.Main){
               // Log.i("maxlersong","lolo")
                Log.i("maxlersong",it.first().title.toString())

            }
            emit(it)
        }
    }.flowOn(Dispatchers.IO)

    override fun songsFlow(): Flow<Result<List<Song>>> {
        TODO("Not yet implemented")
    }

    override fun albumsFlow(): Flow<Result<List<Album>>> {
        TODO("Not yet implemented")
    }

    override fun artistsFlow(): Flow<Result<List<Artist>>> {
        TODO("Not yet implemented")
    }

    override fun observableHistorySongs(): LiveData<List<Song>> {
        TODO("Not yet implemented")
    }

    override fun albumById(albumId: Long): Album {
        TODO("Not yet implemented")
    }

    override suspend fun fetchAlbums(): List<Album> {
        TODO("Not yet implemented")
    }

    override suspend fun albumByIdAsync(albumId: Long): Album {
        TODO("Not yet implemented")
    }

    override suspend fun allSongs(): List<Song> {
        TODO("Not yet implemented")
    }

    override suspend fun fetchArtists(): List<Artist> {
        TODO("Not yet implemented")
    }

    override suspend fun albumArtists(): List<Artist> {
        TODO("Not yet implemented")
    }


}