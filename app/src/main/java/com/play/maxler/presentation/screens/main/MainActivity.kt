package com.play.maxler.presentation.screens.main

import android.os.Bundle
import android.view.*
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.play.maxler.R
import com.play.maxler.databinding.ActivityMainBinding


import com.play.maxler.presentation.screens.songs.SongsMenuItemBottomSheet
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    /* use viewModels() in activity because give instance of activity scope
    * in fragment use activityViewModels() **/
     private val viewModel by viewModels<MainViewModel>()
    // artifact to retrieve the ViewModel in the activity scope
    private lateinit var mainBinding: ActivityMainBinding
    private val navController by lazy { (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment).navController }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupDataBinding()
        setupNavigation()
        rotateAnimation()
        mainBinding.recentSongTitle.isSelected = true
    }

    private fun setupDataBinding() {
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)
    }


    private fun setupNavigation() {
        // setup bottomNavigationView with navController
        mainBinding.bottomNavigationView.setupWithNavController(navController)
       handelNavigation()
    }


    private fun handelNavigation(){
        navController.addOnDestinationChangedListener { nc, destination, _ ->
            if (destination.id in listOf(
                    R.id.homeFragment,
                    R.id.watchFragment2,
                    R.id.discoverFragment,
                    R.id.songSortOrderBottomSheetDialogFragment,
                    R.id.songsMenuBottomSheetDialogFragment,
                    R.id.playOverFlowMenuBottomSheet
                )){
                mainBinding.bottomNavigationView.visibility = View.VISIBLE
            }else{
                mainBinding.bottomNavigationView.visibility = View.GONE
            }

            if (destination.id == R.id.playOverFlowMenuBottomSheet){
                mainBinding.bottomNavigationView.visibility = View.GONE
            }


            if (destination.id in listOf(R.id.playFragment,R.id.settingFragment,R.id.discoverFragment,R.id.playOverFlowMenuBottomSheet)){
                mainBinding.recentMusicSection.visibility = View.GONE
            }else{
                mainBinding.recentMusicSection.visibility = View.VISIBLE
            }

        }

    }

    private fun rotateAnimation(){
        val animation = AnimationUtils.loadAnimation(this,R.anim.rotate).apply {
            duration = 10000
            repeatCount = Animation.INFINITE
            repeatMode = Animation.INFINITE
            this.setAnimationListener(object : Animation.AnimationListener{
                override fun onAnimationStart(animation: Animation?) {}
                override fun onAnimationEnd(animation: Animation?) { animation?.start() }
                override fun onAnimationRepeat(animation: Animation?) {}
            })
        }
        animation.start()
        mainBinding.recentSongImage.startAnimation(animation)
    }




}

