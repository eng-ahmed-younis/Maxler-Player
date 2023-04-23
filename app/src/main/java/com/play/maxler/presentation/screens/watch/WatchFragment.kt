package com.play.maxler.presentation.watch

import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.play.maxler.databinding.ActivityMainBinding
import com.play.maxler.databinding.FragmentWatchBinding
import com.play.maxler.presentation.screens.main.MainActivity
import com.play.maxler.utils.base.BaseFragment
import javax.inject.Inject

class WatchFragment : BaseFragment<FragmentWatchBinding>(FragmentWatchBinding::inflate) {

    @Inject
     lateinit var mainBinding: ActivityMainBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as MainActivity).mainComponent.inject(this)


        binding?.let {
            it.appBarLayoutWatch.setOnClickListener {
                Toast.makeText(requireContext(),"lolo",Toast.LENGTH_LONG).show()
            }
        }
    }
}