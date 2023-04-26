package com.play.maxler.di.component

import android.content.Context
import com.play.maxler.di.module.AppModule
import com.play.maxler.di.module.StorageModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

/*

@Component(modules = [AppModule::class])
interface AppComponent {

    fun getAli():Ali

    @Component.Builder
    interface Builder{


        @BindsInstance
        fun navController(@Named("this") type:String):Builder

        fun build():AppComponent
    }

}
*/


/*
@Component(modules = [AppModule::class])
interface AppComponent {

    fun getAli():Ali

    @Component.Factory
    interface Factory{


        @BindsInstance
        fun navController(@Named("this") type:String):Builder

        fun create():AppComponent
    }

}*/

@Singleton
@Component(modules = [AppModule::class , StorageModule::class , AppSubComponents::class])
interface AppComponent {

    // Factory to create instances of MainComponent
    @Component.Factory
    interface Factory {
        // tell dagger than create() parameter is one of dependencies -> (context)
        fun create(@BindsInstance context: Context): AppComponent
    }


    fun mainComponent() : MainComponent.Factory

}