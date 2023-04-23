package com.play.maxler.presentation.screens.play

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.play.maxler.R
import com.play.maxler.databinding.FragmentPlayBinding
import com.play.maxler.presentation.screens.songs.AudioViewModel
import com.play.maxler.utils.base.BaseFragment
import com.play.maxler.utils.bindPlayMenu


class PlayFragment : BaseFragment<FragmentPlayBinding>(FragmentPlayBinding::inflate) {

    private val viewmodel : AudioViewModel by navGraphViewModels(R.id.play_graph)

 /*   override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        requireActivity().bindPlayMenu(
            menuHost = requireActivity(),
            menuRes = R.menu.play_screen_menu,
            lifecycleOwner = viewLifecycleOwner)
        {
            when(it.itemId){
                R.id.playPack_speed->{
                    Toast.makeText(context,"playSpeed",Toast.LENGTH_SHORT).show()
                }
                R.id.drive_mode->{
                    Toast.makeText(context,"Drive Mode",Toast.LENGTH_SHORT).show()
                }
            }
        }
        return super.onCreateView(inflater, container, savedInstanceState)

    }*/
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.playingMenu?.setOnClickListener {
            //val menuHost = requireActivity()
            requireActivity().bindPlayMenu(
                menuHost = requireActivity(),
                menuRes = R.menu.play_screen_menu,
                lifecycleOwner = viewLifecycleOwner)
            {
                when(it.itemId){
                    R.id.playPack_speed->{
                        Toast.makeText(context,"playSpeed",Toast.LENGTH_SHORT).show()
                    }
                    R.id.drive_mode->{
                        Toast.makeText(context,"Drive Mode",Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }


        binding?.navigateUp?.setOnClickListener {
            //findNavController().navigateUp()
            val navController = this.findNavController()
            navController.navigateUp()
           // navController.setGraph(R.navigation.nav_graph)
           // navController.navigate(R.id.action_global_homeFragment)
        }



    }




}