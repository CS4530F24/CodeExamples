package com.example.emailsplitter2activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.emailsplitter2activities.databinding.ActivityMainBinding
import com.example.emailsplitter2activities.databinding.ActivitySplitDisplayBinding

class SplitDisplay : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivitySplitDisplayBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val args = intent.extras!!
        val username = args.getString("username")
        val domain = args.getString("domain")
        binding.userView.text = username
        binding.domainView.text = domain

    }
}