package com.play.maxler.domain.repository

import android.database.Cursor
import com.play.maxler.domain.models.Song

abstract class SongsRepository {
    abstract suspend fun songs(): List<Song>

    abstract suspend fun songs(cursor: Cursor?): List<Song>

    abstract suspend fun songs(query: String): List<Song>

    //fun songsByFilePath(filePath: String): List<Song>

    abstract suspend fun song(cursor: Cursor?): Song

    abstract suspend fun song(songId: Long): Song
}