package com.play.maxler.presentation.screens.songs


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.play.maxler.R
import com.play.maxler.databinding.ItemSongBinding
import com.play.maxler.domain.models.Song
import com.play.maxler.presentation.screens.main.MainViewModel

class SongsAdapter constructor(
    private val songClickedListener: SongClickedListener,
    private val mainViewModel: MainViewModel
    ):ListAdapter<Song, SongsAdapter.SongViewHolder>(SongsDiffUtils()){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder { return SongViewHolder.from(parent , mainViewModel) }
    override fun onBindViewHolder(holder: SongViewHolder, position: Int)  = holder.bind(getItem(position),songClickedListener)

    class SongViewHolder constructor(
        private val songsBinding:ItemSongBinding,
        private val mainViewModel: MainViewModel
    ):RecyclerView.ViewHolder(songsBinding.root){

        fun bind(song: Song, clickListener: SongClickedListener){
            songsBinding.apply {
                songs = song
                onClickListener = clickListener
                songsViewModel = mainViewModel
                executePendingBindings()
            }
        }


        companion object{
            fun from(parent:ViewGroup, viewModel: MainViewModel): SongViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view =  DataBindingUtil.inflate<ItemSongBinding>(layoutInflater,
                    R.layout.item_song,parent,false)
                return SongViewHolder(songsBinding = view, mainViewModel = viewModel)
            }
        }
    }


    class SongsDiffUtils :DiffUtil.ItemCallback<Song>(){
        override fun areItemsTheSame(oldItem: Song, newItem: Song): Boolean {
            return oldItem.id == newItem.id }
        override fun areContentsTheSame(oldItem: Song, newItem: Song): Boolean {
            return oldItem == newItem }
    }


}



class SongClickedListener constructor( val clickListener:(id:Long)->Unit){
    fun onClick(song: Song){
        clickListener(song.id)
    }
}