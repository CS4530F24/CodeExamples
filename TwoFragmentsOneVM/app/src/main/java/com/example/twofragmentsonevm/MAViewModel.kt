package com.example.twofragmentsonevm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MAViewModel: ViewModel() {

   private val buttonClick: MutableLiveData<Boolean>
            = MutableLiveData<Boolean>(false)

    var userName : String = ""
    var domainName : String = ""
    val obBtClick = buttonClick as LiveData<Boolean>

    fun buttonClicked(v: Boolean){
        buttonClick.value = v
    }
}