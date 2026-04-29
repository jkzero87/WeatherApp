package com.example.weatherapp

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.weatherapp.api.RetrofitClient
import com.example.weatherapp.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    // ViewBinding: nos da acceso a las views del XML por su id, sin findViewById
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Inflamos el layout y lo asignamos como la vista de esta Activity
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Padding automático para que la UI no quede tapada por barras del sistema
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Cuando el usuario toque el botón:
        binding.btnSearch.setOnClickListener {
            val city = binding.editCity.text.toString().trim()
            if (city.isEmpty()) {
                Toast.makeText(this, "Escribe una ciudad", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            consultarClima(city)
        }
    }

    private fun consultarClima(ciudad: String) {
        binding.txtResult.text = "Consultando..."

        // lifecycleScope es un "scope de coroutine" atado a la Activity.
        // Se cancela automáticamente si la Activity muere.
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.instance.getCurrentWeather(
                    cityName = ciudad,
                    apiKey = RetrofitClient.API_KEY
                )

                // Si llegamos aquí, la API respondió con éxito
                val texto = """
                    Ciudad: ${response.cityName}, ${response.sys.country ?: "?"}
                    Temperatura: ${response.main.temp} °C
                    Sensación térmica: ${response.main.feelsLike} °C
                    Humedad: ${response.main.humidity}%
                    Descripción: ${response.weather.firstOrNull()?.description ?: "?"}
                    Viento: ${response.wind.speed} m/s
                """.trimIndent()

                binding.txtResult.text = texto
                Log.d("WeatherApp", "Respuesta recibida: $response")

            } catch (e: Exception) {
                // Si algo falla (sin internet, ciudad no existe, key inválida, etc.)
                binding.txtResult.text = "Error: ${e.localizedMessage}"
                Log.e("WeatherApp", "Error al consultar el clima", e)
            }
        }
    }
}