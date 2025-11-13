package com.easyfield.location.models

import androidx.room.Entity
import com.google.gson.annotations.SerializedName

@Entity(tableName = "location")

data class BulkLocationRequest(
    @SerializedName("locations") var location: List<LocationRequest>,
    @SerializedName("punch_in_id") var punch_in_id: String=""
)
