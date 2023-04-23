package com.play.maxler.presentation.screens.playlist

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.play.maxler.databinding.FragmentPlayListBinding
import com.play.maxler.utils.base.BaseFragment


class PlayListFragment : BaseFragment<FragmentPlayListBinding>(FragmentPlayListBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setScreenTitle()


        binding!!.playlistNavigateBack.setOnClickListener {
            findNavController().navigateUp()
        }

    }


    private fun setScreenTitle(){
        binding?.screenTitle?.text = findNavController().currentDestination?.label.toString()
    }

}