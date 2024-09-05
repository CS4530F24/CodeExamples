package com.example.fragmentdemo

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.fragmentdemo.databinding.Fragment1Binding


class Fragment1 : Fragment() {


    private lateinit var binding: Fragment1Binding
    //default to nothing
    private var clickCallback : () -> Unit = {}
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = Fragment1Binding.inflate(layoutInflater)
        binding.button.setOnClickListener{
            clickCallback()
        }
        return binding.root
    }

    public fun setListener(listener: () -> Unit){
        clickCallback = listener
    }

}