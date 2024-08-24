package com.example.weatherapp.MVVM

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class WeatherFactoryViewModel(private val repository: WeatherRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return WeatherViewModel(repository) as T
    }
}