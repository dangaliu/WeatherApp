package com.example.weatherapp.view.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapp.adapters.WeatherInHourAdapter
import com.example.weatherapp.databinding.FragmentMainBinding
import com.example.weatherapp.viewmodel.MainViewModel

class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var weatherInHoursAdapter: WeatherInHourAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(layoutInflater, container, false)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        init()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setObservers()
    }

    private fun init() {
        viewModel.getWeather()
        viewModel.getWeatherInHours()

        weatherInHoursAdapter = WeatherInHourAdapter(requireContext())

        binding.rvHours.apply {
            adapter = weatherInHoursAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setObservers() {
        viewModel.weatherForView.observe(viewLifecycleOwner) { weather ->
            with(binding) {
                tvWeatherTemp.text = "${weather.temp}°"
                tvHumidityPercent.text = "${weather.humidity}%"
                tvWindSpeed.text = "${weather.windSpeed} km/h"
                tvWeatherState.text = weather.weather
                tvCurrentDate.text = weather.dateStr
            }
        }

        viewModel.weatherInHour.observe(viewLifecycleOwner) {
            weatherInHoursAdapter.updateItems(ArrayList(it))
        }
    }

}