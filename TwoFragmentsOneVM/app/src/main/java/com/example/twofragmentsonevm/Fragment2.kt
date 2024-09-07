package com.example.twofragmentsonevm

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.twofragmentsonevm.databinding.Fragment1Binding
import com.example.twofragmentsonevm.databinding.Fragment2Binding

/**
 * A simple [Fragment] subclass.
 * Use the [Fragment2.newInstance] factory method to
 * create an instance of this fragment.
 */
class Fragment2 : Fragment() {

    private lateinit var binding : Fragment2Binding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Grab the fragment view binding
        binding =  Fragment2Binding.inflate(inflater, container, false)

        // Get the VM scoped to the activity we're attached to
        val maViewModel: MAViewModel by activityViewModels()

        // We don't even need an observer.
        // This fragment is dynamically created by a button click
        // If we got here, we know the text fields are populated
        binding.tvUserName.text = maViewModel.userName
        binding.tvDomainName.text = maViewModel.domainName

        // Inflate the layout for this fragment
        return binding.root
    }
}