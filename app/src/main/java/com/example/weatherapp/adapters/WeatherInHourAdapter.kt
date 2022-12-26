package com.example.weatherapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.databinding.ItemHourWeatherBinding
import com.example.weatherapp.model.entity.WeatherInHour

class WeatherInHourAdapter(
    private val context: Context
) : RecyclerView.Adapter<WeatherInHourAdapter.WeatherInHourHolder>() {

    private var items: ArrayList<WeatherInHour> = arrayListOf()

    inner class WeatherInHourHolder(val binding: ItemHourWeatherBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherInHourHolder {
        val binding = ItemHourWeatherBinding.inflate(LayoutInflater.from(context), parent, false)
        return WeatherInHourHolder(binding)
    }

    override fun onBindViewHolder(holder: WeatherInHourHolder, position: Int) {
        val item = items[position]
        if (position == items.lastIndex) {
            holder.binding.rootView.setPadding(0, 0, 0, 0)
        }
        with(holder.binding) {
            tvHour.text = item.time
            tvTemp.text = "${item.temp}°"
            tvWindSpeed.text = "${item.windSpeed} km/h"
        }
    }

    override fun getItemCount(): Int = items.size

    fun updateItems(items: ArrayList<WeatherInHour>) {
        this.items = items
        notifyDataSetChanged()
    }
}