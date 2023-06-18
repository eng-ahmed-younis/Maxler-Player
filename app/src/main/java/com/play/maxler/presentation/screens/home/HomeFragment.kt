package com.play.maxler.presentation.screens.home

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.os.Bundle
import android.transition.Slide
import android.transition.TransitionManager
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.animation.AnimationUtils
import android.view.animation.TranslateAnimation
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import androidx.transition.Transition
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.play.maxler.R
import com.play.maxler.databinding.FragmentHomeBinding
import com.play.maxler.presentation.screens.main.MainViewModel
import com.play.maxler.common.data.Constants
import com.play.maxler.common.view.base.BaseFragment
import com.play.maxler.utils.slideTopSectionDown
import com.play.maxler.utils.slideTopSectionUp
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    private val recentMusicPlayed by lazy {requireActivity().findViewById<ConstraintLayout>(R.id.recent_music_played)}
    private val viewModel : MainViewModel by hiltNavGraphViewModels(R.id.home_graph)
    private lateinit var rotationAnimSet: AnimatorSet


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupHomeViewPager()
        setupHomeObservers()
        setupViews()
    }


    private fun navigateToRecentScreen(isNavigate:Boolean){
        if (isNavigate){
            viewModel.Navigation().navigateToRecentScreenComplete()
            findNavController().navigate(R.id.action_homeFragment_to_recentFragment) }
    }

    private fun navigateToFavoritesScreen(isNavigate:Boolean){
        if (isNavigate){
            viewModel.Navigation().navigateToFavoriteScreenComplete()
            findNavController().navigate(R.id.action_homeFragment_to_favoriteFragment) }
    }

    private fun navigateToPlaylistScreen(isNavigate: Boolean){
        if (isNavigate){
            viewModel.Navigation().navigateToPlaylistScreenComplete()
            findNavController().navigate(R.id.action_homeFragment_to_playListFragment) }
    }

    private fun navigateToSearchScreen(isNavigate: Boolean){
        if (isNavigate){
            viewModel.Navigation().navigateToSearchScreenComplete()
            findNavController().navigate(R.id.action_homeFragment_to_searchFragment) }
    }

    private fun navigateToSettingScreen(isNavigate: Boolean){
        if (isNavigate){
            viewModel.Navigation().navigateToSettingScreenComplete()
            findNavController().navigate(R.id.action_homeFragment_to_settingFragment) }
    }


    private fun navigateToPlayScreen(isNavigate: Boolean) {
        Log.i("recentMusicPlayed", "value is $isNavigate")

        if (isNavigate)
            Log.i("recentMusicPlayed", "play $isNavigate")

        if (isNavigate) {
            viewModel.Navigation().navigateToPlayScreenComplete()
            findNavController().navigate(R.id.action_homeFragment_to_playFragment)
        }
    }


    private fun setupHomeObservers(){
        viewModel.apply {
            navigateToPlayFragment.observe(viewLifecycleOwner){ navigateToPlayScreen(it) }
            navigateToRecentFragment.observe(viewLifecycleOwner){navigateToRecentScreen(it)}
            navigateToPlaylistFragment.observe(viewLifecycleOwner){navigateToPlaylistScreen(it)}
            navigateToFavoriteFragment.observe(viewLifecycleOwner){navigateToFavoritesScreen(it)}
            navigateToSearchFragment.observe(viewLifecycleOwner){navigateToSearchScreen(it)}
            navigateToSettingFragment.observe(viewLifecycleOwner){navigateToSettingScreen(it)}
        }
    }

    private fun setupViews(){
        rotationAnimSet = AnimatorInflater.loadAnimator(context, R.animator.album_art_rotation) as AnimatorSet
      //  rotationAnimSet.setTarget(albumArt)

        binding?.apply {
            this.let {
                recentMusic.setOnClickListener {viewModel.Navigation().navigateToRecentScreen()}
                playlistMusic.setOnClickListener { viewModel.Navigation().navigateToPlaylistScreen()}
                favoriteMusic.setOnClickListener { viewModel.Navigation().navigateToFavoriteScreen()}
                drawerButtonHome.setOnClickListener { viewModel.Navigation().navigateToSettingScreen()}
                search.setOnClickListener {
                    viewModel.Navigation().navigateToSearchScreen()
                }
            }

            recentMusicPlayed.setOnClickListener {
                viewModel.Navigation().navigateToPlayScreen()
            }
        }
    }

    private fun setupHomeViewPager() {
        HomeViewPagerAdapter(this.requireActivity()).apply {
            repeat(Constants.screens.size) {
                this.addScreen(Constants.screens[it].screenInstance) }
            binding?.homePager?.adapter = this }
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



