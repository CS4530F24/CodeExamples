package com.example.lightsensordemo

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lightsensordemo.ui.theme.LightSensorDemoTheme

class MainActivity : ComponentActivity() {
    private lateinit var mSensorManager: SensorManager
    private var mLight: Sensor? = null
    private val vm: VM by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Get sensor manager
        mSensorManager = getSystemService(SENSOR_SERVICE) as SensorManager

        //Get the default light sensor
        Log.e("NUMLIGHT SENSORS", "${mSensorManager.getSensorList(Sensor.TYPE_LIGHT).size}")
        mLight = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
        setContent{
            LightSensorDemoTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column {
                        val lightState by vm.lightString.observeAsState()
                        Text("Ambient light level in SI lux units:")
                        Text(lightState ?: "No Reading")
                    }
                }
            }
        }


    }

    private val mLightListener: SensorEventListener = object : SensorEventListener {
        override fun onSensorChanged(sensorEvent: SensorEvent) {

            //Get the illumination value
            val lightVal = sensorEvent.values[0]


            //Update the text view
            vm.lightString.value = "${lightVal.toString()} ${sensorEvent.sensor.name} ${sensorEvent.sensor.reportingMode}"

            //sensorEvent.sensor.reportingMode
        }

        override fun onAccuracyChanged(sensor: Sensor, i: Int) {
            Log.e("INFO", "Accuracy changed!")
        }
    }

    override fun onResume() {
        super.onResume()
        if (mLight != null) {
            mSensorManager.registerListener(
                mLightListener,
                mLight,
                SensorManager.SENSOR_DELAY_NORMAL
            )
        }
    }

    override fun onPause() {
        super.onPause()
        if (mLight != null) {
            mSensorManager.unregisterListener(mLightListener)
        }
    }
}

class VM : ViewModel(){
    val lightString = MutableLiveData<String>("")
}

