package com.example.weatherapp.model.entity.weatherResponse

data class Weather(
    val description: String,
    val icon: String,
    val id: Int,
    val main: String
)