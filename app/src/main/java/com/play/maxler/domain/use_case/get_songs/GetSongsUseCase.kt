package com.play.maxler.domain.use_case.get_songs

import androidx.lifecycle.asLiveData
import com.play.maxler.data.repository.MainRepository
import com.play.maxler.domain.models.Song
import com.play.maxler.common.data.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


class GetSongsUseCase @Inject constructor(
    private val repository: MainRepository
){
    operator fun invoke() : Flow<Result<List<Song>>> = flow {
        try {
            emit(Result.Loading())
            val songs = repository.getAllSongs().asLiveData(Dispatchers.Main)
            emit(Result.Success(songs.value!!))
        }catch (exception:Exception){
            emit(Result.Error(exception.localizedMessage ?: " An Unexpected exception accrued"))
        }
    }
}