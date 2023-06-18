package com.play.maxler.domain.use_case.albums

import com.play.maxler.common.data.Resource
import com.play.maxler.data.repository.MainRepositoryImpl
import com.play.maxler.domain.models.Album
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetAlbumsUseCase @Inject constructor(
    private val repository: MainRepositoryImpl
) {

    operator fun invoke() : Flow<Resource<List<Album>>> = flow {
        emit(Resource.Loading)
        try {
            val allAlbums: List<Album> = repository.albums()
            emit(Resource.Success(allAlbums))
        }catch (exception : Exception){
            emit(Resource.Error(exception))
        }
    }.flowOn(Dispatchers.IO)


}