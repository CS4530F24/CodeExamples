package com.example.testingdemo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SomeViewModel : ViewModel() {
    private val _data: MutableLiveData<Double> = MutableLiveData(3.0)
    val data = _data as LiveData<Double>
    fun setDataClamped(newVal: Double){
        _data.value = if(newVal > 0) newVal else 0.0
    }
}