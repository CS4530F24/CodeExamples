package com.example.customviewdemo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.customviewdemo.databinding.FragmentDrawBinding


class DrawFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val binding = FragmentDrawBinding.inflate(inflater)

        val viewModel : SimpleViewModel by activityViewModels()
        viewModel.color.observe(viewLifecycleOwner){
            binding.customView.drawCircle(it)
        }
        return binding.root

    }


}