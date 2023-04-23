package com.play.maxler.presentation.screens.recent

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.commit
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.play.maxler.R
import com.play.maxler.databinding.FragmentRecentBinding
import com.play.maxler.presentation.screens.play.PlayFragment
import com.play.maxler.utils.base.BaseFragment

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