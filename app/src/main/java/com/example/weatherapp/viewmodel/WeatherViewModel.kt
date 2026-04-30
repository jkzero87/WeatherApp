package com.example.weatherapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.AppDatabase
import com.example.weatherapp.data.LocationEntity
import com.example.weatherapp.data.Result
import com.example.weatherapp.data.WeatherRepository
import kotlinx.coroutines.launch

class WeatherViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: WeatherRepository

    // Datos que la pantalla observa
    val allLocations: LiveData<List<LocationEntity>>

    // Mensajes de estado (errores o confirmaciones) que se muestran en pantalla
    private val _statusMessage = MutableLiveData<String?>()
    val statusMessage: LiveData<String?> = _statusMessage

    // Indicador de carga
    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        // Inicialización: obtener el DAO de la base de datos y crear el repository
        val dao = AppDatabase.getDatabase(application).locationDao()
        repository = WeatherRepository(dao)
        allLocations = repository.allLocations
    }

    /**
     * Agrega una ubicación: llama a la API, guarda en la base de datos.
     */
    fun addLocation(cityName: String) {
        if (cityName.isBlank()) {
            _statusMessage.value = "Escribe el nombre de una ciudad"
            return
        }

        viewModelScope.launch {
            _isLoading.value = true

            when (val result = repository.fetchAndSaveWeather(cityName.trim())) {
                is Result.Success -> _statusMessage.value = "${result.data.cityName} agregada"
                is Result.Error -> _statusMessage.value = result.message
            }

            _isLoading.value = false
        }
    }

    /**
     * Elimina una ubicación de la base de datos.
     */
    fun deleteLocation(location: LocationEntity) {
        viewModelScope.launch {
            repository.deleteLocation(location.id)
            _statusMessage.value = "${location.cityName} eliminada"
        }
    }

    /**
     * Limpia el mensaje de estado para evitar que se muestre dos veces.
     */
    fun clearStatus() {
        _statusMessage.value = null
    }
}