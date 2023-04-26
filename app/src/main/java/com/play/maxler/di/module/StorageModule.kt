package com.play.maxler.di.module

import com.play.maxler.data.local.preferences.SharedPreferencesStorage
import com.play.maxler.data.local.preferences.Storage
import dagger.Binds
import dagger.Module

@Module  // Because of @Binds, StorageModule needs to be an abstract class
abstract class StorageModule {

    // Makes Dagger provide SharedPreferencesStorage when a Storage type is requested
    @Binds
    abstract fun provideStorage(storage: SharedPreferencesStorage) : Storage

}
// StorageModule is abstract now because the provideStorage is abstract.
//We've told Dagger that when a Storage object is requested it should create an instance of SharedPreferencesStorage,

// each time need SharedPreferencesStorage inject constructor of dependent of storage interface