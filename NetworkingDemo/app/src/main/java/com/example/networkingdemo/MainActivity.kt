package com.example.networkingdemo

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.example.networkingdemo.ui.theme.NetworkingDemoTheme
import com.google.gson.Gson
import com.google.gson.stream.JsonReader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.Date


class MainActivity : ComponentActivity() {

    private var mDownloadId: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //set up to receive notifications from download manager
        registerReceiver(onComplete, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE),
            RECEIVER_NOT_EXPORTED)

        setContent {
            NetworkingDemoTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column {


                        val vm: ISSViewModel by viewModels()
                        val scope = rememberCoroutineScope()
                        var job: Job? by remember { mutableStateOf<Job?>((null)) }
                        //val job: State<Job?> = remember {Job()}
                        Row {
                            Button(onClick = {
                                job = scope.launch {
                                    delay(500) //give me the chance to cancel
                                    val response = getISSLocationCoroutine()
                                    vm.addData(response)
                                }
                            }) {
                                Text("Get New Data")
                            }

                            Button(onClick = {
                                job?.cancel()
                            }) {
                                Text("Cancel last request")
                            }
                        }
                        Row {
                            Button(onClick = {
                                getRequestVolley(VolleySingleton.getInstance(this@MainActivity.applicationContext).requestQueue) {
                                    vm.addData(Gson().fromJson(it, ISSResult::class.java))
                                }
                            }) {
                                Text("Get New Data With Volley")
                            }
                        }

                        Row {
                            Button(onClick = {
                                downloadPictureWithDownloadManager()
                            }) {
                                Text("Download picture with Download Manager")
                            }
                        }

                        val entries = vm.data.observeAsState()
                        LazyColumn {
                            for (entry in (entries.value ?: listOf()).asReversed()) {
                                item {
                                    Text(
                                        "Lat: ${entry.iss_position.latitude} Lon: ${entry.iss_position.longitude} Time:" +
                                                "${Date(1000 * entry.timestamp)}"
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun downloadPictureWithDownloadManager() {
        //Get the string for URL
        val url = "https://i.imgur.com/6cjobaJ.jpeg"

        //DownloadManager wants a Uri object (not URI or URL)
        val downloadUri = Uri.parse(url)

        //Create the DownloadManager request
        val downloadRequest = DownloadManager.Request(downloadUri)

        //Decide whether we can download over wifi or data or both
        downloadRequest.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)

        //Set a nice title and description
        downloadRequest.setTitle("Image Download")
        downloadRequest.setDescription("Download image from $url")

        //Say where to save stuff
        downloadRequest.setDestinationInExternalPublicDir(
            Environment.DIRECTORY_DOWNLOADS,
            "DownloadedImage.jpg"
        )

        //Put this request in the queue
        mDownloadId =
            (getSystemService(DOWNLOAD_SERVICE) as DownloadManager).enqueue(downloadRequest)
    }

    //Create our BroadcastReceiver to receive the system broadcast
    private var onComplete: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            if (downloadId == mDownloadId) {
                Toast.makeText(context, "File downloaded!", Toast.LENGTH_LONG).show()
            }
        }
    }
}


data class ISSPosition(var latitude: Double, var longitude: Double)
data class ISSResult(var message: String, val timestamp: Long, var iss_position: ISSPosition)

class ISSViewModel : ViewModel() {
    private val _data = MutableLiveData<List<ISSResult>>(listOf())
    val data = _data as LiveData<List<ISSResult>>
    fun addData(newVal: ISSResult) {
        _data.value = _data.value?.plus(newVal)
        Log.e("UPDATE", "${data.value?.size}")
    }
}

suspend fun getISSLocationCoroutine(): ISSResult {
    return withContext(Dispatchers.IO) {

        val url: Uri = Uri.Builder().scheme("http")
            .authority("api.open-notify.org")
            .appendPath("iss-now.json").build()

        val conn = URL(url.toString()).openConnection() as HttpURLConnection
        conn.connect()
        val gson = Gson()
        val result = gson.fromJson(
            InputStreamReader(conn.inputStream, "UTF-8"),
            ISSResult::class.java
        )
        Log.e("data!", gson.toJson(result).toString())
        result
    }
}

fun getRequestVolley(rq: RequestQueue, callback: (response: String) -> Unit) {
    val url: Uri = Uri.Builder().scheme("http")
        .authority("api.open-notify.org")
        .appendPath("iss-now.json").build()
    val stringRequest = StringRequest(Request.Method.GET, url.toString(),
        callback,
        {
            Log.e("VOLLEY FAILURE", "uh oh")
        }
    )
    rq.add(stringRequest)
}