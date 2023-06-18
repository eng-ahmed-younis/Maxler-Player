package com.play.maxler.domain.repository

import com.play.maxler.domain.models.Album

interface AlbumRepository {
    suspend fun albums(): List<Album>
    suspend fun albums(query: String): List<Album>
    suspend fun album(albumId: Long): Album
}