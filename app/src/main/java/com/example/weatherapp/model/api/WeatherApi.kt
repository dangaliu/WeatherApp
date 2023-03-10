package com.example.weatherapp.model.api

import com.example.weatherapp.model.entity.dataFromCoordinates.DataFromCoordinatesResponse
import com.example.weatherapp.model.entity.weatherResponse.WeatherResponse
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    companion object {

        private val baseUrl = "https://api.openweathermap.org/"
        private val apiKey = "a880131edab78e076f5a2bb603d9dac7"

        val weatherApiClient = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WeatherApi::class.java)
    }

    @GET("data/2.5/forecast")
    suspend fun get5DayWeather(
        @Query("appid") apiKey: String = WeatherApi.apiKey,
        @Query("lat") lat: Double = 44.34,
        @Query("lon") lon: Double = 10.99,
        @Query("lang") language: String = "ru",
        @Query("units") units: String = "metric"
    ): Response<WeatherResponse>

    @GET("geo/1.0/reverse")
    suspend fun getCityFromCoordinates(
        @Query("appid") apiKey: String = WeatherApi.apiKey,
        @Query("lat") lat: Double = 44.34,
        @Query("lon") lon: Double = 10.99,
    ): Response<DataFromCoordinatesResponse>
}