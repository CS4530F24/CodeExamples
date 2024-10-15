package com.example.httpdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.httpdemo.databinding.ActivityMainBinding
import io.ktor.client.*
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.http.ContentType.Application.Json
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class ISSPosition(val latitude: Double, val longitude: Double)

@Serializable
data class APIResponse(val timestamp: Long, val iss_position: ISSPosition)

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)

        val client = HttpClient(Android){
            install(ContentNegotiation){
                json(Json{
                    isLenient = true
                    ignoreUnknownKeys = true
                })
            }
        }

        binding.button.setOnClickListener {
            lifecycleScope.launch {
                try{
                    val response: APIResponse =
                        client.get("http://api.open-notify.org/iss-now.json").body()
                    withContext(Dispatchers.Main){
                        binding.textview.text =
                            "ISS position: Lat ${response.iss_position.latitude} Lon ${response.iss_position.longitude}"
                    }
                } catch(e: Exception){
                    Toast.makeText(this@MainActivity,
                        "Uh oh: ${e.message}",
                        Toast.LENGTH_SHORT).show();
                    return@launch
                }
                //MainScope().launch {  } would also work here

            }
        }


        setContentView(binding.root)
    }
}