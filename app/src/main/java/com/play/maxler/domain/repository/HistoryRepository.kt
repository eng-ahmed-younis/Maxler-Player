package com.play.maxler.domain.repository

import androidx.lifecycle.LiveData
import com.play.maxler.domain.entities.HistoryEntity

interface HistoryRepository {
    val historyPlayed: LiveData<List<HistoryEntity>>

    suspend fun insert(historyEntity: HistoryEntity)

    suspend fun trim()

    suspend fun fetchFirst(): HistoryEntity?
}