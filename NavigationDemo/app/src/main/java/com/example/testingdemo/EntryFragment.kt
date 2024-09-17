package com.example.testingdemo

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import androidx.navigation.fragment.findNavController
import com.example.testingdemo.databinding.FragmentEntryBinding

class EntryFragment : Fragment() {


    private val viewModel: EmailViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentEntryBinding.inflate(layoutInflater, container, false)

        binding.submit.setOnClickListener{
            viewModel.setEmail(binding.emailEntry.text.toString(), {
                Log.e("entry frag", "navigating")
                findNavController().navigate(R.id.action_email_submitted)
            }, {
                Toast.makeText(requireContext(), "email is bad", Toast.LENGTH_SHORT).show()
            })
        }
        Log.e("entry fragment", "here it is")
        return binding.root
    }



}