package com.play.maxler.presentation.screens.onBoarding

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.play.maxler.R
import com.play.maxler.databinding.ItemOnBoardingBinding

class OnBoardingAdapter() : ListAdapter<Board, OnBoardingAdapter.OnBoardingViewHolder>(BoardDiffUtils()){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnBoardingViewHolder { return OnBoardingViewHolder.from(parent) }
    override fun onBindViewHolder(holder: OnBoardingViewHolder, position: Int) { holder.bind(getItem(position)) }
    class OnBoardingViewHolder constructor(private val boardBinding:ItemOnBoardingBinding)
        : RecyclerView.ViewHolder(boardBinding.root) {
        fun bind(board: Board) {
            boardBinding.apply {
                boards = board
                executePendingBindings()
            }
        }
        companion object{
            fun from(parent:ViewGroup): OnBoardingViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view =  DataBindingUtil.inflate<ItemOnBoardingBinding>(layoutInflater,
                    R.layout.item_on_boarding,parent,false)
                return OnBoardingViewHolder(boardBinding = view)
            }
        }
    }
}
    class BoardDiffUtils : DiffUtil.ItemCallback<Board>(){
        override fun areItemsTheSame(oldItem: Board, newItem: Board): Boolean { return oldItem.title == newItem.title}
        override fun areContentsTheSame(oldItem: Board, newItem: Board): Boolean { return oldItem == newItem }
    }
