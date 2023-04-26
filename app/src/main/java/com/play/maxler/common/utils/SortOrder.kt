package com.play.maxler.common.utils

import android.provider.MediaStore

class SortOrder {

    /** Song sort order entries.*/

    interface SongSortOrder{

        companion object{

            /* song sort order A-Z*/
            const val SORT_ORDER_A_Z: String = MediaStore.Audio.Media.DEFAULT_SORT_ORDER

            /* SONG SORT ORDER Z-A*/
            const val SORT_ORDER_Z_A = "$SORT_ORDER_A_Z DESC"

            /*SONG SORT ORDER ARTIST*/
            const val SORT_ARTIST = MediaStore.Audio.Artists.DEFAULT_SORT_ORDER

            /*SONG SORT ORDER ALBUM*/
            const val SORT_ALBUM = MediaStore.Audio.Albums.DEFAULT_SORT_ORDER

            /*SORT ORDER YEAR*/
            const val SORT_YEAR = "${MediaStore.Audio.Media.YEAR} DESC"

            /*SORt ORDER DURATION*/
            const val SORT_ORDER = "${MediaStore.Audio.Media.DURATION} DESC"

            /* Song sort order date */
            const val SONG_DATE = "${MediaStore.Audio.Media.DATE_ADDED} DESC"

            /* Song sort modified date */
            const val SONG_DATE_MODIFIED = MediaStore.Audio.Media.DATE_MODIFIED + " DESC"

            /* Song sort order composer*/
            const val COMPOSER = MediaStore.Audio.Media.COMPOSER
        }
    }
}