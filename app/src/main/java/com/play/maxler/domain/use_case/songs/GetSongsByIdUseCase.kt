package com.play.maxler.domain.use_case.songs

import com.play.maxler.data.repository.MainRepositoryImpl
import com.play.maxler.domain.models.Song
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetSongsByIdUseCase @Inject constructor(
    private val repository: MainRepositoryImpl
) {
    operator fun invoke(songsId : Long) : Flow<Song> = flow {
        try {
            val song = repository.songById(songsId)
            emit(song)
        }catch (exception : Exception){

        }
    }.flowOn(Dispatchers.IO)


}