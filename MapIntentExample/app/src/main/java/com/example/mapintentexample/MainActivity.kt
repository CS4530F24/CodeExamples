package com.example.mapintentexample

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.mapintentexample.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //this class implements the onClickListener
        binding.buttonSubmit.setOnClickListener(this)


    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.button_submit -> {

                //Get the string from the EditText
                val searchString = binding.etSearch.text.toString()
                if (searchString.isBlank()) {
                    Toast.makeText(this, "Enter something!", Toast.LENGTH_SHORT).show()
                } else {
                    //We have to grab the search term and construct a URI object from it.
                    //We'll hardcode WEB's location here
                    val searchUri = Uri.parse("geo:40.767778,-111.845205?q=$searchString")

                    //Create the implicit intent
                    val mapIntent = Intent(Intent.ACTION_VIEW, searchUri)

                    //If there's an activity associated with this intent, launch it
                    try{
                        startActivity(mapIntent)
                    }catch(ex: ActivityNotFoundException){
                        //handle errors here
                        Toast.makeText(this, "Womp womp", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
}