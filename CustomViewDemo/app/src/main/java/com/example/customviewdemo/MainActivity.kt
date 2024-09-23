package com.example.customviewdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.example.customviewdemo.databinding.ActivityMainBinding
import kotlin.reflect.typeOf

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)

        val child = binding.fragmentContainerView.getFragment<Fragment>()
        when(child) {
            is ClickFragment -> child.setButtonFunction {
                //val drawFragment = DrawFragment()
                supportFragmentManager.commit{
                    replace<DrawFragment>(R.id.fragmentContainerView, "draw_tag")
                    addToBackStack(null)
                }
            }
            is DrawFragment -> {
                var here :Int = 1
            }
        }
        setContentView(binding.root)
    }
}