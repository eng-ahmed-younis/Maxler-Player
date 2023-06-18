package com.play.maxler.di

import javax.inject.Qualifier
import javax.inject.Scope

@Scope
@MustBeDocumented
@Retention(value = AnnotationRetention.RUNTIME)
annotation class MainScope




@Retention(value = AnnotationRetention.BINARY)
@Qualifier
annotation class RecentMusicPlayed




@Retention(value = AnnotationRetention.BINARY)
@Qualifier
annotation class MediaBrowseCallers