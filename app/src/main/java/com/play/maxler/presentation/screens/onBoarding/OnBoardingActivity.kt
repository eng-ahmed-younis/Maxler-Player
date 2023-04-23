/*
package com.play.maxler.presentation.onBoarding

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.edit
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.play.maxler.R
import com.play.maxler.data.local.preferences.PreferencesUtils
import com.play.maxler.databinding.ActivityOnBoardingBinding
import com.play.maxler.presentation.screens.main.MainActivity
import com.play.maxler.presentation.screens.permission.PermissionActivity
import com.play.maxler.utils.Constants.HAS_SEEN_ON_BOARDING
import com.play.maxler.utils.Constants.boards
import com.play.maxler.utils.Utils
import com.play.maxler.utils.hiddenStatusBar
import com.play.maxler.utils.launchScreen

class OnBoardingActivity : AppCompatActivity() ,OnPageChangeListener{

    private lateinit var onBoardingBinding: ActivityOnBoardingBinding
    private val sharedPref by lazy {
        PreferencesUtils(this).sharedPreferences
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.hiddenStatusBar()

        onBoardingBinding = ActivityOnBoardingBinding.inflate(layoutInflater)
        setContentView(onBoardingBinding.root)
        setupViewPagerAdapter()
        showSkipButton()



        Log.i("scroll",onBoardingBinding.viewPager.currentItem.toString())

        onBoardingBinding.apply {

            skipButton.setOnClickListener { startToNextActivity() }

            next.setOnClickListener {
                onBoardingBinding.viewPager.setCurrentItem(
                    onBoardingBinding.viewPager.currentItem + 1,
                    true
                )
            }
            back.setOnClickListener {
                onBoardingBinding.viewPager.setCurrentItem(
                onBoardingBinding.viewPager.currentItem - 1,
                true
            ) }
        }


        if (onBoardingBinding.viewPager.currentItem == 0){
            onBoardingBinding.back.visibility = View.GONE
        }else{
            onBoardingBinding.back.visibility = View.VISIBLE
        }

    }


    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        Toast.makeText(this,"show",Toast.LENGTH_SHORT).show()
       // Log.i("scroll",onBoardingBinding.viewPager.currentItem.toString())
        val newProgress = (position + positionOffset) / (boards.size - 1)
        //onBoardingBinding.onBoardingRoot.progress = newProgress
        checkIsLastBoard()
    }
    override fun onPageSelected(position: Int) {
    }
    override fun onPageScrollStateChanged(state: Int) {}


    private fun startToNextActivity() {
         val nextActivity =
             if (Utils.isPermissionGranted(android.Manifest.permission.WRITE_EXTERNAL_STORAGE,this)) {
                 MainActivity()
             } else {
                 PermissionActivity()
             }
            launchNextActivity(nextActivity)
         sharedPref.edit {
             putBoolean(HAS_SEEN_ON_BOARDING, true)
         }
    }

    private fun launchNextActivity(activity:Activity){
        Intent().launchScreen(this,activity)
    }

    private fun checkIsLastBoard(){
        if (onBoardingBinding.viewPager.currentItem.toString() == (boards.size-1).toString()){
            onNextDone()
        }
    }
    private fun onNextDone(){
        onBoardingBinding.next.text = this.getString(R.string.go_it)
    }
    private fun setupViewPagerAdapter(){
        onBoardingBinding.viewPager.adapter = OnBoardingAdapter(this, boards)
    }

    private fun showSkipButton(){
        if (onBoardingBinding.viewPager.currentItem.toString() == (boards.size-1).toString())
            onBoardingBinding.skipButton.visibility = View.VISIBLE
    }

    override fun onDestroy() {
        super.onDestroy()
        onBoardingBinding.viewPager.removeOnPageChangeListener(this)
    }
}




*/



