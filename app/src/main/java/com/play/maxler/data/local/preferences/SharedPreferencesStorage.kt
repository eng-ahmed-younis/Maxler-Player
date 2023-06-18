package com.play.maxler.data.local.preferences

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.play.maxler.R
import com.play.maxler.common.data.Constants
import com.play.maxler.common.data.Constants.ALBUM_DETAIL_SONG_SORT_ORDER
import com.play.maxler.common.data.Constants.ALBUM_SONG_SORT_ORDER
import com.play.maxler.common.data.Constants.ALBUM_SORT_ORDER
import com.play.maxler.common.data.Constants.SONG_SORT_ORDER
import com.play.maxler.common.utils.SortOrder
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SharedPreferencesStorage @Inject constructor(@ApplicationContext val context: Context) : Storage {

    private var _sharedPreferences =
        context.getSharedPreferences(context.getString(R.string.app_name),Context.MODE_PRIVATE)
    override val sharedPreferences:SharedPreferences
    get() = _sharedPreferences!!


    // SONG ORDER
    override var songsSortOrder : String?
        get() =  _sharedPreferences?.getString(
            SONG_SORT_ORDER,
            SortOrder.SongSortOrder.SONG_A_Z
        )
        set(value) = _sharedPreferences!!.edit {
            this.putString(SONG_SORT_ORDER, value)
        }


    // ALBUM ORDER
    override var albumSortOrder: String?
        get() = _sharedPreferences.getString(
            ALBUM_SORT_ORDER,
            SortOrder.AlbumSortOrder.ALBUM_A_Z
        )
        set(value) = sharedPreferences.edit {
            putString(ALBUM_SORT_ORDER, value)
        }


   override val albumSongSortOrder : String?
        get() = sharedPreferences.getString(
            ALBUM_SONG_SORT_ORDER,
            SortOrder.AlbumSongSortOrder.SONG_TRACK_LIST
        )



    override var albumDetailSongSortOrder : String?
        get() = sharedPreferences.getString(
            ALBUM_DETAIL_SONG_SORT_ORDER,
            SortOrder.AlbumSongSortOrder.SONG_TRACK_LIST
        )
        set(value) = sharedPreferences.edit { putString(ALBUM_DETAIL_SONG_SORT_ORDER, value) }


    override var sortOrderItemSelected: String?
        get() = sharedPreferences.getString(
            Constants.SORT_ORDER_ITEM_SELECTED,
            ""
        )
        set(value) {
            sharedPreferences.edit{putString(Constants.SORT_ORDER_ITEM_SELECTED,value)}
        }


/*

    var albumArtistsOnly
        get() = sharedPreferences.getBoolean(
            ALBUM_ARTISTS_ONLY,
            false
        )
        set(value) = sharedPreferences.edit { putBoolean(ALBUM_ARTISTS_ONLY, value) }
*/




}