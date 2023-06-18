package com.play.maxler.common.view.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint

abstract class BaseFragment<VB:ViewBinding>(
    private val bindingInflater:(layoutInflater: LayoutInflater) -> VB
) : Fragment() {

    //bindings
    private var _binding:VB? = null
    //Binding
    protected val binding get() = _binding



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        /* هنا بيتم استخدام بيندنج انفليتر اللي عباره عن بروبرتي
        بيطبق عليه الكستم اكسسورز كانه متغير عادي معرف داخل الكلاس
        لكن ده معرف داخل بريمري كونستركتور وليه سيت وجيت زي اي كلاس
        فهو متغير بيقبل دالة هذه الداله تاخد براميتر لياوت انفليتر وترجع بيندنج كلاس خاص بالفرجمنت
        فعند وراثة البيز فراجمنت يتم تمرير الداله انفليت وهي دالة تاخد لياوت انفليتر وترجع بيندنج
        كلاس
        * */
        _binding = bindingInflater.invoke(inflater)
        return binding?.root
    }


    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }


}