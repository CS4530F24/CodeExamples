package com.example.fragmentdemo

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.fragmentdemo.databinding.Fragment2Binding


/**
 * A simple [Fragment] subclass.
 * Use the [Fragment2.newInstance] factory method to
 * create an instance of this fragment.
 */
class Fragment2 : Fragment() {

    private lateinit var binding: Fragment2Binding
    private var clickCallback  : () -> Unit = {}
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = Fragment2Binding.inflate(layoutInflater)
        binding.button2.setOnClickListener{
            clickCallback()
        }
        return binding.root
    }

    public fun setListener(listener: () -> Unit){
        clickCallback = listener
    }

}