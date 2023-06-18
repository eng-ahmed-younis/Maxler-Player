package com.play.maxler.presentation.screens.album

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.play.maxler.R
import com.play.maxler.databinding.ItemAlbumBinding
import com.play.maxler.domain.models.Album

class AlbumsAdapter constructor(private val albumClickListener: AlbumClickListener )
    :ListAdapter<Album,AlbumsAdapter.AlbumsViewHolder>(AlbumsDiffUtils()){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumsViewHolder { return AlbumsViewHolder.from(parent) }
    override fun onBindViewHolder(holder: AlbumsViewHolder, position: Int)  = holder.bind(getItem(position),albumClickListener)

    class AlbumsViewHolder constructor(private val AlbumsBinding:ItemAlbumBinding)
        : RecyclerView.ViewHolder(AlbumsBinding.root){

        fun bind(Album: Album, clickListener: AlbumClickListener){
            AlbumsBinding.apply {
                albums = Album
                onClickListener = clickListener
                executePendingBindings()
            }
        }

        companion object{
            fun from(parent: ViewGroup): AlbumsViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view =  DataBindingUtil.inflate<ItemAlbumBinding>(layoutInflater,
                    R.layout.item_album,parent,false)
                return AlbumsViewHolder(AlbumsBinding = view)
            } }
    }

    class AlbumsDiffUtils : DiffUtil.ItemCallback<Album>(){
        override fun areItemsTheSame(oldItem: Album, newItem: Album): Boolean { return oldItem.id == newItem.id }
        override fun areContentsTheSame(oldItem: Album, newItem: Album): Boolean { return oldItem == newItem }
    }

}//end

class AlbumClickListener constructor( val clickListener:(id:Long)->Unit){
    fun onClick(album: Album){
        clickListener(album.id)
    }
}

