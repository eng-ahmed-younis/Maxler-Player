package com.play.maxler.data.repository

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.database.getStringOrNull
import com.play.maxler.common.data.Constants
import com.play.maxler.common.data.Constants.projection
import com.play.maxler.common.utils.Utils
import com.play.maxler.data.local.preferences.SharedPreferencesStorage
import com.play.maxler.data.local.preferences.Storage
import com.play.maxler.domain.models.Song
import com.play.maxler.domain.repository.SongsRepository
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

/* storage thar provided by storage module dagger provide
   SharedPreferencesStorage when ask for Stooge interface
    */
class SongsRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val storage: Storage
    ) : SongsRepository{


    /* return all songs of device without any query
    * -> return cursor without any query and use songs fun
    *   to get songs from cursor
    * */
    @RequiresApi(Build.VERSION_CODES.Q)
    override suspend fun songs(): List<Song> {
        return songs(makeSongsCursor(
            null,
            null,
            storage.songsSortOrder
        ))
    }

    /* return list of songs from cursor */
    @RequiresApi(Build.VERSION_CODES.Q)
    override suspend fun songs(cursor: Cursor?): List<Song> {
        val songs = mutableListOf<Song>()
        if (cursor != null && cursor.moveToFirst()){
            do {
                songs.add(getSongFromCursorImpl(cursor))
            }while (cursor.moveToNext())
        }
        cursor?.close()
        return songs
    }

    /* return single song
    * -> don`t make loop to get all songs in cursor just first
    * */
    @RequiresApi(Build.VERSION_CODES.Q)
    override suspend fun song(cursor: Cursor?): Song {
        val song = if (cursor != null && cursor.moveToFirst()){
            getSongFromCursorImpl(cursor)
        }else{
            Song(1,
                "tito",
                trackNumber = -1,
                year = "",
                duration = -1,
                data = "",
                dateTaken = "",
                dateModified = -1,
                albumId = -1,
                albumName = "",
                artistId = -1,
                artistName = "",
                composer = "",
                albumArtist = "",
                count = -1,
                songNumber = -1
            )
        }
        cursor?.close()
        return song
    }


    /* return list of songs  by search of title */
    @RequiresApi(Build.VERSION_CODES.Q)
    override suspend fun songs(query: String): List<Song> {
        val selection = "${MediaStore.Audio.AudioColumns.TITLE} LIKE ?"
        return songs(
            makeSongsCursor(selection = selection, arrayOf("%$query%"))
        )
    }




/*
     return single song by search using id
*/
    @RequiresApi(Build.VERSION_CODES.Q)
    override suspend fun song(songId: Long): Song {
        val selection = "${MediaStore.Audio.AudioColumns._ID} =?"
        return song(
            makeSongsCursor(selection = selection, arrayOf("%$songId%"))
        )
    }

/*    @RequiresApi(Build.VERSION_CODES.Q)
    override suspend fun song(songId: Long): Song {
        return song(makeSongsCursor(MediaStore.Audio.Media._ID
                + "=?", arrayOf(songId.toString())))
    }*/




    @SuppressLint("Recycle")
    override fun makeSongsCursor(
        selection:String?,
        selectionArgs:Array<String>?,
        sortOrder: String?
    ):Cursor?{

        val selectionFinal = Constants.IS_MUSIC

      /*  var selectionFinal: String? = selection
        selectionFinal = if (selection!!.isNotEmpty() && selection.trim() != ""){
               "${Constants.IS_MUSIC} AND $selectionFinal"
        }else{
            Constants.IS_MUSIC
        }*/



        val uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
            MediaStore.Audio.Media.getContentUri(
                MediaStore.VOLUME_EXTERNAL
            )
        }else{
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        }

        return try {
            //29
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                context.contentResolver?.query(
                    uri,
                    projection,
                    selectionFinal,
                    selectionArgs,
                    sortOrder ?: storage.songsSortOrder
                )
            } else {
                null
            }

        } catch (ex: SecurityException) {
            Log.i("error_songs",ex.message.toString())
            return null
        }

    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun getSongFromCursorImpl(cursor: Cursor): Song {

        val id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns._ID))
        val title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.TITLE))
        val trackNumber = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.TRACK))
        val year =  cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.YEAR))
        //******************
        val duration = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.DURATION))
        val data = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.DATA))
        val dateTaken = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.DATE_TAKEN))
        val dateModified = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.DATE_MODIFIED))
        val albumId = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.ALBUM_ID))
        val albumName = cursor.getStringOrNull(cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.ALBUM))
        val artistId = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.ARTIST_ID))
        val artistName = cursor.getStringOrNull(cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.ARTIST))
        val composer = cursor.getStringOrNull(cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.COMPOSER))
        val albumArtist = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            cursor.getStringOrNull(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ARTIST))
        } else {
            null
        }
        val count = cursor.count.toLong()
        val songNumber: Long =
            Utils.getTrackNumber(cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Playlists.Members.TRACK))).toLongOrNull()
                ?: 0


        return Song(
            id = id,
            title = title,
            trackNumber = trackNumber,
            year = year,
            duration = duration,
            data = data,
            dateTaken = dateTaken,
            dateModified = dateModified,
            albumId = albumId,
            albumName = albumName ?: "",
            artistId = artistId,
            artistName = artistName ?: "",
            composer = composer ?: "",
            albumArtist = albumArtist ?: "",
            count = count,
            songNumber = songNumber
        )
    }


}