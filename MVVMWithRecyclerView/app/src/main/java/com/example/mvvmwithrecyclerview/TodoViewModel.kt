package com.example.mvvmwithrecyclerview

import android.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlin.random.Random

data class TodoItem(val text: String, val color: Color)


class TodoViewModel : ViewModel() {
    private val rand = Random.Default //just a normal private class member

    //MODEL part of MVVM
    //Simple enough here to just store it as a member of our VM, won't be the case in general
    private val actualList = MutableLiveData(
        mutableListOf(TodoItem("initial item", Color.valueOf(0f,0f,0.5f))))

    //Part of the VM that the view see
    val observableList = actualList as LiveData<out List<TodoItem>>

    //pick a random color
    fun newItem(item: String){
        actualList.value!!.add(TodoItem(item,
            Color.valueOf(rand.nextFloat(),rand.nextFloat(),rand.nextFloat())))
        actualList.value = actualList.value //force observers to fire
    }

    fun removeItem(item: TodoItem){
        actualList.value!!.remove(item)
        actualList.value = actualList.value
    }
}