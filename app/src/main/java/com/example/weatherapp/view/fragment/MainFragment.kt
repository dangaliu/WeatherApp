package com.example.weatherapp.view.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.weatherapp.adapters.WeatherInHourAdapter
import com.example.weatherapp.databinding.FragmentMainBinding
import com.example.weatherapp.utils.checkPermission
import com.example.weatherapp.utils.coarseLocation
import com.example.weatherapp.utils.fineLocation
import com.example.weatherapp.utils.showToast
import com.example.weatherapp.viewmodel.MainViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var weatherInHoursAdapter: WeatherInHourAdapter
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private lateinit var locationManager: LocationManager

    private var requestLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissionsMap ->

            if (permissionsMap[coarseLocation] == true && permissionsMap[fineLocation] == true) {
                showToast("Был предоставлен точный доступ к местоположению")
            } else if (permissionsMap[coarseLocation] == true && permissionsMap[fineLocation] == false) {
                showToast("Был предоставлен приблизительный доступ к местоположению")
            } else {
                showToast("Доступ к местоположению не был предоставлен")
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        locationManager =
            requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }

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
        if (!checkPermission(coarseLocation)) {
            requestLauncher.launch(arrayOf(coarseLocation, fineLocation))
        } else {
            locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                0L,
                0f
            ) { location ->
                Log.d("location", "${location.latitude} ${location.longitude}")
                viewModel.getWeather(lat = location.latitude, lon = location.longitude)
                viewModel.getWeatherInHours(lat = location.latitude, lon = location.longitude)
                viewModel.getCityFromLocation(location)
            }
        }
    }

    private fun init() {
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
                Glide.with(requireContext())
                    .load("http://openweathermap.org/img/wn/${weather.icon}.png")
                    .into(binding.ivWeatherImg)
            }
        }

        viewModel.weatherInHour.observe(viewLifecycleOwner) {
            weatherInHoursAdapter.updateItems(ArrayList(it))
        }

        viewModel.city.observe(viewLifecycleOwner) {
            binding.tvLocation.text = it
        }
    }

}