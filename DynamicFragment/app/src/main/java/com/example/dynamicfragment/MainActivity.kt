package com.example.dynamicfragment

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.example.dynamicfragment.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var isFragmentVisible: Boolean = false
        val binding: ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        binding.buttonAppearFragment.setOnClickListener{

            if(!isFragmentVisible) {
                // Mark fragment as visible
                isFragmentVisible = true

                // Replace fragmentContainerView with a fragment
                supportFragmentManager.commit {
                    replace<Dynamic>(R.id.dynamicFragContainerView,"DynamicFragment")
                    binding.buttonAppearFragment.text = "Fragment off"
                }
            }
            else{
                isFragmentVisible = false
                val dynamicFrag = supportFragmentManager.findFragmentByTag("DynamicFragment")
                if(dynamicFrag!=null){
                    supportFragmentManager.commit{
                        remove(dynamicFrag)
                        binding.buttonAppearFragment.text = "Fragment on"
                    }
                }
            }
        }
        setContentView(binding.root)
    }
}