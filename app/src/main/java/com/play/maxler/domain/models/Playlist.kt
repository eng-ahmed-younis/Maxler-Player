package com.play.maxler.domain.models

import android.content.Context
import android.os.Parcelable
import com.play.maxler.data.repository.PlaylistRepositoryImpl
import com.play.maxler.domain.repository.PlaylistRepository
import kotlinx.coroutines.withContext
import kotlinx.parcelize.IgnoredOnParcel

import kotlinx.parcelize.Parcelize
import javax.inject.Inject

@Parcelize
open class Playlist(
    val id: Long,
    val name: String,
    var songsCount: Int = 0,
    val modified: Long,
) : Parcelable {

    companion object {
        val empty = Playlist(-1, "",0,0L)
    }


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Playlist

        if (id != other.id) return false
        if (name != other.name) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + name.hashCode()
        return result
    }


}