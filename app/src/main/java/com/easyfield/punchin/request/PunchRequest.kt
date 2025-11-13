package com.easyfield.punchin.request

import com.google.gson.annotations.SerializedName

data class PunchRequest(
    @SerializedName("latitude") var lat: Double? = null,
    @SerializedName("longitude") var lng: Double? = null,
    @SerializedName("place") var place: String? = null,
    @SerializedName("vehicle_type") var vehicleType: String? = null,
    @SerializedName("meter_reading_in_km") var meterReading: Double? = null,
    @SerializedName("battery") var battery: String? = null,
    @SerializedName("mobile_network") var mobile_network: String? = null,
    @SerializedName("image") var meterReadingPhoto: String? = null,
    @SerializedName("is_night_stay") var isNightStay: Boolean? = false,
    @SerializedName("tour_details") var tour_details: String? = null,
    @SerializedName("punch_in_type") var punch_in_type: String? = null

)
