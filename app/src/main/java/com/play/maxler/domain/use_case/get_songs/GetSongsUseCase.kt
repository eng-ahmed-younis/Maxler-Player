package com.play.maxler.domain.use_case.get_songs

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.play.maxler.data.repository.MainRepository
import com.play.maxler.domain.models.Song
import com.play.maxler.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import javax.inject.Inject


class GetSongsUseCase @Inject constructor(
    private val repository: MainRepository
){
    operator fun invoke() : Flow<Resource<List<Song>>> = flow {
        try {
            emit(Resource.Loading())
            val songs = repository.getAllSongs().asLiveData(Dispatchers.Main)
            emit(Resource.Success(songs.value!!))
        }catch (exception:Exception){
            emit(Resource.Error(exception.localizedMessage ?: " An Unexpected exception accrued"))
        }
    }
}