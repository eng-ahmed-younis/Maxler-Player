package com.play.maxler

import android.app.Application
import com.play.maxler.di.component.AppComponent
import com.play.maxler.di.component.DaggerAppComponent

//import com.play.maxler.di.DaggerAppComponent

//@HiltAndroidApp
class BaseApplication : Application() {

    //dagger

   // appComponent lives in the Application class to share its lifecycle
    // Reference to the application graph that is used across the whole app
    val appComponent: AppComponent by lazy {
        initializeApplicationGraphComponent()
    }

    private fun  initializeApplicationGraphComponent() : AppComponent {
        return DaggerAppComponent.factory().create(applicationContext)
    }

}