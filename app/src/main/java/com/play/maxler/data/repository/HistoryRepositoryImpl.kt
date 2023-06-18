package com.play.maxler.data.repository

import androidx.lifecycle.LiveData
import com.play.maxler.data.local.database.dao.HistoryDao
import com.play.maxler.domain.entities.HistoryEntity
import com.play.maxler.domain.repository.HistoryRepository
import javax.inject.Inject

class HistoryRepositoryImpl @Inject constructor(
    private val historyDto : HistoryDao
): HistoryRepository {

    override val historyPlayed: LiveData<List<HistoryEntity>>
        get() = historyDto.fetchAll()

    override suspend fun insert(historyEntity: HistoryEntity)  =  historyDto.insert(historyEntity = historyEntity)

    override suspend fun trim()  = historyDto.trim()

    override suspend fun fetchFirst(): HistoryEntity? = historyDto.fetchFirst()

}