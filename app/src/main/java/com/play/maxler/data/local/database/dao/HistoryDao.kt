package com.play.maxler.data.local.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.play.maxler.common.data.Constants
import com.play.maxler.domain.entities.HistoryEntity


@Dao
interface HistoryDao {

    @Query("SELECT * FROM  history_table ORDER BY time_played DESC")
    fun fetchAll(): LiveData<List<HistoryEntity>>

    @Query("SELECT * FROM history_table ORDER BY time_played DESC LIMIT 1")
    suspend fun fetchFirst(): HistoryEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(historyEntity: HistoryEntity)

    /**
     * We want to keep a maximum of [Constants.MAX_RECENTLY_PLAYED] items in this database 50 items
     *
     * This will delete the rows whose id is greater than [Constants.MAX_RECENTLY_PLAYED]
     */
    @Query("DELETE FROM history_table where id NOT IN (SELECT id from history_table ORDER BY time_played DESC LIMIT ${Constants.MAX_RECENTLY_PLAYED})")
    suspend fun trim()

}