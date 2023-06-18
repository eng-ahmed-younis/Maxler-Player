package com.play.maxler.domain.repository

import android.database.Cursor
import com.play.maxler.domain.models.Song

 interface SongsRepository {
    suspend fun songs(): List<Song>

    suspend fun songs(cursor: Cursor?): List<Song>

    suspend fun songs(query: String): List<Song>

    //fun songsByFilePath(filePath: String): List<Song>

    suspend fun song(cursor: Cursor?): Song

    suspend fun song(songId: Long): Song

    fun makeSongsCursor(selection:String?,selectionArgs:Array<String>?,sortOrder: String?  = "") : Cursor?
}
