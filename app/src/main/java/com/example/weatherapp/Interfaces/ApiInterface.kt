package com.example.weatherapp.Interfaces

import com.example.weatherapp.ModelClasses.WeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {
    @GET ("forecast")
   suspend fun getCurrentWeatherData(
    @Query("lat") latitude : String,
    @Query("lon") longitude : String,
    @Query("appid") appid :String,
    @Query("units") units : String ) : Response<WeatherResponse>
}