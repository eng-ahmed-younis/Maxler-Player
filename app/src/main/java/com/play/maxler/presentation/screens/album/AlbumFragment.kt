package com.play.maxler.presentation.screens.album

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.play.maxler.R
import com.play.maxler.common.data.Resource
import com.play.maxler.common.view.base.BaseFragment
import com.play.maxler.databinding.FragmentAlbumBinding
import com.play.maxler.presentation.screens.main.MainViewModel
import com.play.maxler.presentation.screens.songs.SongsFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

class AlbumFragment : BaseFragment<FragmentAlbumBinding>(FragmentAlbumBinding::inflate) {

    private val viewModel : MainViewModel by navGraphViewModels(R.id.home_graph)
    private lateinit var albumAdapter : AlbumsAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

         albumAdapter  = AlbumsAdapter(AlbumClickListener { it ->
            it.let {
                viewModel.Navigation().navigateToAlbumDetailsScreen()
            }
        })

        observeViewData()
        setupRecyclerView(albumAdapter)
    }



    private fun setupRecyclerView(albumsAdapter: AlbumsAdapter){
        binding?.albumRecyclerView?.apply {
            adapter = albumsAdapter
            layoutManager = GridLayoutManager(requireContext(),2)
        }
    }

    private fun observeViewData(){
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.album.collect{
                    when(it){
                        is Resource.Loading -> { Log.i("loadingxx","loading") }
                        is Resource.Success -> { albumAdapter.submitList(it.data) }
                        is Resource.Error -> { Log.i("loadingxx",it.toString()) }
                    }
                } } }

        viewModel.navigateToAlbumDetailsFragment.observe(viewLifecycleOwner){
            if (it){
                viewModel.Navigation().navigateToAlbumDetailsScreenComplete()
                findNavController().navigate(R.id.action_homeFragment_to_albumDetailsFragment)
            } }
    }

/*    private fun getAlbumById(albumId : Long){
        val action = AlbumFragmentD
      //  val action = CategoryFragmentDirections.
        actionCategoryFragmentToDetailsFragment(mealName)
        val extras = FragmentNavigatorExtras(
            cardView_details to "shared_element_container_category" )
        findNavController().navigate(action,extras)
    }*/


    companion object{
        fun newInstance() = AlbumFragment()
    }
}