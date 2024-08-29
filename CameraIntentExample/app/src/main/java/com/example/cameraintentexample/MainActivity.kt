package com.example.cameraintentexample

import android.content.ActivityNotFoundException
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.provider.MediaStore
import android.graphics.Bitmap
import android.util.Log
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import com.example.cameraintentexample.databinding.ActivityMainBinding

//Implement View.onClickListener to listen to button clicks. This means we have to override onClick().
class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding : ActivityMainBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //Say that this class itself contains the listener.
        binding.buttonSubmit.setOnClickListener(this)
        binding.buttonPic.setOnClickListener(this)
    }

    //Handle clicks for ALL buttons here
    @RequiresApi(34)
    override fun onClick(view: View) {
        when (view.id) {
            R.id.button_submit -> {

                //First, get the string from the EditText
                var fullName =binding.etName.text.toString()

                //Check if the EditText string is empty
                if (fullName.isBlank()) {
                    //Complain that there's no text
                    Toast.makeText(this, "Enter a name first!", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    //Reward them for submitting their names
                    Toast.makeText(this@MainActivity, "Good job!", Toast.LENGTH_SHORT).show()

                    //Remove any leading spaces or tabs
                    fullName = fullName.replace("^\\s+".toRegex(), "")

                    //Separate the string into first and last name using simple Java stuff
                    val splitStrings = fullName.split("\\s+".toRegex()).toTypedArray()
                    when (splitStrings.size) {
                        1 -> {
                            Toast.makeText(
                                this@MainActivity,
                                "Enter both first and last name!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        2 -> {
                            val firstName = splitStrings[0]
                            val lastName = splitStrings[1]

                            //Set the text views
                            binding.tvFnData.text = firstName
                            binding.tvLnData.text = lastName
                        }
                        else -> {
                            Toast.makeText(
                                this@MainActivity,
                                "Enter only first and last name!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
            R.id.button_pic -> {

                //The button press should open a camera
                val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                try{
                    cameraActivity.launch(cameraIntent)
                }catch(ex:ActivityNotFoundException){
                    //Do error handling here
                }
            }
        }
    }
    private val cameraActivity = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
        if(result.resultCode == RESULT_OK) {
            val extras = result.data!!.extras

            //old version which is not typesafe extras["data"] as Bitmap
            val thumbnailImage: Bitmap?  = extras!!.getParcelable("data", Bitmap::class.java)
            binding.ivPic.setImageBitmap(thumbnailImage)
        } else {
            Log.e("CAMERA APP", "no result")
        }
    }
}