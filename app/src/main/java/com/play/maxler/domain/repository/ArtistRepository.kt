package com.play.maxler.domain.repository

import com.play.maxler.domain.models.Artist

interface ArtistRepository {

    suspend fun artists(): List<Artist>

    suspend fun albumArtists(): List<Artist>

    suspend fun artists(query: String): List<Artist>

    suspend fun artist(artistId: Long): Artist
}