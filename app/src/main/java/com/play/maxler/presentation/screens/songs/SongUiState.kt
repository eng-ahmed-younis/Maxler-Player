package com.play.maxler.presentation.screens.songs

import com.play.maxler.common.data.Constants
import com.play.maxler.domain.models.Song

data class SongUiState(
    val isLoading : Boolean = false,
    val songs: List<Song> = Constants.emptyListSongs,
    val error : String = ""
)