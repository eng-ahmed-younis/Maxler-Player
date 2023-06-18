package com.play.maxler.common.view.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.play.maxler.R
import com.play.maxler.presentation.screens.main.MainViewModel

abstract class BaseBottomSheetFragment<VB: ViewBinding>(
    private val bindingInflater:(layoutInflater: LayoutInflater) -> VB,
    private val navGraphViewModel  : Int
) : BottomSheetDialogFragment() {

     val viewModel: MainViewModel by hiltNavGraphViewModels(navGraphId = navGraphViewModel)
    //bindings
    private var _binding:VB? = null
    //Binding
    protected val binding get() = _binding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding =  bindingInflater.invoke(layoutInflater)
        return _binding!!.root
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}