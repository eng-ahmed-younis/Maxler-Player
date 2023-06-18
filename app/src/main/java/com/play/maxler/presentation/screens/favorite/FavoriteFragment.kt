package com.play.maxler.presentation.screens.favorite

import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.view.View
import androidx.navigation.fragment.findNavController
import com.play.maxler.databinding.FragmentFavoriteBinding
import com.play.maxler.common.view.base.BaseFragment

class FavoriteFragment() : BaseFragment<FragmentFavoriteBinding>(FragmentFavoriteBinding::inflate),
    Parcelable {


    constructor( parcel: Parcel) : this() {

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setScreenTitle()

        binding!!.favoriteNavigateBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setScreenTitle(){
        binding?.screenTitle?.text = findNavController().currentDestination?.label.toString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<FavoriteFragment> {
        override fun createFromParcel(parcel: Parcel): FavoriteFragment {
            return FavoriteFragment(parcel)
        }

        override fun newArray(size: Int): Array<FavoriteFragment?> {
            return arrayOfNulls(size)
        }
    }
}