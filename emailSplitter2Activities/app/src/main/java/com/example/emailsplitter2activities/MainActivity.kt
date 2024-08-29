package com.example.emailsplitter2activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.emailsplitter2activities.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        binding.button.setOnClickListener{
            val email = binding.emailInput.text.toString()
            val pieces = email.split('@')
            if(pieces.size != 2 || pieces.any(String::isEmpty)){
                Toast.makeText(this, "Invalid email!", Toast.LENGTH_SHORT).show()
                val thisIntent = intent;
                Log.e("intent", "$thisIntent")
            } else {
                val intent = Intent(this, SplitDisplay::class.java)
                val argsBundle = Bundle()
                argsBundle.putString("username", pieces[0])
                argsBundle.putString("domain", pieces[1])
                intent.putExtras(argsBundle)
                startActivity(intent)
            }
        }
        setContentView(binding.root)
    }
}