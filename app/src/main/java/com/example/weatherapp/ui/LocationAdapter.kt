package com.example.weatherapp.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weatherapp.data.LocationEntity
import com.example.weatherapp.databinding.ItemLocationBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class LocationAdapter(
    private val onDeleteClick: (LocationEntity) -> Unit
) : ListAdapter<LocationEntity, LocationAdapter.LocationViewHolder>(DiffCallback()) {

    private val dateFormat = SimpleDateFormat("dd/MM HH:mm", Locale.getDefault())

    inner class LocationViewHolder(private val binding: ItemLocationBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(location: LocationEntity) {
            binding.txtCity.text = "${location.cityName}, ${location.country}"
            binding.txtTemp.text = "${"%.1f".format(location.temperature)} °C"
            binding.txtDescription.text = location.description.replaceFirstChar { it.uppercase() }
            binding.txtHumidity.text = "Humedad: ${location.humidity}%"
            binding.txtWind.text = "Viento: ${"%.1f".format(location.windSpeed)} m/s"
            binding.txtUpdated.text = "Actualizado: ${dateFormat.format(Date(location.lastUpdated))}"

            // Cargar el ícono del clima desde la URL de OpenWeatherMap
            val iconUrl = "https://openweathermap.org/img/wn/${location.iconCode}@2x.png"
            Glide.with(binding.imgIcon.context)
                .load(iconUrl)
                .into(binding.imgIcon)

            // Configurar el botón de eliminar
            binding.btnDelete.setOnClickListener { onDeleteClick(location) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder {
        val binding = ItemLocationBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return LocationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LocationViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    /**
     * DiffUtil compara la lista vieja con la nueva y solo actualiza
     * los items que realmente cambiaron, en lugar de redibujar todo.
     */
    class DiffCallback : DiffUtil.ItemCallback<LocationEntity>() {
        override fun areItemsTheSame(oldItem: LocationEntity, newItem: LocationEntity) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: LocationEntity, newItem: LocationEntity) =
            oldItem == newItem
    }
}