package com.play.maxler.presentation.screens.songs

import com.play.maxler.domain.models.Song
import com.play.maxler.utils.Constants

data class SongUiState(
    val isLoading : Boolean = false,
    val songs: List<Song> = Constants.emptyListSongs,
    val error : String = ""
)