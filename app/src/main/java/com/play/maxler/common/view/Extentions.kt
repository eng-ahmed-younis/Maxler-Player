package com.play.maxler.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.*
import android.widget.Toast
import androidx.annotation.MenuRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat.MessagingStyle.Message
import androidx.core.content.ContextCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleService
import androidx.navigation.NavController
import com.play.maxler.R

fun Intent.launchScreen(context: Context, targetActivity:Activity){
    context.startActivity(
        Intent(context,targetActivity::class.java)
    )

}


fun Toast.displayMessage(context: Context,message: String){
    Toast.makeText(context,message,Toast.LENGTH_SHORT).show()
}


fun FragmentActivity.bindPlayMenu(
    menuHost: MenuHost,
    @MenuRes menuRes: Int,
    lifecycleOwner: LifecycleOwner,
    onMenuItemSelected: (MenuItem)->Unit
):MenuProvider{

    val menuProvider = object :MenuProvider{
        override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater)  = menuInflater.inflate(menuRes,menu)
        override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
            onMenuItemSelected(menuItem)
            return true
        }
    }
    menuHost.addMenuProvider(menuProvider,lifecycleOwner,Lifecycle.State.RESUMED)
    return menuProvider
}


/*fun Fragment.navigateToScreen(isNavigate:Boolean,navController: NavController,screenId:Int){
    if (isNavigate){
        when (screenId){
            R.id.recentFragment->{
                navController.navigate(R.id.action_homeFragment_to_recentFragment)
            }
            R.id.favoriteFragment->{
                navController.navigate(R.id.action_homeFragment_to_favoriteFragment)
            }
            R.id.playListFragment->{
                navController.navigate(R.id.action_homeFragment_to_playListFragment)
            }
            R.id.searchFragment->{
                navController.navigate(R.id.action_homeFragment_to_searchFragment)
            }
        }
    }
}*/


fun Int.toText(context: Context):String{
    return context.getString(this)
}



/*
fun Activity.hiddenStatusBar(){
    val decorView = this.window.decorView
    if (Build.VERSION.SDK_INT < 30){
        decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        actionBar?.hide()
    }else{
        //decorView.windowInsetsController?.setsys
    //    decorView.windowInsetsController?.hide(WindowInsets.Type.systemBars())
        decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

    }
}
*/








