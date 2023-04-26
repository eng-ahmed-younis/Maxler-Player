package com.play.maxler.presentation.screens.recent

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.navigation.fragment.findNavController
import com.play.maxler.databinding.FragmentRecentBinding
import com.play.maxler.common.view.base.BaseFragment

class RecentFragment : BaseFragment<FragmentRecentBinding>(FragmentRecentBinding::inflate) {




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setScreenTitle()


        binding!!.recentNavigateBack.setOnClickListener {
            findNavController().navigateUp()
        }
        Log.i("tito","koko")
    }

    private fun setScreenTitle(){
        binding?.screenTitle?.text = findNavController().currentDestination?.label.toString()
    }

}