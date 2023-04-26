package com.play.maxler.data.local.preferences

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.play.maxler.R
import com.play.maxler.common.data.Constants
import com.play.maxler.common.data.Constants.SONG_SORT_ORDER
import com.play.maxler.common.utils.SortOrder
import javax.inject.Inject

class SharedPreferencesStorage @Inject constructor(val context: Context) : Storage {

    private var _sharedPreferences = context?.getSharedPreferences(context.getString(R.string.app_name),Context.MODE_PRIVATE)
    val sharedPreferences:SharedPreferences
    get() = _sharedPreferences!!



    override var songsSortOrder: String?
        get() =  _sharedPreferences?.getString(
            Constants.SONG_SORT_ORDER,
            SortOrder.SongSortOrder.SORT_ORDER_A_Z
        )
        set(value) = _sharedPreferences!!.edit {
            this.putString(SONG_SORT_ORDER, value)
        }




}