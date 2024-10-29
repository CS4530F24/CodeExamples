package com.example.mixedcompose

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.fragment.findNavController
import com.example.mixedcompose.databinding.Fragment2Binding


class Fragment2 : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = Fragment2Binding.inflate(layoutInflater)

        val viewModel: TheViewModel by activityViewModels()

        binding.composeView2.setContent {
            AnotherComposable(viewModel = viewModel) {
                viewModel.incrementCount()
                findNavController().popBackStack()
            }
        }
        return binding.root
    }


}


@Composable
fun AnotherComposable(
    modifier: Modifier = Modifier,
    viewModel: TheViewModel = viewModel(
        viewModelStoreOwner = LocalContext.current.findActivity()
    ),
    onClick: () -> Unit
) {
    Column {
        val currentCount by viewModel.countFlow.collectAsState()
        Text("Fragment 2!: $currentCount")
        Button(onClick = onClick) {
            Text("Click Me")
        }
    }
}