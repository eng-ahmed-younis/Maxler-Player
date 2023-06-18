package com.play.maxler.domain.use_case.history

import androidx.lifecycle.LiveData
import com.play.maxler.data.local.database.toSong
import com.play.maxler.domain.models.Song
import com.play.maxler.domain.repository.HistoryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FetchFirstUseCase @Inject constructor(
    private val historyRepository: HistoryRepository
){
    suspend operator fun invoke() : Song {
        return withContext(Dispatchers.IO){
            return@withContext historyRepository.fetchFirst()!!.toSong()
        }
    }
}