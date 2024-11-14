package com.example.sensorlistdemo

import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.sensorlistdemo.ui.theme.SensorListDemoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        val sensors = sensorManager.getSensorList(Sensor.TYPE_ALL)

        setContent {
            SensorListDemoTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LazyColumn(
                        contentPadding = PaddingValues(vertical = 16.dp),
                    ) {
                        for (sensor in sensors) {
                            item {
                                Text(
                                    modifier = Modifier.padding(vertical = 8.dp),
                                    text = "${sensorTypeToString(sensor.type)}/Power:${sensor.power}" +
                                            "/Resolution: ${sensor.resolution}"
                                )
                            }
                        }
                    }
                }
            }
        }
    }


}

//Could (should) be a map probably
fun sensorTypeToString(sensorType: Int): String {
    return when (sensorType) {
        Sensor.TYPE_ACCELEROMETER -> "Accelerometer"
        Sensor.TYPE_AMBIENT_TEMPERATURE -> "Ambient Temperature"
        Sensor.TYPE_GAME_ROTATION_VECTOR -> "Game Rotation Vector"
        Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR -> "Geomagnetic Rotation Vector"
        Sensor.TYPE_GRAVITY -> "Gravity"
        Sensor.TYPE_GYROSCOPE -> "Gyroscope"
        Sensor.TYPE_GYROSCOPE_UNCALIBRATED -> "Gyroscope Uncalibrated"
        Sensor.TYPE_HEART_RATE -> "Heart Rate Monitor"
        Sensor.TYPE_LIGHT -> "Light"
        Sensor.TYPE_LINEAR_ACCELERATION -> "Linear Acceleration"
        Sensor.TYPE_MAGNETIC_FIELD -> "Magnetic Field"
        Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED -> "Magnetic Field Uncalibrated"
        Sensor.TYPE_PRESSURE -> "Orientation"
        Sensor.TYPE_PROXIMITY -> "Proximity"
        Sensor.TYPE_RELATIVE_HUMIDITY -> "Relative Humidity"
        Sensor.TYPE_ROTATION_VECTOR -> "Rotation Vector"
        Sensor.TYPE_SIGNIFICANT_MOTION -> "Significant Motion"
        Sensor.TYPE_STEP_COUNTER -> "Step Counter"
        Sensor.TYPE_STEP_DETECTOR -> "Step Detector"
        else -> "Unknown"
    }
}
