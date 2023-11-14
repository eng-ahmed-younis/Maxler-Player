package com.play.maxler.presentation.screens.songs

import com.play.maxler.domain.models.Song

data class SortOrderItem(
    var sortOrderItemClicked : Boolean,
    var sortOrderItemClickedValue : String?,
    var sortOrderChanged : Boolean
)