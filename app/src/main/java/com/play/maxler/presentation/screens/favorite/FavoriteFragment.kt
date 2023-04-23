package com.play.maxler.presentation.screens.favorite

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.play.maxler.R
import com.play.maxler.databinding.FragmentFavoriteBinding
import com.play.maxler.databinding.FragmentHomeBinding
import com.play.maxler.utils.base.BaseFragment

class FavoriteFragment : BaseFragment<FragmentFavoriteBinding>(FragmentFavoriteBinding::inflate) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setScreenTitle()

        binding!!.favoriteNavigateBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setScreenTitle(){
        binding?.screenTitle?.text = findNavController().currentDestination?.label.toString()
    }
}