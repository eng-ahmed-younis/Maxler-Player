package com.play.maxler.presentation.screens.play

import android.drm.DrmStore.Playback.RESUME
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.play.maxler.R
import com.play.maxler.common.view.base.BaseFragment
import com.play.maxler.databinding.FragmentPlayBinding
import com.play.maxler.presentation.screens.home.HomeFragmentDirections
import com.play.maxler.presentation.screens.main.MainViewModel
import com.play.maxler.utils.bindPlayMenu


class PlayFragment : BaseFragment<FragmentPlayBinding>(FragmentPlayBinding::inflate) {

    private val viewModel : MainViewModel by hiltNavGraphViewModels(R.id.home_graph)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
     super.onViewCreated(view, savedInstanceState)
        setupViews()
        observeViewData()
    }



    private fun observeViewData(){
        viewModel.playOverFlowMenu.observe(viewLifecycleOwner){
            if (it){
                viewModel.playOverFlowMenuClickedComplete()
                findNavController().navigate(R.id.action_playFragment_to_playOverFlowMenuBottomSheet)
            }
        }
    }



    private fun setupViews(){
        binding?.viewModel = viewModel
        binding?.navigateUp?.setOnClickListener {
            val navController = this.findNavController()
            navController.navigateUp()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }



}