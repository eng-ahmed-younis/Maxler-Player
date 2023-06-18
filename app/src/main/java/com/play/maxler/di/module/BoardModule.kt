package com.play.maxler.di.module

import com.play.maxler.presentation.screens.main.MainActivity
import com.play.maxler.presentation.screens.onBoarding.OnBoardingAdapter
import com.play.maxler.presentation.screens.permission.PermissionActivity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped


@InstallIn(ActivityComponent::class)
@Module
object BoardModule {

    @ActivityScoped
    @Provides
    fun provideBoardAdapter() = OnBoardingAdapter()

    @ActivityScoped
    @Provides
    fun providePermissionScreen() = PermissionActivity()


    @ActivityScoped
    @Provides
    fun provideMainScreen() = MainActivity()

}