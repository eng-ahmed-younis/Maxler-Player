package com.play.maxler.presentation.screens.home

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.core.view.GravityCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import com.play.maxler.R
import com.play.maxler.databinding.ActivityMainBinding
import com.play.maxler.databinding.FragmentHomeBinding
import com.play.maxler.presentation.screens.main.MainActivity
import com.play.maxler.presentation.screens.main.MainViewModel
import com.play.maxler.common.data.Constants
import com.play.maxler.common.view.base.BaseFragment
import javax.inject.Inject


class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    @Inject
    lateinit var mainActivityMainBinding: ActivityMainBinding
    /* private  val viewModel by lazy {
        ViewModelProvider(requireActivity())[MainViewModel::class.java]
    }*/


    private val viewModel by activityViewModels<MainViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as MainActivity).mainComponent.inject(this)
        setupHomeViewPager()
        binding?.drawerButton?.setOnClickListener {
            mainActivityMainBinding.drawerLayout.openDrawer(GravityCompat.START)
        }

        //  viewModel.navigateToPlayFragment.observe(viewLifecycleOwner){navigateToPlayScreen(it) }
         viewModel.navigateToRecentFragment.observe(viewLifecycleOwner){navigateToRecentScreen(it)}
        viewModel.navigateToPlaylistFragment.observe(viewLifecycleOwner){navigateToPlaylistScreen(it)}
        viewModel.navigateToFavoriteFragment.observe(viewLifecycleOwner){navigateToFavoritesScreen(it)}
        viewModel.navigateToSearchFragment.observe(viewLifecycleOwner){navigateToSearchScreen(it)}
        binding?.apply {
            this.let {
                recentMusic.setOnClickListener {viewModel.Navigation().navigateToRecentScreen()}
                playlistMusic.setOnClickListener { viewModel.Navigation().navigateToPlaylistScreen()}
                favoriteMusic.setOnClickListener { viewModel.Navigation().navigateToFavoriteScreen()}
                drawerButton.setOnClickListener { mainActivityMainBinding.drawerLayout.openDrawer(GravityCompat.START)}
                search.setOnClickListener {
                    viewModel.Navigation().navigateToSearchScreen()
                }
            }

        }
    }


    private fun navigateToRecentScreen(isNavigate:Boolean){
        if (isNavigate){
            viewModel.Navigation().navigateToRecentScreenComplete()
            findNavController().navigate(R.id.action_homeFragment_to_recentFragment)
        }
    }

    private fun navigateToFavoritesScreen(isNavigate:Boolean){
        if (isNavigate){
            viewModel.Navigation().navigateToFavoriteScreenComplete()
            findNavController().navigate(R.id.action_homeFragment_to_favoriteFragment)
        }
    }

    private fun navigateToPlaylistScreen(isNavigate: Boolean){
        if (isNavigate){
            viewModel.Navigation().navigateToPlaylistScreenComplete()
            findNavController().navigate(R.id.action_homeFragment_to_playListFragment)
        }
    }

    private fun navigateToSearchScreen(isNavigate: Boolean){
        if (isNavigate){
            viewModel.Navigation().navigateToSearchScreenComplete()
            findNavController().navigate(R.id.action_homeFragment_to_searchFragment)
        }
    }

          private fun navigateToPlayScreen(isNavigate: Boolean) {
       /*    if (isNavigate){
            viewModel.Navigation().navigateToPlayScreenComplete()
            findNavController().navigate(R.id.action_homeFragment_to_playFragment)
        }*/

       /*  if(isNavigate){
            viewModel.Navigation().navigateToPlayScreenComplete()
            val navController = this.findNavController()
            navController.setGraph(R.navigation.nav_play)
            navController.navigate(R.id.action_global_playFragment2)
        }*/


        //  app:enterAnim="@anim/slide_up"
        //app:exitAnim="@anim/slide_down"
        // app:popExitAnim="@anim/slide_down"

            val navOptions =
                NavOptions.Builder()
                    .setPopUpTo(
                        findNavController().graph.findStartDestination().id,
                        // R.id.homeFragment,
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
                viewModel.Navigation().navigateToPlayScreenComplete()
                findNavController().navigate(Uri.parse("myApp://myFragment_play"), navOptions, null)
            }

        }


        private fun setupHomeViewPager() {
            HomeViewPagerAdapter(this.requireActivity()).apply {
                repeat(Constants.screens.size) { this.addScreen(Constants.screens[it].screenInstance) }
                binding?.homePager?.adapter = this
            }
            connectTableLayoutWithViewPager()
        }

        private fun connectTableLayoutWithViewPager() {
            binding?.let {
                TabLayoutMediator(it.tabLayout, it.homePager) { tab, position ->
                    tab.text = context?.getText(Constants.screens[position].title).toString()
                }.attach()
            }
        }



}