package com.play.maxler.presentation.screens.onBoarding

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.viewpager.widget.ViewPager.LayoutParams
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.play.maxler.R
import com.play.maxler.data.local.preferences.PreferencesUtils
import com.play.maxler.databinding.ActivityOnBoardingBinding
import com.play.maxler.presentation.screens.main.MainActivity
import com.play.maxler.presentation.screens.permission.PermissionActivity
import com.play.maxler.utils.Constants
import com.play.maxler.utils.Constants.boards
import com.play.maxler.utils.Utils
import com.play.maxler.utils.launchScreen

class OnBoardingActivity : AppCompatActivity(), OnBoardEvents {

    private lateinit var onBoardingBinding: ActivityOnBoardingBinding
    private val sharedPref by lazy { PreferencesUtils(this).sharedPreferences }
    private val adapter by lazy { OnBoardingAdapter() }


    override fun onRestart() {
        super.onRestart()
        if (sharedPref.getBoolean(Constants.HAS_SEEN_ON_BOARDING, false))
            finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        onBoardingBinding = ActivityOnBoardingBinding.inflate(layoutInflater)
        setContentView(onBoardingBinding.root)
        setupOnBoardingAdapter()
        setupIndicators()
        setupCurrentIndicator(0)
        onBoardingBinding.viewPager.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                setupCurrentIndicator(position)
            }
        })
        nextOnBoardClicked()
        previousBoardClicked()
        skipBoardingClicked()
    }



    private fun setupOnBoardingAdapter(){
        adapter.submitList(boards.toMutableList())
        onBoardingBinding.viewPager.adapter = adapter
    }

    private fun setupIndicators(){
        val indicators = arrayOfNulls<ImageView>(boards.size)
        val layoutParams : LinearLayout.LayoutParams =
            LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT)
        layoutParams.setMargins(8,0,8,0)
        for (i in indicators.indices){
            indicators[i] = ImageView(applicationContext)
            indicators[i].apply {
                this?.setImageDrawable(ContextCompat.getDrawable(applicationContext,R.drawable.indicator_inactive))
                this?.layoutParams = layoutParams
            }
            onBoardingBinding.indicatorsContainer.addView(indicators[i])
        }
    }

    private fun setupCurrentIndicator(index:Int){
        val childCount = onBoardingBinding.indicatorsContainer.childCount
        for (i in 0 until childCount){
            val imageView = onBoardingBinding.indicatorsContainer[i] as ImageView
            if (i == index)
                imageView.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.indicator_active))
            else
                imageView.setImageDrawable(ContextCompat.getDrawable(applicationContext,R.drawable.indicator_inactive))
        }
    }

    override fun nextOnBoardClicked() {
        onBoardingBinding.fabNext.setOnClickListener {
            if (onBoardingBinding.viewPager.currentItem+1 < boards.size){
                onBoardingBinding.viewPager.currentItem += 1
            }else{
                moveToNextScreen()
            }
        }
    }

    override fun previousBoardClicked() {
        onBoardingBinding.backBoard.setOnClickListener {
            if (onBoardingBinding.viewPager.currentItem == boards.indexOf(boards.first()))
                finish()
            else
                onBoardingBinding.viewPager.currentItem -= 1
        }
    }

    override fun skipBoardingClicked() {
        onBoardingBinding.skipButton.setOnClickListener { moveToNextScreen() }
    }

    private fun moveToNextScreen(){
        sharedPref.edit().putBoolean(Constants.HAS_SEEN_ON_BOARDING,true).apply()
           // Intent().launchScreen(this, targetActivity = MainActivity())
        Intent().launchScreen(this, targetActivity = getNextScreen())
        finish()
    }

    private fun getNextScreen() : AppCompatActivity {
      return  if (!Utils.isPermissionGranted(
                permission = Constants.cameraPermission,
                context = this
            )
        ) { // if false show PermissionActivity
            PermissionActivity()
        } else {
            MainActivity() // if permission granted and on boarding seen before then show MainActivity
        }
    }

    override fun onStop() {
        super.onStop()
        finish()
    }



}



