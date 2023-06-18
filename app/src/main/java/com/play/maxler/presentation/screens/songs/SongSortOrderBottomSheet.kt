package com.play.maxler.presentation.screens.songs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.play.maxler.R
import com.play.maxler.common.utils.SortOrder
import com.play.maxler.common.view.base.BaseBottomSheetFragment
import com.play.maxler.data.local.preferences.Storage
import com.play.maxler.databinding.SongsSortOrderBinding
import com.play.maxler.presentation.screens.main.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SongSortOrderBottomSheet : BaseBottomSheetFragment<SongsSortOrderBinding>(
    SongsSortOrderBinding::inflate,
    R.id.home_graph
) , ISortOrder{

    @Inject lateinit var storage: Storage

    companion object {
    const val TAG = "SongSortOrderBottomSheet"
}


override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    binding!!.apply {
        sortOrderAZ.setOnClickListener {sortByAZ()}
        sortOrderZA.setOnClickListener {sortByZA()}
        sortOrderArtist.setOnClickListener { sortByArtist()}
        sortOrderAlbum.setOnClickListener {sortByAlbum() }
        sortOrderYear.setOnClickListener { sortByYear()}
        sortOrderDateModified.setOnClickListener { sortByDateModified()}
        sortOrderDateAdded.setOnClickListener { sortByDateAdded()}
        sortOrderComposer.setOnClickListener { sortByDateComposer()}
        sortOrderDurationSongs.setOnClickListener { sortByDateDuration()}
    }


    viewModel.sortOrderItemClickedValue.observe(viewLifecycleOwner){
        it?.let {
            when(it){
                "SONG_A_Z" -> {binding!!.sortOrderAZ.setTextColor(ContextCompat.getColor(requireContext(),R.color.color_primary))}
                "SONG_Z_A" -> {binding!!.sortOrderZA.setTextColor(ContextCompat.getColor(requireContext(),R.color.color_primary))}
                "SONG_ARTIST" ->{binding!!.sortOrderArtist.setTextColor(ContextCompat.getColor(requireContext(),R.color.color_primary))}
                "SONG_ALBUM" ->{binding!!.sortOrderAlbum.setTextColor(ContextCompat.getColor(requireContext(),R.color.color_primary))}
                "SONG_YEAR" ->{binding!!.sortOrderYear.setTextColor(ContextCompat.getColor(requireContext(),R.color.color_primary))}
                "SONG_DATE_MODIFIED" ->{binding!!.sortOrderDateModified.setTextColor(ContextCompat.getColor(requireContext(),R.color.color_primary))}
                "SONG_DATE_ADDED"->{binding!!.sortOrderDateAdded.setTextColor(ContextCompat.getColor(requireContext(),R.color.color_primary))}
                "COMPOSER" ->{binding!!.sortOrderComposer.setTextColor(ContextCompat.getColor(requireContext(),R.color.color_primary))}
                "SONG_DURATION"->{binding!!.sortOrderDurationSongs.setTextColor(ContextCompat.getColor(requireContext(),R.color.color_primary))}
            }
        }
    }

}

    override  fun sortByAZ(){
        storage.songsSortOrder = SortOrder.SongSortOrder.SONG_A_Z
        viewModel.SortOrderUpdate().songsOrderUpdate()
        viewModel.setSortOrderItemClickedValue("SONG_A_Z")
        this.dismiss()
    }

    override fun sortByZA(){
        storage.songsSortOrder = SortOrder.SongSortOrder.SONG_Z_A
        viewModel.SortOrderUpdate().songsOrderUpdate()
        viewModel.setSortOrderItemClickedValue("SONG_Z_A")
        this.dismiss()
    }

    override fun sortByArtist(){
        storage.songsSortOrder = SortOrder.SongSortOrder.SONG_ARTIST
        viewModel.SortOrderUpdate().songsOrderUpdate()
        viewModel.setSortOrderItemClickedValue("SONG_ARTIST")
        this.dismiss()

    }

    override fun sortByAlbum(){
        storage.songsSortOrder = SortOrder.SongSortOrder.SONG_ALBUM
        viewModel.SortOrderUpdate().songsOrderUpdate()
        viewModel.setSortOrderItemClickedValue("SONG_ALBUM")
        this.dismiss()
    }

    override fun sortByYear(){
        storage.songsSortOrder = SortOrder.SongSortOrder.SONG_YEAR
        viewModel.SortOrderUpdate().songsOrderUpdate()
        viewModel.setSortOrderItemClickedValue("SONG_YEAR")
        this.dismiss()
    }

    override fun sortByDateModified(){
        storage.songsSortOrder = SortOrder.SongSortOrder.SONG_DATE_MODIFIED
        viewModel.SortOrderUpdate().songsOrderUpdate()
        viewModel.setSortOrderItemClickedValue("SONG_DATE_MODIFIED")
        this.dismiss()
    }

    override fun sortByDateAdded(){
        storage.songsSortOrder = SortOrder.SongSortOrder.SONG_DATE_ADDED
        viewModel.SortOrderUpdate().songsOrderUpdate()
        viewModel.setSortOrderItemClickedValue("SONG_DATE_ADDED")
        this.dismiss()
    }

    override fun sortByDateComposer(){
        storage.songsSortOrder = SortOrder.SongSortOrder.COMPOSER
        viewModel.SortOrderUpdate().songsOrderUpdate()
        viewModel.setSortOrderItemClickedValue("COMPOSER")
        this.dismiss()
    }

    override fun sortByDateDuration(){
        storage.songsSortOrder = SortOrder.SongSortOrder.SONG_DURATION
        viewModel.SortOrderUpdate().songsOrderUpdate()
        viewModel.setSortOrderItemClickedValue("SONG_DURATION")
        this.dismiss()
    }




}


interface ISortOrder{
     fun sortByAZ()
     fun sortByZA()
     fun sortByArtist()
     fun sortByAlbum()
     fun sortByYear()
     fun sortByDateModified()
     fun sortByDateAdded()
     fun sortByDateComposer()
     fun sortByDateDuration()
}
