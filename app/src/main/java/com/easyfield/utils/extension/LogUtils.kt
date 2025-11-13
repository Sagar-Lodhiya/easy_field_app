package com.easyfield.utils.extension

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.util.Log
import java.io.IOException

inline fun Any.log(tag: String = this::class.java.simpleName, message: () -> Unit) {
    Log.d(tag, message().toString())
}

inline fun Any.errorLog(tag: String = this::class.java.simpleName, message: () -> String) {
    Log.e(tag, message())
}

fun Location.getAddressString(context: Context): String {
    var addresses = mutableListOf<Address>()
    var errorMessage = "No Address Found."
    var address: String? = null

    try {
        addresses = Geocoder(context).getFromLocation(this.latitude, this.longitude, 1)!!
    } catch (ioException: IOException) {
        // Catch network or other I/O problems.
        errorMessage = "Service not available."
    } catch (illegalArgumentException: IllegalArgumentException) {
        // Catch invalid latitude or longitude values.
        errorMessage = "Invalid Lat/Long."
    }

    if (addresses.isEmpty()) {
        if (errorMessage.isEmpty()) {
            errorMessage = "No Address Found."
        }
    } else {
        val addressItem = addresses.first()
        address = addressItem.getAddressLine(0)
    }

    return address ?: errorMessage
}