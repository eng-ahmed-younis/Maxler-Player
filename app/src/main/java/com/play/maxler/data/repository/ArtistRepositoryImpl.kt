package com.play.maxler.data.repository

import com.play.maxler.domain.models.Artist
import com.play.maxler.domain.repository.ArtistRepository

class ArtistRepositoryImpl : ArtistRepository {
    override suspend fun artists(): List<Artist> {
        TODO("Not yet implemented")
    }

    override suspend fun artists(query: String): List<Artist> {
        TODO("Not yet implemented")
    }

    override suspend fun albumArtists(): List<Artist> {
        TODO("Not yet implemented")
    }

    override suspend fun artist(artistId: Long): Artist {
        TODO("Not yet implemented")
    }


}