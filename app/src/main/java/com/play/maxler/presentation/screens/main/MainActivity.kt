package com.play.maxler.presentation.screens.main

import android.graphics.BlurMaskFilter.Blur
import android.os.Bundle
import android.os.PersistableBundle
import android.view.*
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import coil.load
import coil.transform.RoundedCornersTransformation
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.card.MaterialCardView
import com.google.android.material.navigation.NavigationView
import com.play.maxler.R
import com.play.maxler.di.component.MainComponent
import com.play.maxler.BaseApplication
import com.play.maxler.databinding.ActivityMainBinding
import com.play.maxler.databinding.NavHeaderMainBinding
import javax.inject.Inject


import androidx.fragment.app.activityViewModels

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

  //  private val viewModel by lazy { ViewModelProvider(this).get(MainViewModel::class.java)}
    /* use viewModels() in activity because give instance of activity scope
    * in fragment use activityViewModels() **/
    private val viewModel by viewModels<MainViewModel>()
    // artifact to retrieve the ViewModel in the activity scope


    //  @Inject
  //  lateinit var mainViewModel: MainViewModel
    // Stores an instance of RegistrationComponent so that its Fragments can access it
    lateinit var mainComponent: MainComponent
    private lateinit var mainBinding: ActivityMainBinding
    private lateinit var headerBinding: NavHeaderMainBinding
    private val navController by lazy {
        (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment).navController
    }

    private val bottomNavigationView by lazy {
        findViewById<BottomNavigationView>(R.id.bottomNavigationView)
    }

   private val recentMusicPlayed by lazy {
        findViewById<ConstraintLayout>(R.id.recent_music_played)
    }
    private val recentMusicSection by lazy {
        findViewById<MaterialCardView>(R.id.recent_music_section)
    }

    private val lastSongTitle by lazy {
        findViewById<TextView>(R.id.recent_song_title)
    }
    private val lastSongImage by lazy {
        findViewById<ImageView>(R.id.recent_song_image)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupDataBinding()
        setupNavigation()
        rotateAnimation()



        mainComponent = (applicationContext as BaseApplication).appComponent.mainComponent().create(mainBinding)
        mainComponent.inject(this)


        //val component = DaggerAppComponent

       //component.printAli()


        recentMusicPlayed.setOnClickListener {
            if (navController.currentDestination?.id == R.id.homeFragment){
                viewModel.navigateToPlayScreen()
            }
        }
/*
        viewModel.navigateToPlayFragment.observe(this){
            if (it){
                viewModel.navigateToPlayScreenComplete()
                navController.navigateUp() // to clear previous navigation history
                navController.navigate(R.id.playFragment)
            }
        }*/
        lastSongTitle.isSelected = true
    //  val appComponent =   DaggerAppComponent.builder().navController("esraa mohamed").build()


      //  val appComponent =

            //DaggerAppComponent.factory().create().mainComponent().create(mainBinding)

       // appComponent.getAli().printAli()



    }

    private fun setupDataBinding() {
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)
        headerBinding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.nav_header_main,
            mainBinding.navView,
            false
        )
        headerBinding.navDrawerImage.load(R.drawable.maxler_header_image){
            crossfade(true)
           // transformations(RoundedCornersTransformation(20f))
        }
        mainBinding.navView.addHeaderView(headerBinding.root)
    }


    private fun setupNavigation() {
        // setup bottomNavigationView with navController
        bottomNavigationView.setupWithNavController(navController)
       // mainBinding.navView.setNavigationItemSelectedListener(this)
        mainBinding.navView.setNavigationItemSelectedListener(this)
        handelNavigation()
    }



    private fun handelNavigation(){
        mainBinding.navView.menu.findItem(R.id.theme)
        navController.addOnDestinationChangedListener { nc, destination, _ ->

            if (destination.id in listOf(R.id.homeFragment,R.id.watchFragment2,R.id.discoverFragment)){
                bottomNavigationView.visibility = View.VISIBLE
            }else{
                bottomNavigationView.visibility = View.GONE
            }

            if (destination.id in listOf(R.id.playFragment)){
                recentMusicSection.visibility = View.GONE
            }else{
                recentMusicSection.visibility = View.VISIBLE
            }

        }

    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        return true
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
        lastSongImage.startAnimation(animation)
    }


    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        navController.saveState()
    }


    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        navController.restoreState(savedInstanceState)
    }
}

