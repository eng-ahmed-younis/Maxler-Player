// Copyright (c) 2019 . Wilberforce Uwadiegwu. All Rights Reserved.

package com.play.maxler.presentation.screens.onBoarding

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class Board(
    @DrawableRes val image: Int,
    @StringRes   val title: Int,
    @StringRes   val description: Int
    )