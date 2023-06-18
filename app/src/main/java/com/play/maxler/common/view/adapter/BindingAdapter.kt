package com.play.maxler.common.view.adapter

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.play.maxler.R
import com.play.maxler.common.utils.ImageUtils
import com.play.maxler.domain.models.Album
import com.play.maxler.domain.models.Song
import com.play.maxler.presentation.screens.onBoarding.Board
import java.util.concurrent.TimeUnit


@BindingAdapter("android:boardImage")
fun setOnBoardingItemImage(view: ImageView,board: Board){

    board.image.let {
        Glide.with(view.context)
            .load(it)
            .into(view)
    }
}

@BindingAdapter("android:board_description")
fun setOnBoardingTextDescription(view: TextView,board: Board){
    board.description.let {
        view.text = view.context.getText(board.description)
    }
}

@BindingAdapter("android:board_title")
fun setOnBoardingTitle(view: TextView,board: Board){
    board.description.let {
        view.text = view.context.getText(board.title)
    }
}


/* songs */
@BindingAdapter(value = ["song_duration"])
fun TextView.setSongDuration(song: Song){
    val hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(song.duration),
        TimeUnit.MILLISECONDS.toMinutes(song.duration) % TimeUnit.HOURS.toMinutes(1),
        TimeUnit.MILLISECONDS.toSeconds(song.duration) % TimeUnit.MINUTES.toSeconds(1))
    this.text = hms
}

@BindingAdapter("android:song_image")
fun setAlbumImage(imageView: ImageView , song: Song){
    song.albumId.let {
        Glide.with(imageView.context)
            .load(ImageUtils.getAlbumArtUri(albumId = song.albumId))
            .placeholder(R.drawable.maxler_img)
            .into(imageView)
    }
}



/* Albums */
@BindingAdapter("android:album_image")
fun setAlbumImage(imageView: ImageView , album: Album){
    album.id.let {
        Glide.with(imageView.context)
            .load(ImageUtils.getAlbumArtUri(albumId = album.id))
            .placeholder(R.drawable.defalut_album)
            .into(imageView)
    }
}


