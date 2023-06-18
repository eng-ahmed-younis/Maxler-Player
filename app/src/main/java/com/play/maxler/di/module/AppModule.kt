package com.play.maxler.di.module

import android.app.Application
import android.content.ComponentName
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.Room
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.play.maxler.R
import com.play.maxler.data.local.database.dp.MaxlerRoomDatabase
import com.play.maxler.data.local.preferences.Storage
import com.play.maxler.data.repository.*
import com.play.maxler.domain.repository.*
import com.play.maxler.presentation.exoplayer.MediaPlaybackService
import com.play.maxler.presentation.exoplayer.MediaSessionConnection
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ServiceScoped
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class) // scope of component is scope of all app
@Module
object AppModule {


    @Singleton
    @Provides // use MainRepository as return type not MainRepositoryImpl, as dependency use in constructor MainRepository
    fun provideMainRepository(
        @ApplicationContext context : Context ,
        songsRepo : SongsRepository,
        albumRepository: AlbumRepository,
        playlistRepository: PlaylistRepository,
        historyRepository: HistoryRepository
        ) : MainRepository {
        return MainRepositoryImpl(
            context = context,
            songsRepository = songsRepo,
            albumRepository = albumRepository,
            playlistRepository =  playlistRepository,
            historyRepository = historyRepository
        )
    }


    @Provides
    @Singleton
    fun  providePlayListsRepository(
        @ApplicationContext context: Context
    ) : PlaylistRepository{
        return PlaylistRepositoryImpl(context)
    }


    @Provides
    @Singleton
    fun provideSongsRepository(
        @ApplicationContext context: Context ,
        storage: Storage
    ) : SongsRepository{
        return SongsRepositoryImpl( context = context , storage = storage)
    }

    @Provides
    @Singleton
    fun provideAlbumsRepository(
        songsRepo: SongsRepository,
        storage: Storage
    ) : AlbumRepository{
        return AlbumRepositoryImpl( songsRepository = songsRepo, storage = storage)
    }


    @Provides
    @Singleton
    fun provideMaxlerRoomDatabase(app : Application)  : MaxlerRoomDatabase {
        return Room.databaseBuilder(
            app,
            MaxlerRoomDatabase::class.java,
            MaxlerRoomDatabase.RECENT_DATABASE_NAME
        ).build()
    }


    @Provides
    @Singleton
    fun providesHistoryRepository(maxlerDataBase : MaxlerRoomDatabase) : HistoryRepository{
        return HistoryRepositoryImpl(maxlerDataBase.historyDao)
    }



    @Singleton
    @Provides
    fun provideGlideInstance(
        @ApplicationContext context: Context
    ): RequestManager = Glide.with(context).setDefaultRequestOptions(
        RequestOptions()
            .placeholder(R.drawable.ic_image)
            .error(R.drawable.ic_image)
            .diskCacheStrategy(DiskCacheStrategy.DATA)
    )



    @Provides
    @Singleton
    fun provideMediaSessionConnection(
        @ApplicationContext context: Context,
        componentName: ComponentName,
        storage: Storage
    ): MediaSessionConnection {
        return MediaSessionConnection(context = context , serviceComponent = componentName,storage)
    }


    @Provides
    @Singleton
    fun provideComponentName(
        @ApplicationContext context: Context
    ) : ComponentName{
        return ComponentName( context , MediaPlaybackService::class.java)
    }

}

/* each hilt container comes from set of default bindings that can be injected as dependencies into custom bindings
example is "@ApplicationContext"

You can't provide or do a findViewById on an activity module that is compatible with dagger-android,
one reason being that the module is created before the view is actually on the hierarchy.
* */





