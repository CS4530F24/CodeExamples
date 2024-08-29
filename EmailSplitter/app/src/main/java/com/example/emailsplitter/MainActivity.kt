package com.example.emailsplitter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    private lateinit var textInput : EditText
    private lateinit var usernameView: TextView
    private lateinit var domainView: TextView

    private val username_key = "username_key"
    private val domain_key = "domain_key"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e("Lifecycle", "OnCreate $this")
        setContentView(R.layout.activity_main)

        textInput = findViewById(R.id.emailInput)
        usernameView = findViewById(R.id.usernameView)
        domainView = findViewById(R.id.domainView)


        //findViewById<Button>(R.id.splitButton).setOnClickListener(myMemberVar)

        findViewById<Button>(R.id.splitButton).setOnClickListener {
//            val email = textInput.text.toString()
//            val pieces = email.split('@')
//            if (pieces.size != 2 || pieces.any(String::isEmpty)) {
//                Toast.makeText(this, "Invalid email!", Toast.LENGTH_SHORT).show()
//            } else {
//                usernameView.text = pieces[0]
//                domainView.text = pieces[1]
//            }

            splitEmail(textInput.text.toString()).onSuccess {
                usernameView.text = it[0]
                domainView.text = it[1]
            }.onFailure {
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            }


        }
    }


    override fun onPause() {
        super.onPause()
        Log.e("Lifecycle", "onPause $this")

    }

    override fun onResume(){
        super.onResume()
        Log.e("Lifecycle", "onResume $this")
    }

    override fun onStop() {
        super.onStop()
        Log.e("Lifecycle", "onStop $this")
    }

    override fun onStart() {
        super.onStart()
        Log.e("Lifecycle", "onStart $this")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e("Lifecycle", "onDestroy $this")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.e("Lifecycle", "onSaveInstanceState $this")
        //todo add sleep

        outState.putString(username_key, usernameView.text.toString())
        Thread.sleep(5000)
        outState.putString(domain_key, domainView.text.toString())


    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        Log.e("Lifecycle", "onRestoreinstanceState $this")
        usernameView.text = savedInstanceState.getString(username_key)
        domainView.text = savedInstanceState.getString(domain_key)
    }
    class myClickListenerClass : View.OnClickListener{
        override fun onClick(p0: View?) {
            TODO("Not yet implemented")
        }

    }
    val myMemberVar = myClickListenerClass()

    val myClickListener = View.OnClickListener {

    }


}