package com.example.customviewdemo

import android.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlin.random.Random

class SimpleViewModel :ViewModel() {

    //Model
    private val _color : MutableLiveData<Color> =
        MutableLiveData(Color.valueOf(1f, 1f, 0f))

    val color  = _color as LiveData<Color>

    fun pickColor(){
        with(Random.Default) {
            _color.value = Color.valueOf(nextFloat(), nextFloat(), nextFloat())
        }
    }
}