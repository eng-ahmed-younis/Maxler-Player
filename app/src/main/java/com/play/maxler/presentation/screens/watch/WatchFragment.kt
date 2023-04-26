package com.play.maxler.presentation.screens.watch

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.navigation.navGraphViewModels
import com.play.maxler.R
import com.play.maxler.databinding.ActivityMainBinding
import com.play.maxler.databinding.FragmentWatchBinding
import com.play.maxler.presentation.screens.main.MainActivity
import com.play.maxler.common.view.base.BaseFragment
import javax.inject.Inject

class WatchFragment : BaseFragment<FragmentWatchBinding>(FragmentWatchBinding::inflate) {

     private val viewModel : WatchViewModel  by navGraphViewModels(R.id.watch_graph)

    @Inject
     lateinit var mainBinding: ActivityMainBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        Log.i("asmaa",viewModel.x.toString())
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as MainActivity).mainComponent.inject(this)


        binding?.let {
            it.appBarLayoutWatch.setOnClickListener {
                Toast.makeText(requireContext(),"lolo",Toast.LENGTH_LONG).show()
            }
        }
    }
}