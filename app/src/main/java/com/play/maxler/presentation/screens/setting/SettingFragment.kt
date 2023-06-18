package com.play.maxler.presentation.screens.setting

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.play.maxler.R
import com.play.maxler.common.view.base.BaseFragment
import com.play.maxler.databinding.FragmentSettingBinding


class SettingFragment : BaseFragment<FragmentSettingBinding>(FragmentSettingBinding::inflate) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.settingNavigateBack?.setOnClickListener {
            findNavController().navigateUp()
        }
    }


}