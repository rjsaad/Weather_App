package com.example.weatherapp.MVVM

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.ModelClasses.WeatherResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WeatherViewModel(private val weatherRepository: WeatherRepository) : ViewModel(){

        fun fetchWeatherData(lat: String, long: String, appID: String, units: String) {
            viewModelScope.launch(Dispatchers.IO) {
                weatherRepository.getCurrentWeatherData(lat, long, appID, units)
            }
        }


    val weatherData :LiveData<WeatherResponse>
        get() = weatherRepository.weatherData
}