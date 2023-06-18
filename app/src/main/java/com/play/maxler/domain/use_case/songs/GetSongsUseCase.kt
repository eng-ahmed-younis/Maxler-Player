package com.play.maxler.domain.use_case.songs

import com.play.maxler.common.data.Resource
import com.play.maxler.data.repository.MainRepositoryImpl
import com.play.maxler.domain.models.Song
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject


class GetSongsUseCase @Inject constructor(
    private val repository: MainRepositoryImpl
){
    operator fun invoke() : Flow<Resource<List<Song>>>  = flow {
        emit(Resource.Loading)
        try {
            val allSongs: List<Song> = repository.songs()
            emit(Resource.Success(allSongs))
        }catch (exception : Exception){
            emit(Resource.Error(exception))
        }
    }.flowOn(Dispatchers.IO)

}