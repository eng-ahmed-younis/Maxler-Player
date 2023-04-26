package com.play.maxler.common.view.base

import androidx.viewbinding.ViewBinding
import com.play.maxler.common.view.base.BaseFragment
import com.play.maxler.databinding.FragmentSongsBinding

abstract class BasePlayerFragment<VB: ViewBinding> : BaseFragment<FragmentSongsBinding>(FragmentSongsBinding::inflate)
{

}