package com.example.weatherdemo

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


class WeatherViewModel(private val repository: WeatherRepository) : ViewModel() {

    val currentWeather: LiveData<WeatherData> = repository.currentWeather
    val allWeather: LiveData<List<WeatherData>> = repository.allWeather

    fun checkWeather(city: String){
        Log.e("VM", "Checking weather $city")
        repository.checkWeather(city)
    }

}

// This factory class allows us to define custom constructors for the view model
class WeatherViewModelFactory(private val repository: WeatherRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WeatherViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return WeatherViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}