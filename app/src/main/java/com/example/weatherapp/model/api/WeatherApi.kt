package com.example.weatherapp.model.api

import com.example.weatherapp.model.entity.Weather
import com.example.weatherapp.model.entity.WeatherResponse
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    companion object {

        private val baseUrl = "api.openweathermap.org/data/2.5/"
        private val apiKey = "a880131edab78e076f5a2bb603d9dac7"

        val weatherApiClient = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WeatherApi::class.java)
    }

    @GET("forecast")
    suspend fun get5DayWeather(
        @Query("appid") apiKey: String = WeatherApi.apiKey,
        @Query("q") city: String,
        @Query("lang") language: String = "ru",
        @Query("units") units: String = "metric"
    ): Response<WeatherResponse>
}