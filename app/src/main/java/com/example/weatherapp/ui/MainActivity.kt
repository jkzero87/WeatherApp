package com.example.weatherapp.ui

import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapp.databinding.ActivityMainBinding
import com.example.weatherapp.viewmodel.WeatherViewModel
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: WeatherViewModel by viewModels()
    private lateinit var adapter: LocationAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        observeViewModel()

        binding.fabAdd.setOnClickListener { showAddLocationDialog() }
    }

    private fun setupRecyclerView() {
        adapter = LocationAdapter { location ->
            // Confirmar antes de eliminar
            AlertDialog.Builder(this)
                .setTitle("Eliminar")
                .setMessage("¿Eliminar ${location.cityName} de la lista?")
                .setPositiveButton("Sí") { _, _ -> viewModel.deleteLocation(location) }
                .setNegativeButton("Cancelar", null)
                .show()
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
    }

    private fun observeViewModel() {
        // Lista de ubicaciones
        viewModel.allLocations.observe(this) { locations ->
            adapter.submitList(locations)
            binding.txtEmpty.visibility =
                if (locations.isEmpty()) View.VISIBLE else View.GONE
        }

        // Indicador de carga
        viewModel.isLoading.observe(this) { loading ->
            binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
        }

        // Mensajes de estado (Snackbars)
        viewModel.statusMessage.observe(this) { message ->
            message?.let {
                Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT).show()
                viewModel.clearStatus()
            }
        }
    }

    private fun showAddLocationDialog() {
        val input = EditText(this).apply {
            hint = "Ej: Bogotá, London, Tokyo"
            setPadding(48, 32, 48, 32)
        }

        AlertDialog.Builder(this)
            .setTitle("Agregar ubicación")
            .setView(input)
            .setPositiveButton("Agregar") { _, _ ->
                viewModel.addLocation(input.text.toString())
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }
}