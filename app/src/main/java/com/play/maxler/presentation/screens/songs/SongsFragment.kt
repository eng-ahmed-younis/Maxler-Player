package com.play.maxler.presentation.screens.songs

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.play.maxler.R
import com.play.maxler.databinding.FragmentSongsBinding
import com.play.maxler.utils.base.BaseFragment
import kotlinx.coroutines.launch


class SongsFragment : BaseFragment<FragmentSongsBinding>(FragmentSongsBinding::inflate) {

 //   private val fragmentContainer = view?.findViewById<View>(R.id.play_nav_graph)
 //   val navController  = Navigation.findNavController(fragmentContainer!!)

    //private val viewModel  by navGraphViewModels<MediaViewModel>(navController.graph.id)
   private val viewModel : AudioViewModel  by navGraphViewModels(R.id.play_graph)
     //   defaultViewModelProviderFactory
   // }
  //  private val viewModel : MediaViewModel by viewModels()

   //private lateinit var viewModel : AudioViewModel


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
