package com.play.maxler.domain.use_case.history

import com.play.maxler.data.local.database.toHistoryEntity
import com.play.maxler.domain.models.Song
import com.play.maxler.domain.repository.HistoryRepository
import javax.inject.Inject

class InsertRecentUseCase @Inject constructor(
    private val recentPlayedRepository: HistoryRepository
){
    suspend operator fun invoke(song : Song){
        song.toHistoryEntity(System.currentTimeMillis()).apply {
            recentPlayedRepository.insert(this)
        }
    }
}