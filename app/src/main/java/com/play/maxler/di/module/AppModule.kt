package com.play.maxler.di.module

import android.app.Application
import android.content.Context
import com.play.maxler.data.local.preferences.SharedPreferencesStorage
import com.play.maxler.data.local.preferences.Storage
import com.play.maxler.data.repository.MainRepository
import com.play.maxler.data.repository.SongsRepositoryImpl
import com.play.maxler.domain.repository.SongsRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule constructor(
    //val context : Context
) {


    @Singleton
    @Provides // use MainRepository as return type not MainRepositoryImpl, as dependency use in constructor MainRepository
    fun provideMainRepository(context: Context,songsRepo: SongsRepository): MainRepository {
        return MainRepository( context, songsRepository = songsRepo)
    }

 /*   @Singleton
    @Provides
    fun provideAppSharedPreferences(): SharedPreferencesStorage {
        return SharedPreferencesStorage()
    }*/


 /*   @Provides //scope is not necessary for parameters stored within the module
    @Singleton
     fun provideConstant(): Context {
        return context
    }*/


    @Provides
    @Singleton
    fun provideSongsRepository(context: Context , storage: Storage) : SongsRepository{
        return SongsRepositoryImpl(storage , context)
    }


}