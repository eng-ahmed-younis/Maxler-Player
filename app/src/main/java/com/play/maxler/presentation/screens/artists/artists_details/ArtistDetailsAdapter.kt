package com.play.maxler.presentation.screens.artists

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.play.maxler.databinding.ItemArtistBinding
import com.play.maxler.domain.models.Artist
import com.play.maxler.domain.models.DiscoverItem

class ArtistAdapter (
    private val artistItemClickedListener : ArtistItemClickedListener
): ListAdapter<Artist,ArtistAdapter.ViewHolder>(ArtistDiffUtils()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder { return ViewHolder.from(parent) }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) { holder.bind(getItem(position),artistItemClickedListener) }

    class ViewHolder constructor(private val binding : ItemArtistBinding) : RecyclerView.ViewHolder(binding.root){
        companion object{
            fun from(parent : ViewGroup) : ViewHolder{
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = ItemArtistBinding.inflate(layoutInflater)
                return ViewHolder(view)
            }
        }
        fun bind(artist : Artist , artisClickedListener : ArtistItemClickedListener){

            binding.apply {
                this.artist = artist
                this.artistOnClicked = artisClickedListener
                this.executePendingBindings()
            }

        }
    }



    class ArtistDiffUtils : DiffUtil.ItemCallback<Artist>(){
        override fun areItemsTheSame(oldItem: Artist, newItem: Artist): Boolean {
            return oldItem.id == newItem.id
        }
        override fun areContentsTheSame(oldItem: Artist, newItem: Artist): Boolean {
            return oldItem.name == newItem.name
        }

    }


    class ArtistItemClickedListener(val clickListener : (artistId : Long  , artistName  :String) -> Unit ){
        fun onclick(artist: Artist) {
            clickListener(artist.id , artist.name.toString())
        }
    }

}