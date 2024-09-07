package com.example.twofragmentsonevm

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.twofragmentsonevm.databinding.Fragment1Binding


/**
 * A simple [Fragment] subclass.
 * Use the [Fragment1.newInstance] factory method to
 * create an instance of this fragment.
 */
class Fragment1 : Fragment() {

    private lateinit var binding : Fragment1Binding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Grab the fragment view binding
        binding =  Fragment1Binding.inflate(inflater, container, false)

        // Get the VM scoped to the activity we're attached to
        val maViewModel: MAViewModel by activityViewModels()

        // Get the button, attach a click listener
        binding.btSubmit.setOnClickListener{
            val email = binding.etEmail.text.toString()
            val pieces = email.split('@')
            if(pieces.size == 2 ){
                maViewModel.userName = pieces[0]
                maViewModel.domainName = pieces[1]
                maViewModel.buttonClicked(true)
            }
        }

        // Since we've already inflated, return the root
        return binding.root
    }
}