
package com.play.maxler.data.local.database

import com.play.maxler.domain.entities.HistoryEntity
import com.play.maxler.domain.entities.PlayCountEntity
import com.play.maxler.domain.entities.PlaylistEntity
import com.play.maxler.domain.entities.SongEntity
import com.play.maxler.domain.models.Song

fun List<HistoryEntity>.fromHistoryToSongs(): List<Song> {
    return map {
        it.toSong()
    }
}

fun List<SongEntity>.toSongs(): List<Song> {
    return map {
        it.toSong()
    }
}

fun List<Song>.toSongs(playlistId: Long): List<SongEntity> {
    return map {
        it.toSongEntity(playlistId)
    }
}

fun Song.toHistoryEntity(timePlayed: Long): HistoryEntity {
    return HistoryEntity(
        id = id,
        title = title!!,
        trackNumber = trackNumber,
        year = year!!.toInt(),
        duration = duration,
        data = data!!,
        dateTaken = dateTaken!!,
        dateModified = dateModified,
        albumId = albumId,
        albumName = albumName!!,
        artistId = artistId,
        artistName = artistName!!,
        composer = composer,
        albumArtist = albumArtist,
        timePlayed = timePlayed
    )
}

fun Song.toSongEntity(playListId: Long): SongEntity {
    return SongEntity(
        playlistCreatorId = playListId,
        id = id,
        title = title!!,
        trackNumber = trackNumber,
        year = year!!.toInt(),
        duration = duration,
        data = data!!,
        dateModified = dateModified,
        albumId = albumId,
        albumName = albumName!!,
        artistId = artistId,
        artistName = artistName!!,
        composer = composer,
        albumArtist = albumArtist
    )
}

fun SongEntity.toSong(): Song {
    return Song(
        id = id,
        title = title,
        trackNumber = trackNumber,
        year = year.toString(),
        duration = duration,
        data = data,
        dateModified = dateModified,
        albumId = albumId,
        albumName = albumName,
        artistId = artistId,
        artistName = artistName,
        composer = composer,
        albumArtist = albumArtist,
        count = 0L,
        dateTaken = "",
        songNumber = 0L
    )
}

fun PlayCountEntity.toSong(): Song {
    return Song(
        id = id,
        title = title,
        trackNumber = trackNumber,
        year = year.toString(),
        duration = duration,
        data = data,
        dateModified = dateModified,
        albumId = albumId,
        albumName = albumName,
        artistId = artistId,
        artistName = artistName,
        composer = composer,
        albumArtist = albumArtist,
        count = 0L,
        dateTaken = "",
        songNumber = 0L
    )
}

fun HistoryEntity.toSong(): Song {
    return Song(
        id = id,
        title = title,
        trackNumber = trackNumber,
        year = year.toString(),
        duration = duration,
        data = data,
        dateModified = dateModified,
        albumId = albumId,
        albumName = albumName,
        artistId = artistId,
        artistName = artistName,
        composer = composer,
        albumArtist = albumArtist,
        count = 0L ,
        dateTaken = dateTaken,
        songNumber = 0L
    )
}





fun Song.toPlayCount(): PlayCountEntity {
    return PlayCountEntity(
        id = id,
        title = title!!,
        trackNumber = trackNumber,
        year = year!!.toInt(),
        duration = duration,
        data = data!!,
        dateModified = dateModified,
        albumId = albumId,
        albumName = albumName!!,
        artistId = artistId,
        artistName = artistName!!,
        composer = composer,
        albumArtist = albumArtist,
        timePlayed = System.currentTimeMillis(),
        playCount = 1
    )
}

fun List<Song>.toSongsEntity(playlistEntity: PlaylistEntity): List<SongEntity> {
    return map {
        it.toSongEntity(playlistEntity.playListId)
    }
}
