package com.example.mixedcompose

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.fragment.findNavController
import com.example.mixedcompose.databinding.FragmentComposable1Binding



class ComposableFragment1 : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val binding = FragmentComposable1Binding.inflate(layoutInflater)

        //ComposeView gives us a `Composable` context to run functions in
        binding.composeView1.setContent {
            MyComposable(Modifier.padding(16.dp)){
                findNavController().navigate(R.id.action_composableFragment1_to_fragment2)
            }
        }

        return binding.root
    }


}

@Composable
fun MyComposable(modifier: Modifier = Modifier,
                 //typically "screen level" composables that need a VM
                 //take it as a parameter with a default value
                 //the viewModel function here works like `by activityViewModels()`
                 viewModel: TheViewModel = viewModel(
                     viewModelStoreOwner = LocalContext.current.findActivity()
                 ),
                 //take the click handler as a parameter to make this more reusable
                 onClick: ()->Unit){
    Column(modifier = modifier.padding(32.dp) ) {
        //currentCount can be used like an int and when the flow/state
        //changes it will trigger recomposition of everything that
        //depends on currentCount
        val currentCount by viewModel.countFlow.collectAsState()
        SayHello(currentCount)
        Button(onClick = onClick) {
            Text("Click this button!")
        }
    }
}

@Composable
fun SayHello(currentCount: Int){
    Text("Hello!: $currentCount")
}

@Preview
@Composable
fun previewHello(){
    SayHello(5)
}