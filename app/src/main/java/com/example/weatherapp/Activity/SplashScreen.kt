package com.example.weatherapp.Activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.weatherapp.Interfaces.ApiInterface
import com.example.weatherapp.MVVM.RetrofitHelper
import com.example.weatherapp.MVVM.WeatherFactoryViewModel
import com.example.weatherapp.MVVM.WeatherRepository
import com.example.weatherapp.MVVM.WeatherViewModel
import com.example.weatherapp.databinding.ActivitySplashScreenBinding
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch



class SplashScreen : AppCompatActivity() {
    private val binding by lazy { ActivitySplashScreenBinding.inflate(layoutInflater) }
    private lateinit var weatherViewModel: WeatherViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val apiInterface = RetrofitHelper.getInstance().create(ApiInterface::class.java)
        val repository = WeatherRepository(apiInterface)
        weatherViewModel = ViewModelProvider(this, WeatherFactoryViewModel(repository)).get(WeatherViewModel::class.java)



        GlobalScope.launch {
            delay(5000) // Delay for 5 seconds
            val openMainActivity = Intent(
                this@SplashScreen,
                MainActivity::class.java
            )
            startActivity(openMainActivity)
            finish()
        }

    }
}