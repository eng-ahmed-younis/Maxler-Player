package com.play.maxler.presentation.screens.songs

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.ViewModel
import com.play.maxler.common.data.Constants
import kotlinx.coroutines.flow.*

class PlaybackViewModel (
    private val preferences: SharedPreferences

) : ViewModel(){
    private val _uiState = MutableStateFlow(SongUiState())
    val uiState: StateFlow<SongUiState> = _uiState.asStateFlow()



    init {
     //   getSongs()
            Log.d("AFRC", "SharedViewModelTwo has created!")

    }


        fun loadTest(){
            Log.i("marwan","success")
        }
/*
    private fun getSongs(){
        getSongsUseCase().onEach { result->
            when(result){
                is Resource.Success -> {
                    _uiState.value = SongUiState(songs = result.data ?:Constants.emptyListSongs )
                }
                is Resource.Error -> {
                    _uiState.value = SongUiState(
                        error = result.message ?: "An unexpected error accrued")
                }
                is Resource.Loading -> {
                    _uiState.value = SongUiState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }*/








/*    fun playAll(playId: String = Constants.PLAY_RANDOM) {
        val parentId = lastParendId
        val list = mediaItems.value
        if (parentId == Constants.SONGS_ROOT && list != null) {
            playMediaId(getItemFrmPlayId(playId, list)?.id)
        } else {
            playMediaAfterLoad = playId
            mediaSessionConnection.unsubscribe(parentId, subscriptionCallback)
            mediaSessionConnection.subscribe(Constants.SONGS_ROOT, subscriptionCallback)
        }
    }*/

/*    fun playAll(playId : String = Constants.PLAY_RANDOM){
        // check is songs is want to play
        val parentId = lastParentId
        val list  =
    }*/





















    private val lastParentId: String
    get() = preferences.getString(Constants.LAST_PARENT_ID, Constants.SONGS_ROOT)!!



}