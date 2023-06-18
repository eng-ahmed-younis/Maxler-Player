package com.play.maxler

import android.app.Application
import dagger.hilt.android.HiltAndroidApp


// trigger hilt code generate
@HiltAndroidApp
class BaseApplication : Application()