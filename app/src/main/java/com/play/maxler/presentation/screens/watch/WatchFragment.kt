package com.play.maxler.presentation.screens.watch

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.play.maxler.R
import com.play.maxler.databinding.ActivityMainBinding
import com.play.maxler.databinding.FragmentWatchBinding
import com.play.maxler.presentation.screens.main.MainActivity
import com.play.maxler.common.view.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class WatchFragment : BaseFragment<FragmentWatchBinding>(FragmentWatchBinding::inflate) {

    private val recentMusicPlayed by lazy {requireActivity().findViewById<ConstraintLayout>(R.id.recent_music_played)}
    private val viewModel : WatchViewModel  by hiltNavGraphViewModels<WatchViewModel>(R.id.watch_graph)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding?.let {
            it.appBarLayoutWatch.setOnClickListener {
                Toast.makeText(requireContext(),"lolo",Toast.LENGTH_LONG).show()
            }
        }



        recentMusicPlayed.setOnClickListener {
            viewModel.navigateToPlayScreen()
        }

        viewModel.navigateToPlayFragment.observe(viewLifecycleOwner){
            navigateToPlayScreen(it)
        }
    }

    private fun navigateToPlayScreen(isNavigate: Boolean) {

        if (isNavigate && findNavController().currentDestination?.id == R.id.watchFragment2 ){
               val navOptions =
             NavOptions.Builder()
                 .setPopUpTo(
                     //findNavController().graph.findStartDestination().id,
                     R.id.watchFragment2,
                     false,
                     saveState = true
                 )
                 .setRestoreState(true)
                 .setEnterAnim(R.anim.slide_up)
                 .setExitAnim(R.anim.slide_down)
                 // .setPopEnterAnim(R.anim.slide_in_left)
                 .setPopExitAnim(R.anim.slide_down)
                 .build()
         if (isNavigate) {
             viewModel.navigateToPlayScreenComplete()
             findNavController().navigate(Uri.parse("myApp://myFragment_play"), navOptions, null)
         }
          /*  viewModel.navigateToPlayScreenComplete()
            findNavController().navigate(R.id.action_homeFragment_to_playFragment)*/
        }
    }
}