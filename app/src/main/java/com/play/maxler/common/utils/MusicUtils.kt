package com.play.maxler.common.utils

import android.text.TextUtils
import com.play.maxler.domain.models.Artist
import java.util.*

object MusicUtils {


    fun isArtistNameUnknown(artistName: String?): Boolean {
        if (TextUtils.isEmpty(artistName)) {
            return false
        }
        if (artistName == Artist.UNKNOWN_ARTIST_DISPLAY_NAME) {
            return true
        }
        val tempName = artistName!!.trim { it <= ' ' }.lowercase()
        return tempName == "unknown" || tempName == "<unknown>"
    }

    fun isVariousArtists(artistName: String?): Boolean {
        if (TextUtils.isEmpty(artistName)) {
            return false
        }
        if (artistName == Artist.VARIOUS_ARTISTS_DISPLAY_NAME) {
            return true
        }
        return false
    }
}