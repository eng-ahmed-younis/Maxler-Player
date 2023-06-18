package com.play.maxler.data.repository

import android.provider.MediaStore
import com.play.maxler.common.utils.SortOrder
import com.play.maxler.data.local.preferences.Storage
import com.play.maxler.domain.models.Album
import com.play.maxler.domain.models.Song
import com.play.maxler.domain.repository.AlbumRepository
import com.play.maxler.domain.repository.SongsRepository
import javax.inject.Inject


class AlbumRepositoryImpl @Inject constructor(
    private val songsRepository: SongsRepository,
    private val storage: Storage
) : AlbumRepository{
    override suspend fun albums(): List<Album> {
        val songs = songsRepository.songs(
            songsRepository.makeSongsCursor(
                null,
                null,
                getSongLoaderSortOrder()
            )
        )
        return splitIntoAlbums(songs)
    }


    override suspend fun albums(query: String): List<Album> {
        val songs = songsRepository.songs(
            songsRepository.makeSongsCursor(
                MediaStore.Audio.AudioColumns.ALBUM + " LIKE ?",
                arrayOf("%$query%"),
                getSongLoaderSortOrder()
            )
        )
        return splitIntoAlbums(songs)
    }

    override suspend fun album(albumId: Long): Album {
        val cursor = songsRepository.makeSongsCursor(
            MediaStore.Audio.AudioColumns.ALBUM_ID + "=?",
            arrayOf(albumId.toString()),
            getSongLoaderSortOrder()
        )
        val songs = songsRepository.songs(cursor)
        val album = Album(albumId, songs)
        sortAlbumSongs(album)
        return album
    }

    /* this function take all songs and split songs depend on albums
    * 1- group songs that the same album id
    * 2-
    *  */
    private fun splitIntoAlbums(
        songs: List<Song>
    ): List<Album> {
        return songs.groupBy {
            it.albumId
        }.map {
            // it.key = id long
            // it.value = list<song>
            //  Album data class take (id,list<song>)
            sortAlbumSongs(Album(it.key, it.value))
        }
    }



    private fun sortAlbumSongs(album: Album): Album {
        val songs = when (storage.albumDetailSongSortOrder) {
            SortOrder.AlbumSongSortOrder.SONG_TRACK_LIST ->
                album.songs.sortedWith { o1, o2 ->
                    o1.trackNumber.compareTo(o2.trackNumber)
                }
            SortOrder.AlbumSongSortOrder.SONG_A_Z -> album.songs.sortedWith { o1, o2 ->
                o1.title!!.compareTo(o2.title!!)
            }
            SortOrder.AlbumSongSortOrder.SONG_Z_A -> album.songs.sortedWith { o1, o2 ->
                o2.title!!.compareTo(o1.title!!)
            }
            SortOrder.AlbumSongSortOrder.SONG_DURATION -> album.songs.sortedWith { o1, o2 ->
                o1.duration.compareTo(o2.duration)
            }
            else -> throw IllegalArgumentException("invalid ${storage.albumDetailSongSortOrder}")
        }
        return album.copy(songs = songs)
    }


    private fun getSongLoaderSortOrder(): String {
        return storage.albumSortOrder + ", " +
                storage.albumSongSortOrder
    }

}