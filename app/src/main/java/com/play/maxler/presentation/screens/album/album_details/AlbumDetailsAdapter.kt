package com.play.maxler.presentation.screens.artists.artists_details

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.play.maxler.databinding.ItemArtistDetailsBinding
import com.play.maxler.domain.models.Song

class ArtistDetailsAdapter (
 //   private val AlbumItemClickedListener : AlbumItemClickedListener
): ListAdapter<Song, ArtistDetailsAdapter.ViewHolder>(AlbumDiffUtils()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder { return ViewHolder.from(parent) }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) { holder.bind(getItem(position)) }

    class ViewHolder constructor(private val binding : ItemArtistDetailsBinding) : RecyclerView.ViewHolder(binding.root){
        companion object{
            fun from(parent : ViewGroup) : ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = ItemArtistDetailsBinding.inflate(layoutInflater)
                return ViewHolder(view)
            }
        }
        fun bind(song : Song ){

            binding.apply {
                this.song = song
             //   this.AlbumOnClicked = artisClickedListener
                this.executePendingBindings()
            }

        }
    }



    class AlbumDiffUtils : DiffUtil.ItemCallback<Song>(){
        override fun areItemsTheSame(oldItem: Song, newItem: Song): Boolean {
            return oldItem.id == newItem.id
        }
        override fun areContentsTheSame(oldItem: Song, newItem: Song): Boolean {
            return oldItem.artistName == newItem.artistName
        }

    }


  /*  class AlbumItemDetailsClickedListener(val clickListener : (AlbumId : Long  , AlbumName  :String) -> Unit ){
        fun onclick(Album: Album) {
            clickListener(Album.id , Album.name.toString())
        }
    }*/

}