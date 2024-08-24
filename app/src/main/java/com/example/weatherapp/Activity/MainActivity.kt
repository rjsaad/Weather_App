package com.example.weatherapp.Activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.Adapters.WeatherAdapter
import com.example.weatherapp.Interfaces.ApiInterface
import com.example.weatherapp.MVVM.RetrofitHelper
import com.example.weatherapp.MVVM.WeatherFactoryViewModel
import com.example.weatherapp.MVVM.WeatherRepository
import com.example.weatherapp.MVVM.WeatherViewModel
import com.example.weatherapp.ModelClasses.WeatherResponse
import com.example.weatherapp.R
import com.example.weatherapp.databinding.ActivityMainBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private val LOCATION_PERMISSION_REQUEST_CODE = 1
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var weatherViewModel: WeatherViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val apiInterface = RetrofitHelper.getInstance().create(ApiInterface::class.java)
        val respository = WeatherRepository(apiInterface)
        weatherViewModel = ViewModelProvider(this , WeatherFactoryViewModel(respository)).get(WeatherViewModel::class.java)

        weatherViewModel.weatherData.observe(this) { weatherResponse ->
            weatherResponse?.let {
                updateUI(it)
            }
        }
        initViews()
        enableLocation()
    }

    private fun fetchWeatherData(latitude: String, longitude: String) {
        // Use the ViewModel to fetch weather data
        weatherViewModel.fetchWeatherData(latitude, longitude, "19b294cdfd3cf8e9cf3c506e3998355c", "metric")
    }



    private fun changeBackground(condition: String) {
        when {
            condition.contains("cloud", ignoreCase = true) -> {
                binding.mainLayout.setBackgroundResource(R.drawable.colud_background)
                binding.lottieAnimationView.setAnimation(R.raw.cloud)
            }

            condition.contains("sunny", ignoreCase = true) -> {
                binding.mainLayout.setBackgroundResource(R.drawable.sunny_background)
                binding.lottieAnimationView.setAnimation(R.raw.sun)
            }

            condition.contains("rain", ignoreCase = true) -> {
                binding.mainLayout.setBackgroundResource(R.drawable.rain_background)
                binding.lottieAnimationView.setAnimation(R.raw.rain)
            }

            condition.contains("snow", ignoreCase = true) -> {
                binding.mainLayout.setBackgroundResource(R.drawable.snow_background)
                binding.lottieAnimationView.setAnimation(R.raw.snow)
            }
            // Add more conditions as needed
            else -> {
                binding.mainLayout.setBackgroundResource(R.drawable.sunny_background)
                binding.lottieAnimationView.setAnimation(R.raw.sun)
            }
        }
    }

    private fun date(): String {
        val sdf = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        return sdf.format(Date())
    }

    fun dayName(timeStrap: Long): String {
        val timeStamp = SimpleDateFormat("EEEE", Locale.ENGLISH).format(Calendar.getInstance().time)
        return timeStamp.format(Date(timeStrap * 1000)) // Convert from Unix time
    }

    private fun initViews() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    }

    @SuppressLint("SetTextI18n")
    private fun updateUI(responseBody: WeatherResponse) {
        binding.lottieAnim.visibility = View.GONE
        binding.mainLayout.visibility = View.VISIBLE

        val temperature = responseBody.list[0].main.temp
        val minTemp = responseBody.list[0].main.temp_min
        val maxTemp = responseBody.list[0].main.temp_max
        val humidity = responseBody.list[0].main.humidity
        val condition = responseBody.list[0].weather[0].description

        binding.txtWindSpeed.text = "${responseBody.list[0].wind.speed} m/s"
        binding.txtSea.text = "${responseBody.list[0].main.sea_level} hPa"
        binding.txtSunrise.text = "${responseBody.list[0].main.pressure} hPa"
        binding.txtSunset.text = "${responseBody.list[0].main.feels_like} °C"
        binding.txtRain.text = "${responseBody.list[0].rain?.threeHours ?: 0}%"

        binding.txtTemp.text = temperature.toString()
        binding.txtMinTemp.text = minTemp.toString()
        binding.txtMaxTemp.text = maxTemp.toString()
        binding.txtHumidity.text = "$humidity%"
        binding.txtWeather.text = condition
        binding.txtDate.text = date()
        binding.txtWeekDay.text = dayName(System.currentTimeMillis())

        // Change the background based on weather condition
        changeBackground(condition)

        binding.hourlyWeatherRv.adapter = WeatherAdapter(this, responseBody.list)
    }

    private fun enableLocation() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        } else {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                location?.let {
                    val latitude = it.latitude.toString()
                    val longitude = it.longitude.toString()

                    // Fetch the city name using Geocoder
                    val geocoder = Geocoder(this, Locale.getDefault())
                    val addresses = geocoder.getFromLocation(it.latitude, it.longitude, 1)
                    val cityName = addresses?.get(0)?.locality ?: "Unknown"
                    binding.txtCity.text = cityName

                    fetchWeatherData(latitude, longitude)
                    Toast.makeText(this, "$latitude $longitude", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                enableLocation()
            } else {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    //    private fun fetchWeatherData(latitude: String, longitude: String) {
//        val retrofit = Retrofit.Builder()
//            .baseUrl("https://api.openweathermap.org/data/2.5/")
//            .addConverterFactory(GsonConverterFactory.create())
//            .build().create(ApiInterface::class.java)
//
//        binding.mainLayout.visibility = View.GONE
//        binding.lottieAnim.visibility = View.VISIBLE
//
//        val response = retrofit.getCurrentWeatherData(
//            latitude,
//            longitude,
//            "19b294cdfd3cf8e9cf3c506e3998355c",
//            "metric"
//        )
//
//        response.enqueue(object : Callback<WeatherResponse> {
//            @SuppressLint("SetTextI18n")
//            override fun onResponse(
//                call: Call<WeatherResponse>,
//                response: Response<WeatherResponse>
//            ) {
//                if (response.isSuccessful) {
//                    binding.lottieAnim.visibility = View.GONE
//                    binding.mainLayout.visibility = View.VISIBLE
//                    val responseBody = response.body()
//                    responseBody?.let {
//                        val temperature = responseBody.list[0].main.temp
//                        val minTemp = responseBody.list[0].main.temp_min
//                        val maxTemp = responseBody.list[0].main.temp_max
//                        val humidity = responseBody.list[0].main.humidity
//                        val condition = responseBody.list[0].weather[0].description
//
//                        binding.txtWindSpeed.text =
//                            responseBody.list[0].wind.speed.toString() + "m/s"
//                        binding.txtSea.text = responseBody.list[0].main.sea_level.toString() + "hPa"
//                        binding.txtSunrise.text =
//                            responseBody.list[0].main.pressure.toString() + "hPa"
//                        binding.txtSunset.text =
//                            responseBody.list[0].main.feels_like.toString() + "°C"
//                        binding.txtRain.text =
//                            responseBody.list[0].rain?.threeHours.toString() + "%"
//
//                        binding.txtTemp.text = temperature.toString()
//                        binding.txtMinTemp.text = minTemp.toString()
//                        binding.txtMaxTemp.text = maxTemp.toString()
//                        binding.txtHumidity.text = "$humidity%"
//                        binding.txtWeather.text = condition
//                        binding.txtDate.text = date()
//                        binding.txtWeekDay.text = dayName(System.currentTimeMillis())
//
//                        // Change the background based on weather condition
//                        changeBackground(condition)
//
//                        binding.hourlyWeatherRv.adapter =
//                            WeatherAdapter(this@MainActivity, responseBody.list)
//                    }
//                }
//            }
//
//            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
//                Log.e("Tag", "Failure: ${t.message}")
//            }
//        })
//    }
}
