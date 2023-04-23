package com.play.maxler.data.repository

import android.content.Context
import android.util.Log
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

class MainRepository constructor(private val context: Context) : Repository{

    private val songsRepository by lazy {
        SongsRepositoryImpl(context = context)
    }

    fun getAllSongs(): Flow<List<Song>> = flow {
        songsRepository.songs().let {
            withContext(Dispatchers.Main){
               // Log.i("maxlersong","lolo")
                Log.i("maxlersong",it.first().title.toString())

            }
            emit(it)
        }
    }.flowOn(Dispatchers.IO)





}