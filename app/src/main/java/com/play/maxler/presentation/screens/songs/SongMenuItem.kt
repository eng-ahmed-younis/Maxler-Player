package com.play.maxler.presentation.screens.songs

import com.play.maxler.domain.models.Song

data class SongMenuItem(
    var menuItemClicked : Boolean,
    var clickedSong : Song?
)