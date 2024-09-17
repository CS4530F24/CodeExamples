package com.example.storagedemo

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import com.example.storagedemo.databinding.ActivityMainBinding
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileInputStream
import java.io.PrintWriter
import java.nio.file.Files

//extension property, in this package, Context objects will now have a datastore property
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "preferenceFilename")

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val FILENAME = "myfile"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val preferences: Flow<Preferences> = dataStore.data
        val keyForData: Preferences.Key<Int> = intPreferencesKey("my_key")


        //periodically write to the data store
        //this coroutine scope is tied to the lifecycle of the activity
        //the task here will be cancelled when the activity is destroyed
        lifecycleScope.launch{
            for(i in 1..10){
                delay(2000)
                Log.e("iteration", i.toString())
                dataStore.edit {prefs : MutablePreferences -> //without this, parameter is named `it` by default
                    // "Elvis operator"  meaning "default value if the thing on the left is null"
                    val currentVal = prefs[keyForData] ?: 0
                    prefs[keyForData] = currentVal + 1
                }
            }
        }

        //collect() below is a suspend func, so we must run it in a coroutine
        //scope that understands how to handle it yielding/resuming
        lifecycleScope.launch {
            //watch for updates.  This is pretty similar to livedata.observe
            //preferences is a "flow", .collect() is like .observe()
            preferences.collect() {
                val currentVal = it[keyForData]?.toString() ?: "unknown"

                //We must perform UI updates in a particular thread.  This scope is pinned
                //to that thread, so we run the text update there
                MainScope().launch {
                    binding.text.text = "preferences value: $currentVal"
                }
            }
        }

        updateFileView()

        binding.deleteButton.setOnClickListener(){
            val filename = File(filesDir, FILENAME)
            Files.deleteIfExists(filename.toPath())
            updateFileView()
        }

        binding.saveButton.setOnClickListener(){
            val filename = File(filesDir, FILENAME)
            filename.writeText("This is some text that's saved in a file")
            updateFileView()
        }
        setContentView(binding.root)
    }

    //Could use  VM or live data to avoid manually calling this function in the 2 methods above
    private fun updateFileView(){
        //app specific files director.  Requires no extra permissions
        val filename = File(filesDir, FILENAME)
        binding.fileContents.text = if(filename.exists()){
            filename.readText()
        } else {
            "File doesn't exist"
        }
    }

}