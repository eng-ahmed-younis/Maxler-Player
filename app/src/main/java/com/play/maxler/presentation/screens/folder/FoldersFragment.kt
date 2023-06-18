package com.play.maxler.presentation.screens.folder

import android.os.Bundle
import android.view.View
import com.play.maxler.databinding.FragmentFoldersBinding
import com.play.maxler.common.view.base.BaseFragment
import com.play.maxler.presentation.artists.ArtistsFragment
import dagger.hilt.android.AndroidEntryPoint

class FoldersFragment : BaseFragment<FragmentFoldersBinding>(FragmentFoldersBinding::inflate) {

    companion object{
        fun newInstance() = FoldersFragment()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }


}