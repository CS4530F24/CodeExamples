package com.example.fragmentdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.FragmentManager
import com.example.fragmentdemo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        Log.e("BINDING", "${binding.textID}")
        val frag1 = Fragment1()
        frag1.setListener{
            val frag2 = Fragment2()
            frag2.setListener(){
                //like pressing back
                supportFragmentManager.popBackStack()
            }
            val fTrans = supportFragmentManager.beginTransaction()
            fTrans.replace(R.id.fragmentView, frag2)
            fTrans.addToBackStack(null)
            fTrans.commit()

        }
        setContentView(binding.root)

        val fTrans = supportFragmentManager.beginTransaction()
        fTrans.replace(R.id.fragmentView, frag1)
        fTrans.commit()

        Log.e("XML VALUE", "${resources.getBoolean(R.bool.isNarrowScreen)}")

    }




}