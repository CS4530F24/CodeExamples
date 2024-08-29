package com.example.helloworld

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    private lateinit var textView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //var button = findViewById<Button>(R.id.specialButton)

        findViewById<Button>(R.id.specialButton).setOnClickListener{
            Log.e("TEST", it.toString())
            textView = findViewById<TextView>(R.id.text)
            textView.text = "New text!!"
        }
    }
}