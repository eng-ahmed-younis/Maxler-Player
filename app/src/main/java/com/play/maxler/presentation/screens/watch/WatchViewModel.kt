package com.play.maxler.presentation.screens.watch

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WatchViewModel @Inject constructor() : ViewModel() {

    private var _navigateToPlayFragment = MutableLiveData<Boolean>()
    val navigateToPlayFragment: LiveData<Boolean>
        get() = _navigateToPlayFragment



    init {
        _navigateToPlayFragment.value = false
    }


    //playing
    fun navigateToPlayScreen(){ _navigateToPlayFragment.value = true }
    fun navigateToPlayScreenComplete(){ _navigateToPlayFragment.value = false }
}