package com.example.twofragmentsonevm

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.lifecycle.ViewModel
import com.example.twofragmentsonevm.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        // We'll have the activity grab the
        // viewmodel first so all fragments
        // talking to it see it
        val myViewModel : MAViewModel by viewModels()

        // In this example, the activity
        // just gets the fragments on the screen
        // They do all the work and talk to each other
        // via the viewmodel
        supportFragmentManager.commit{
            replace<Fragment1>(R.id.etFCV)
        }

        myViewModel.obBtClick.observe(this){ click->
            if(click){
                supportFragmentManager.commit{
                    replace<Fragment2>(R.id.dispFCV)
                }
            }
        }

        setContentView(binding.root)
    }
}
