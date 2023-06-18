package com.play.maxler.presentation.screens.songs

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.play.maxler.R
import com.play.maxler.common.view.base.BaseBottomSheetFragment
import com.play.maxler.databinding.SongsMenuItemBinding
import com.play.maxler.presentation.screens.main.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@AndroidEntryPoint
class SongsMenuItemBottomSheet  : BaseBottomSheetFragment<SongsMenuItemBinding>(
    SongsMenuItemBinding::inflate,
    R.id.home_graph
) ,ISongsMenu {
    companion object {
        const val TAG = "SongsOrderBottomSheet"
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding!!.songsMenuTitle.text = arguments?.getString("songTitle")

        binding!!.apply {
            delete.setOnClickListener { }
            share.setOnClickListener { }
            addToPlaylist.setOnClickListener { }
            addToQueue.setOnClickListener { }
            playLater.setOnClickListener { }
        }
    }

    override fun deleteSong() {
        TODO("Not yet implemented")
    }

    override fun shareSong() {
        TODO("Not yet implemented")
    }

    override fun addSongToPlayList() {
        TODO("Not yet implemented")
    }

    override fun addSongToQueue() {
        TODO("Not yet implemented")
    }

    override fun songPlayLater() {
        TODO("Not yet implemented")
    }


}


interface ISongsMenu{
    fun deleteSong()
    fun shareSong()
    fun addSongToPlayList()
    fun addSongToQueue()
    fun songPlayLater()
}