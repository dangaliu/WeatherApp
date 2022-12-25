package com.example.weatherapp.model.entity

data class WeatherForView(
    val dateStr: String,
    val temp: Int,
    val humidity: Int,
    val weather: String,
    val windSpeed: Int
)
