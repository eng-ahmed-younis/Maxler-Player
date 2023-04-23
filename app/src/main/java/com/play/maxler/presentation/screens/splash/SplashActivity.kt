package com.play.maxler.presentation.screens.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.play.maxler.data.local.preferences.PreferencesUtils
import com.play.maxler.presentation.screens.main.MainActivity
import com.play.maxler.presentation.screens.onBoarding.OnBoardingActivity
import com.play.maxler.presentation.screens.permission.PermissionActivity
import com.play.maxler.utils.Constants
import com.play.maxler.utils.Constants.HAS_SEEN_ON_BOARDING
import com.play.maxler.utils.Utils
import com.play.maxler.utils.launchScreen

class SplashActivity : AppCompatActivity() {

    private val preferences by lazy {
        PreferencesUtils(this).sharedPreferences
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
            routeToNextScreen()
    }

    private fun routeToNextScreen() {
        Intent().launchScreen(this,getNextScreen())
        finish()
    }

    private fun getNextScreen() : AppCompatActivity {
        return  if (!preferences.getBoolean(HAS_SEEN_ON_BOARDING, false))  { // if false show OnBoardingActivity
            OnBoardingActivity()
        } else if (!Utils.isPermissionGranted(Constants.cameraPermission,this)) { // if false show PermissionActivity
            PermissionActivity()
        } else { MainActivity() } // if permission granted and on boarding seen before then show MainActivity
    }
}