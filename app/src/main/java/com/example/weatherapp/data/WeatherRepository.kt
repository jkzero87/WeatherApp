package com.example.weatherapp.data

import androidx.lifecycle.LiveData
import com.example.weatherapp.api.RetrofitClient

/**
 * Sealed class para representar el resultado de una operación.
 * Es buena práctica para manejar éxito/error de forma tipada.
 */
sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val message: String) : Result<Nothing>()
}

class WeatherRepository(private val locationDao: LocationDao) {

    // LiveData que la pantalla observará para ver la lista actualizada
    val allLocations: LiveData<List<LocationEntity>> = locationDao.getAllLocations()

    /**
     * Llama a la API y guarda el resultado en SQLite.
     * Si la ciudad ya existe, la actualiza con datos frescos.
     */
    suspend fun fetchAndSaveWeather(cityName: String): Result<LocationEntity> {
        return try {
            val response = RetrofitClient.instance.getCurrentWeather(
                cityName = cityName,
                apiKey = RetrofitClient.API_KEY
            )

            // Buscar si ya existe esta ciudad en la base de datos
            val existing = locationDao.findByCity(response.cityName)

            // Construir la entidad con los datos recibidos
            val entity = LocationEntity(
                id = existing?.id ?: 0,
                cityName = response.cityName,
                country = response.sys.country ?: "",
                temperature = response.main.temp,
                description = response.weather.firstOrNull()?.description ?: "",
                humidity = response.main.humidity,
                windSpeed = response.wind.speed,
                iconCode = response.weather.firstOrNull()?.icon ?: "01d",
                lastUpdated = System.currentTimeMillis()
            )

            // Insertar (si es nueva) o actualizar (si ya existía)
            if (existing == null) {
                val newId = locationDao.insert(entity)
                Result.Success(entity.copy(id = newId))
            } else {
                locationDao.update(entity)
                Result.Success(entity)
            }

        } catch (e: retrofit2.HttpException) {
            when (e.code()) {
                401 -> Result.Error("API key inválida o aún no activa")
                404 -> Result.Error("Ciudad no encontrada")
                else -> Result.Error("Error del servidor: ${e.code()}")
            }
        } catch (e: java.net.UnknownHostException) {
            Result.Error("Sin conexión a internet")
        } catch (e: Exception) {
            Result.Error("Error inesperado: ${e.localizedMessage}")
        }
    }

    /**
     * Elimina una ubicación por su id.
     */
    suspend fun deleteLocation(id: Long) {
        locationDao.deleteById(id)
    }
}