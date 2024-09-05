package com.example.mvvmwithrecyclerview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.mvvmwithrecyclerview.databinding.ActivityMainBinding
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager

class MainActivity : AppCompatActivity() {


    // Only compute on first access, lazy init
    val binding: ActivityMainBinding by lazy {ActivityMainBinding.inflate(layoutInflater)}

    // the recyclerview
    val recycler by lazy{ binding.recycler }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //binding =
        Log.e("SCREEN", "Screen wide? ${resources.getBoolean(R.bool.isScreenWide)}")


        //"DELEGATED PROPERTY".  Access to myViewModel calls viewModels.getValue which manages lifecycle stuff
        val myViewModel : TodoViewModel by viewModels()

        // Use with so you don't have to repeatedly
        // say recycler.something everytime you use its
        // properties
        with(recycler){
            layoutManager = LinearLayoutManager(this@MainActivity) //NEW KOTLIN THING
            adapter = TodoListAdapter(listOf()){
                myViewModel.removeItem(it)
            }
        }

        myViewModel.observableList.observe(this) {
            (recycler.adapter as TodoListAdapter).updateList(it)
            Log.e("LIST SIZE", "${it.size}")

            binding.numberOfItems.text = "${it.size} item${if(it.size == 1) "" else "s"}"
        }

        binding.newItemButton.setOnClickListener{
            val text = binding.newItemText.text.toString()
            if(text.isEmpty()){
                return@setOnClickListener //NEW KOTLIN THING
            }
            myViewModel.newItem(text)
            binding.newItemText.text.clear()

        }

        setContentView(binding.root)
    }
}