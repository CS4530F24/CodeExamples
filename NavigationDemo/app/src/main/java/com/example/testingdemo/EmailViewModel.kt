package com.example.testingdemo

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class EmailViewModel : ViewModel() {


    private val _username = MutableLiveData("")
    val username  = _username as LiveData<String>
    private val _domain = MutableLiveData("")
    val domain = _domain as LiveData<String>

    fun setEmail(email: String, onSuccess: ()-> Unit, onFail: () -> Unit){
        val words = email.split("@")
        if(words.size == 2){
            _username.value = words[0]
            _domain.value = words[1]
            Log.e("viewmodel", "success")
            onSuccess()
        } else {
            Log.e("viewmodel", "failure")
            onFail()
        }
    }


}