package com.example.weatherapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.model.api.WeatherApi
import com.example.weatherapp.model.entity.WeatherForView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDateTime

class MainViewModel : ViewModel() {

    private var weatherForViewMutable = MutableLiveData<WeatherForView>()
    val weatherForView: LiveData<WeatherForView> = weatherForViewMutable

    fun getWeather() {
        viewModelScope.launch(Dispatchers.IO) {
            val response = WeatherApi.weatherApiClient.get5DayWeather()
            if (response.isSuccessful && response.body() != null) {
                val weatherResponse = response.body()!!

                val currentDate = LocalDateTime.now()
                val currentDay = weatherResponse.list[0]
                val dateStr =
                    "${currentDate.dayOfWeek.name}, ${currentDate.dayOfMonth} ${currentDate.month.name}"
                val temp = currentDay.main.temp.toInt()
                val humidity = currentDay.main.humidity
                val weather = currentDay.weather[0].description
                val windSpeed = currentDay.wind.speed.toInt()

                withContext(Dispatchers.Main) {
                    weatherForViewMutable.value =
                        WeatherForView(dateStr, temp, humidity, weather, windSpeed)
                }
            }
        }
    }
}