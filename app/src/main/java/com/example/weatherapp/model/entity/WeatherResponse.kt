package com.example.weatherapp.model.entity

data class WeatherResponse(
    val city: City,
    val cnt: Int,
    val cod: String,
    val list: List<Day>,
    val message: Int
)