package com.example.weatherapp.MVVM

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.weatherapp.Interfaces.ApiInterface
import com.example.weatherapp.ModelClasses.WeatherResponse


class WeatherRepository(private val apiInterface: ApiInterface) {

    private val weatherLiveData = MutableLiveData<WeatherResponse>()

    val weatherData : LiveData<WeatherResponse>
        get() = weatherLiveData

    suspend fun getCurrentWeatherData(lat : String , long : String , appID : String , units : String){
        val result = apiInterface.getCurrentWeatherData(lat , long, appID, units )
        if (result.isSuccessful && result.body()!==null){
            weatherLiveData.postValue(result.body())
        }
    }

}