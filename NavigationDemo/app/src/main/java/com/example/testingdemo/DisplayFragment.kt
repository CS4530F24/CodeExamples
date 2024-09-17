package com.example.testingdemo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.testingdemo.databinding.FragmentDisplayBinding


class DisplayFragment : Fragment() {
   private val viewModel: EmailViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentDisplayBinding.inflate(layoutInflater, container, false)
        viewModel.username.observe(viewLifecycleOwner){
            binding.username.text = it
        }
        viewModel.domain.observe(viewLifecycleOwner){
            binding.domain.text = it
        }
        return binding.root
    }
}