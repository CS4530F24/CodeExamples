package com.example.weatherdemo

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import com.example.weatherdemo.ui.theme.WeatherDemoTheme
import java.util.Date

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val vm: WeatherViewModel by viewModels{
            WeatherViewModelFactory((application as WeatherApplication).weatherRepository)}

        setContent {
            WeatherDemoTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val currentWeather by vm.currentWeather.observeAsState()

                    Column {

                        SearchArea{
                            vm.checkWeather(it)
                        }
                        WeatherDataDisplay(currentWeather)



                        Spacer(modifier = Modifier.padding(32.dp))

                        Text("Previous Weather Data",
                            fontSize = 12.em,
                            lineHeight = 1.em)

                        Spacer(modifier = Modifier.padding(16.dp))

                        val allWeather by vm.allWeather.observeAsState()
                        LazyColumn(verticalArrangement = Arrangement.spacedBy(4.dp),){
                            for(data in allWeather ?: listOf()){
                                item{
                                    WeatherDataDisplay(data = data)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun WeatherDataDisplay(data: WeatherData?, modifier: Modifier = Modifier) {
    Surface(color=MaterialTheme.colorScheme.surface) {
        Text(
            text = if (data != null) "Temp at ${data.timestamp} in ${data.city} is ${data.temp}!" else "NO WEATHER DATA YET",
            modifier = modifier
        )
    }
}

@Composable
fun SearchArea(clickCallback: (city: String) -> Unit){
    Row(modifier = Modifier
        .padding(16.dp)
        .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        var city: String by remember { mutableStateOf("default city") }
        OutlinedTextField(
            modifier = Modifier
                .padding(16.dp)
                .weight(4f),
            value = city,
            onValueChange = { city = it },
            label = { Text("City Name") }

        )
        Button(onClick = { clickCallback(city) }) {
            Text(text ="Get Weather")
        }
    }

}

@Preview(showBackground = false)
@Composable
fun SearchAreaPreview(){
    WeatherDemoTheme {
        SearchArea(clickCallback = {})
    }
}

@Preview(showBackground = true)
@Composable
fun WeatherDataDisplayPreview() {
    WeatherDemoTheme {
        WeatherDataDisplay(WeatherData(Date(), "Salt Lake", 95.0 ))
    }
}