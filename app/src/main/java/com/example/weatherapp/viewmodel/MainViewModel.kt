package com.example.weatherapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.model.api.WeatherApi
import com.example.weatherapp.model.entity.WeatherForView
import com.example.weatherapp.model.entity.WeatherInHour
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.time.ZoneOffset

class MainViewModel : ViewModel() {

    private var weatherForViewMutable = MutableLiveData<WeatherForView>()
    val weatherForView: LiveData<WeatherForView> = weatherForViewMutable

    private var weatherInHoursMutable = MutableLiveData<List<WeatherInHour>>()
    val weatherInHour: LiveData<List<WeatherInHour>> = weatherInHoursMutable

    fun getWeather() {
        viewModelScope.launch(Dispatchers.IO) {
            val response = WeatherApi.weatherApiClient.get5DayWeather()
            if (response.isSuccessful && response.body() != null) {
                val weatherResponse = response.body()!!
                val currentDay = weatherResponse.list[0]
                val dateStr = getFormattedDate(currentDay.dt.toLong())
                val temp = currentDay.main.temp.toInt()
                val humidity = currentDay.main.humidity
                val weather = currentDay.weather[0].description.replaceFirstChar {
                    it.uppercaseChar()
                }
                val windSpeed = currentDay.wind.speed.toInt()

                withContext(Dispatchers.Main) {
                    weatherForViewMutable.value =
                        WeatherForView(dateStr, temp, humidity, weather, windSpeed)
                }
            }
        }
    }

    fun getWeatherInHours() {
        viewModelScope.launch(Dispatchers.IO) {
            val response = WeatherApi.weatherApiClient.get5DayWeather()
            if (response.isSuccessful && response.body() != null) {
                val items = arrayListOf<WeatherInHour>()
                val weatherResponse = response.body()!!
                for (item in weatherResponse.list) {
                    val date = LocalDateTime.ofEpochSecond(item.dt.toLong(), 0, ZoneOffset.UTC)
                    if (date.dayOfMonth == LocalDateTime.now().dayOfMonth) {
                        items.add(
                            WeatherInHour(
                                time = getHour(item.dt.toLong()),
                                temp = item.main.temp.toInt(),
                                windSpeed = item.wind.speed.toInt()
                            )
                        )
                    }
                }
                withContext(Dispatchers.Main) {
                    weatherInHoursMutable.value = items
                }
            }
        }
    }

    private fun getFormattedDate(dateMillis: Long): String {
        val date = LocalDateTime.ofEpochSecond(dateMillis, 0, ZoneOffset.UTC)
        return "${
            date.dayOfWeek.name.lowercase().replaceFirstChar { it.uppercaseChar() }
        }, ${date.dayOfMonth} ${
            date.month.name.lowercase().replaceFirstChar { it.uppercaseChar() }
        }"
    }

    private fun getHour(dateMillis: Long): String {
        val date = LocalDateTime.ofEpochSecond(dateMillis, 0, ZoneOffset.UTC)
        var hour = date.hour.toString()
        var minute = date.minute.toString()
        if (hour.length == 1) hour = "0$hour"
        if (minute.length == 1) minute = "0$minute"
        return "${hour}:${minute}"
    }
}