package com.play.maxler.presentation.screens.songs

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.play.maxler.R
import com.play.maxler.common.data.Resource
import com.play.maxler.common.view.base.BaseFragment
import com.play.maxler.databinding.FragmentSongsBinding
import com.play.maxler.data.local.preferences.Storage
import com.play.maxler.presentation.screens.folder.FoldersFragment
import com.play.maxler.presentation.screens.home.HomeFragmentDirections
import com.play.maxler.presentation.screens.main.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

class SongsFragment : BaseFragment<FragmentSongsBinding>(FragmentSongsBinding::inflate) {

    private val viewModel : MainViewModel by navGraphViewModels(R.id.home_graph)
    private lateinit var songsAdapter: SongsAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewModelWithDataBinding()
        createSongsAdapter()
        observeViewData()
        setupRecyclerView(songsAdapter)
    }


   private fun observeViewData(){
       viewLifecycleOwner.lifecycleScope.launch {
           viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
               viewModel.songs.collect{
                   when(it){
                       is Resource.Loading -> { Log.i("loadingxx","loading") }
                       is Resource.Success -> { songsAdapter.submitList(it.data) }
                       is Resource.Error -> { Log.i("loadingxx",it.toString()) }
                   }
               } }
       }

       viewModel.songMenuItem.observe(viewLifecycleOwner){
           if (it!!.menuItemClicked){
               viewModel.songsMenuItemClickedComplete()
               val action =
                   HomeFragmentDirections.actionHomeFragmentToSongsMenuBottomSheetDialogFragment(it.clickedSong!!.title.toString())
               findNavController().navigate(action)
           }
       }


       viewModel.songsSortOrderClicked.observe(viewLifecycleOwner){
           if (it){
               viewModel.songsSortOrderClickedComplete()
               findNavController().navigate(R.id.action_homeFragment_to_songSortOrderBottomSheetDialogFragment) }
       }

       viewModel.navigateToPlayFragment.observe(viewLifecycleOwner){
           if (it){
               viewModel.Navigation().navigateToPlayScreenComplete()
               findNavController().navigate(R.id.action_homeFragment_to_playFragment)
           } }

   }

    private fun setupViewModelWithDataBinding(){ binding?.songsViewModel  = viewModel }
    private fun createSongsAdapter() {
        songsAdapter =  SongsAdapter(
            songClickedListener =  SongClickedListener { onSongItemClick(it)},
            mainViewModel = viewModel
        )
    }

    private fun setupRecyclerView(songAdapter: SongsAdapter){
        binding?.songRecyclerView?.apply {
            adapter = songAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL,false)
        }
    }



    private fun onSongItemClick(id : Long){
     //   viewModel.Playback().playAll()
        viewModel.Navigation().navigateToPlayScreen()
        viewModel.playMediaId(id.toString())

    }


}
