package com.play.maxler.data.local.preferences

import android.content.SharedPreferences

/*OCP principal*/
interface Storage {

     var songsSortOrder : String?
     val sharedPreferences : SharedPreferences
     val albumSongSortOrder : String?
     var albumSortOrder : String?
     var albumDetailSongSortOrder : String?
     var sortOrderItemSelected : String?

}