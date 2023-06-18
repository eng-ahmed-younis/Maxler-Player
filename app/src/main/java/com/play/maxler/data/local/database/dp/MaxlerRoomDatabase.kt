package com.play.maxler.data.local.database.dp

import androidx.room.Database
import androidx.room.RoomDatabase
import com.play.maxler.data.local.database.dao.HistoryDao
import com.play.maxler.domain.entities.PlaylistEntity
import com.play.maxler.domain.entities.HistoryEntity
import com.play.maxler.domain.entities.SongEntity

@Database(
    entities = [
        HistoryEntity::class,
        PlaylistEntity::class,
        SongEntity::class,
    ],
    version = 1,
    exportSchema = false
)
abstract class MaxlerRoomDatabase : RoomDatabase() {
    abstract val historyDao: HistoryDao

    companion object{
        const val RECENT_DATABASE_NAME = "maxler_player_dp"
    }
}