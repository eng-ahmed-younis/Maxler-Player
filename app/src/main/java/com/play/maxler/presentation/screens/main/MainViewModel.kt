package com.play.maxler.presentation.screens.main

import android.app.Application
import android.content.Context
import androidx.lifecycle.*
import com.play.maxler.data.repository.MainRepository
import com.play.maxler.di.MainScope
import com.play.maxler.domain.models.Song
import com.play.maxler.utils.Constants
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

/*
@MainScope
//class MainViewModel @Inject constructor(private val mainRepository: MainRepository) : ViewModel() {
class MainViewModel @Inject constructor(application: Application) : AndroidViewModel(application) {
*/

class MainViewModel constructor(application: Application) : AndroidViewModel(application) {


    private var _navigateToPlayFragment = MutableLiveData<Boolean>()
    val navigateToPlayFragment:LiveData<Boolean>
        get() = _navigateToPlayFragment

    private var _navigateToRecentFragment = MutableLiveData<Boolean>()
    val navigateToRecentFragment:LiveData<Boolean>
        get() = _navigateToRecentFragment

    private var _navigateToFavoriteFragment = MutableLiveData<Boolean>()
    val navigateToFavoriteFragment:LiveData<Boolean>
        get() = _navigateToFavoriteFragment

    private var _navigateToPlaylistFragment = MutableLiveData<Boolean>()
    val navigateToPlaylistFragment:LiveData<Boolean>
        get() = _navigateToPlaylistFragment

    private var _navigateToSearchFragment = MutableLiveData<Boolean>()
    val navigateToSearchFragment:LiveData<Boolean>
        get() = _navigateToSearchFragment

  /*  private val mainRepo by lazy{
        MainRepository(application.applicationContext)
    }*/

   /* private val _songs = MutableStateFlow<List<Song>>(listOf(Constants.emptySong))
    val songs = _songs.asSharedFlow()
*/


    init {
     //   this.Navigation()
       // mainRepo.getAllSongs()
       // getAllAppSongs()
        _navigateToPlayFragment.value = false
        _navigateToRecentFragment.value = false
        _navigateToFavoriteFragment.value = false
        _navigateToPlaylistFragment.value = false
        _navigateToSearchFragment.value = false
    }

    fun navigateToPlayScreen(){_navigateToPlayFragment.value = true}
    fun navigateToPlayScreenComplete(){_navigateToPlayFragment.value = false }

/*
    private fun getAllAppSongs(){
        viewModelScope.launch {
            mainRepo.getAllSongs().collect {
                _songs.emit(it)
            }
        }
    }*/

    inner class Navigation{
        //playing
        fun navigateToPlayScreen(){_navigateToPlayFragment.value = true}
        fun navigateToPlayScreenComplete(){_navigateToPlayFragment.value = false }
        //favorite
        fun navigateToFavoriteScreen() {_navigateToFavoriteFragment.value = true}
        fun navigateToFavoriteScreenComplete(){_navigateToFavoriteFragment.value = false}
        //recent
        fun navigateToRecentScreen(){_navigateToRecentFragment.value = true}
        fun navigateToRecentScreenComplete(){_navigateToRecentFragment.value = false}
        //playlist
        fun navigateToPlaylistScreen() = true.also { _navigateToPlaylistFragment.value = it }
        fun navigateToPlaylistScreenComplete() = false.also { _navigateToPlaylistFragment.value = it }
        // search
        fun navigateToSearchScreen() = true.also { _navigateToSearchFragment.value = it }
        fun navigateToSearchScreenComplete() = false.also { _navigateToSearchFragment.value = it }

    }
}


/*class MainViewModelFactory(private val context: Context):
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(context) as T
        }
        throw Exception("not found view model")
    }
}*/
