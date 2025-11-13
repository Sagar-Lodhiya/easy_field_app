package com.easyfield.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

import com.easyfield.location.models.LocationRequest

@Dao
interface LocationDao {

    @Query("SELECT * FROM location")
    fun getAllLocations(): List<LocationRequest>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addLocation(location: LocationRequest)

    @Query("DELETE FROM location WHERE location_id IN (:ids)")
    suspend fun deleteLocations(ids: Array<Long>)

    @Query("DELETE FROM location")
    suspend fun deleteallLocations()
}