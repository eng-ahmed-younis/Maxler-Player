package com.play.maxler.di.component

import com.play.maxler.databinding.ActivityMainBinding
import com.play.maxler.di.MainScope
import com.play.maxler.presentation.screens.home.HomeFragment
import com.play.maxler.presentation.screens.main.MainActivity
import com.play.maxler.presentation.screens.watch.WatchFragment
import dagger.BindsInstance
import dagger.Subcomponent


@MainScope
@Subcomponent
interface MainComponent {


    @Subcomponent.Factory
    interface Factory{
        fun create(@BindsInstance mainBinding: ActivityMainBinding): MainComponent
    }

    fun inject(activity: MainActivity)
    fun inject(fragment: HomeFragment)
    fun inject(fragment: WatchFragment)
}