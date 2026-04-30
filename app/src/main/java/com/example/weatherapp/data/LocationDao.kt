package com.example.weatherapp.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface LocationDao {

    @Query("SELECT * FROM locations ORDER BY lastUpdated DESC")
    fun getAllLocations(): LiveData<List<LocationEntity>>

    @Query("SELECT * FROM locations WHERE cityName = :city LIMIT 1")
    suspend fun findByCity(city: String): LocationEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(location: LocationEntity): Long

    @Update
    suspend fun update(location: LocationEntity)

    @Query("DELETE FROM locations WHERE id = :id")
    suspend fun deleteById(id: Long)
}