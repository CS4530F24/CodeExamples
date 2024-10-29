package com.example.mixedcompose

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class TheViewModel : ViewModel() {

    /*
    For a few reasons, current guidance is to use Flows instead of LiveData
    A MutableStateFlow is very similar to MutableLiveData
     */
    private var count = MutableStateFlow(0)

    //the public read only value that the view can observe
    // typically with .collectAsState()
    val countFlow = count.asStateFlow()

    fun incrementCount(){
        //update takes a function which receives the old value as a parameter
        //and returns the new value
        count.update {
            it + 1
        }
    }
}