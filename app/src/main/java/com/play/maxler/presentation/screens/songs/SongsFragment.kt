package com.play.maxler.presentation.screens.songs

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.navGraphViewModels
import com.play.maxler.R
import com.play.maxler.databinding.FragmentSongsBinding
import com.play.maxler.common.view.base.BasePlayerFragment
import kotlinx.coroutines.launch


/*
class SongsFragment : BaseFragment<FragmentSongsBinding>(FragmentSongsBinding::inflate) {
*/
class SongsFragment : BasePlayerFragment<FragmentSongsBinding>() {


    //private val viewModel  by navGraphViewModels<MediaViewModel>(navController.graph.id)
   private val viewModel : PlaybackViewModel  by navGraphViewModels(R.id.home_graph)

   //private val viewModel : PlaybackViewModel by viewModels()



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewLifecycleOwner.lifecycleScope.launch {

            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.uiState.collect {
                        Log.i("lolo","init")
                        // Process item
                        // it.songs
                        // }
                    }

            }



            //viewModel.loadTest()
        }





    }


    override fun onResume() {
        super.onResume()



    }

}
