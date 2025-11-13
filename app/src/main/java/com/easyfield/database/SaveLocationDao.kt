package com.easyfield.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

import com.easyfield.location.models.SaveLocationRequest

@Dao
interface SaveLocationDao {

    @Query("SELECT * FROM location_dummy")
    fun getAllLocations(): List<SaveLocationRequest>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addLocation(location: SaveLocationRequest)

    @Query("DELETE FROM location_dummy WHERE location_id IN (:ids)")
    suspend fun deleteLocations(ids: Array<Long>)

    @Query("DELETE FROM location_dummy")
    suspend fun deleteallLocations()
}