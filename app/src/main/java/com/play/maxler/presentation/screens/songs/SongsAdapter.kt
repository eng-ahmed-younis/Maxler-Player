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

class SongsAdapter constructor(private val songClickedListener : SongsClickListener)
    :ListAdapter<Song,SongsAdapter.SongsViewHolder>(SongsDiffUtils()){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongsViewHolder {
        return SongsViewHolder.from(parent)

    }
    override fun onBindViewHolder(holder: SongsViewHolder, position: Int)  = holder.bind(getItem(position),songClickedListener)




    class SongsViewHolder constructor(private val songsBinding:ItemSongBinding)
        : RecyclerView.ViewHolder(songsBinding.root){

        fun bind(song: Song, clickListener: SongsClickListener){
            songsBinding.apply {
                allSongs = song
              //  onClickListener = clickListener
                executePendingBindings()
            }
        }


        companion object{
            fun from(parent: ViewGroup): SongsViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                //   val view = layoutInflater.inflate(R.layout.songs_item,parent,false)
                // val view = SongsItemBinding.inflate(layoutInflater,parent,false)
                val view =  DataBindingUtil.inflate<ItemSongBinding>(layoutInflater,
                    R.layout.item_song,parent,false)
                return SongsViewHolder(songsBinding = view)
            }
        }


    }

    class SongsDiffUtils : DiffUtil.ItemCallback<Song>(){
        override fun areItemsTheSame(oldItem: Song, newItem: Song): Boolean { return oldItem.id == newItem.id }
        override fun areContentsTheSame(oldItem: Song, newItem: Song): Boolean { return oldItem == newItem }
    }


}//end


class SongsClickListener constructor( val clickListener:(id:Long)->Unit){
    fun onClick(song: Song){
        clickListener(song.id)
    }
}



/*

class SongAdapter constructor(private val clickListener: SongListener)
    :ListAdapter<Song, SongAdapter.SongViewHolder>(SongDiffUtils()){
*/
