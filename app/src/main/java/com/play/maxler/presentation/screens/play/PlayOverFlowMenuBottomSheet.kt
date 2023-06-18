package com.play.maxler.presentation.screens.play

import android.os.Bundle
import android.view.View
import com.play.maxler.R
import com.play.maxler.common.view.base.BaseBottomSheetFragment
import com.play.maxler.databinding.PlayOverFlowMenuBinding

class PlayOverFlowMenuBottomSheet : BaseBottomSheetFragment<PlayOverFlowMenuBinding>(
    PlayOverFlowMenuBinding::inflate,
    R.id.home_graph
){


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}


interface IPlayOverFlowMenu{

}