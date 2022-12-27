package com.example.weatherapp.model.entity.dataFromCoordinates

data class DataFromCoordinatesResponseItem(
    val country: String,
    val lat: Double,
    val local_names: LocalNames,
    val lon: Double,
    val name: String,
    val state: String
)