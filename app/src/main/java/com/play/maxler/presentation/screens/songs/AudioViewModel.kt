package com.play.maxler.presentation.screens.songs

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.*

/*class SongsViewModel @Inject constructor(
    private val getSongsUseCase: GetSongsUseCase
    )*/ //: ViewModel() {

    class AudioViewModel () : ViewModel(){
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

        override fun onCleared() {
            super.onCleared()
            Log.d("AFRC", "SharedViewModelTwo has removed!")
        }

}