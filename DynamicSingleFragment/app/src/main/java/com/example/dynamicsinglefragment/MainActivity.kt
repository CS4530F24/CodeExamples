package com.example.dynamicsinglefragment

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.example.com.example.dynamicsinglefragment.R
import com.example.com.example.dynamicsinglefragment.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)}

    var isFragVisible: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.buttonAppearFragment.setOnClickListener{
            // If the button is clicked, make the fragment
            // appear
            // Modern approach, requires a fragment
            // gradle dependency
            if(!isFragVisible) {
                isFragVisible = true
                supportFragmentManager.commit {
                    replace<DynamicFragment>(R.id.dynamic_fragment, "DynamicFragment")
                    addToBackStack(null)
                    binding.buttonAppearFragment.text="Fragment off"
                }
            }
            else{
                // If the fragment is already on the screen
                // Take it off
                isFragVisible = false
                val dynamicFrag = supportFragmentManager.findFragmentByTag("DynamicFragment")
                if(dynamicFrag!=null) {
                    supportFragmentManager.commit{
                        remove(dynamicFrag)
                        binding.buttonAppearFragment.text="Fragment on"
                    }
                }
            }
            // Older approach to making fragment appear!
            // still works
//            val frag = DynamicFragment()
//            val fTrans = supportFragmentManager.beginTransaction()
//            fTrans.replace(R.id.dynamic_fragment,frag)
//            fTrans.addToBackStack(null)
//            fTrans.commit()

        }

        setContentView(binding.root)

    }
}