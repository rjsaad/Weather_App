package com.example.weatherapp.Adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weatherapp.ModelClasses.WeatherData
import com.example.weatherapp.databinding.WeatherItemDesingBinding
import java.text.SimpleDateFormat
import java.util.Locale

class WeatherAdapter(private val context: Context, private val weatherList: List<WeatherData>) :
    RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        val binding = WeatherItemDesingBinding.inflate(LayoutInflater.from(context), parent, false)
        return WeatherViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        holder.bind(weatherList[position], position)
    }

    override fun getItemCount(): Int {
        return weatherList.size
    }

    inner class WeatherViewHolder(private val binding: WeatherItemDesingBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(weatherData: WeatherData, position: Int) {
            val timeString = getTimeFromDateString(weatherData.dt_txt)
            binding.txtHourlyTime.text = timeString
            binding.txtHourlyTemp.text = weatherData.main.temp.toString()

            Log.d("WeatherAdapter", "Loading icon from URL: ${weatherList[position].weather[0].icon}.png")
            Glide.with(context)
                .load("https://openweathermap.org/img/w/${weatherList[position].weather[0].icon}.png")
                .into(binding.weatherIcon)

        }
        private fun getTimeFromDateString(dateString: String): String {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)
            val outputFormat = SimpleDateFormat("HH:mm", Locale.US)
            val date = inputFormat.parse(dateString)
            return outputFormat.format(date)
        }
    }
}
