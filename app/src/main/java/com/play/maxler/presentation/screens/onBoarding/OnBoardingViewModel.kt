package com.play.maxler.presentation.screens.onBoarding

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OnBoardingViewModel @Inject constructor(): ViewModel() {


    private var _fabNext  =  MutableLiveData<Boolean>()
    val fabNext : LiveData<Boolean>
    get() = _fabNext


    private var _backBoard  =  MutableLiveData<Boolean>()
    val backBoard : LiveData<Boolean>
    get() = _backBoard

   private var _skipButton  =  MutableLiveData<Boolean>()
    val skipButton : LiveData<Boolean>
    get() = _skipButton



    init {
        _backBoard.value = false
        _skipButton.value = false
        _fabNext.value = false
    }




    fun fabNextClicked(){ _fabNext.value = true }
    fun backBoardClicked(){ _backBoard.value = true }
    fun skipButtonClicked(){ _skipButton.value = true }


    fun fabNextClickedComplete(){
        _fabNext.value = false
    }

    fun backBoardClickedComplete(){
        _backBoard.value = false
    }

    fun skipButtonClickedComplete(){
        _skipButton.value = false
    }



}