package com.easyfield.location.models

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.util.*

@Entity(tableName = "location")

data class LocationRequest(
    @PrimaryKey @ColumnInfo(name = "location_id") @SerializedName("location") var locationId: Long = Date().time,
    @ColumnInfo(name = "user_id") @SerializedName("user_id") var userId: String? = null,
    @SerializedName("latitude") var latitude: Double? = null,
    @SerializedName("longitude") var longitude: Double? = null,
    @ColumnInfo(name = "employee_punch_id") @SerializedName("punch_in_id") var attendanceId: String? = null,
    @ColumnInfo(name = "gps_status") @SerializedName("is_location_enabled") var gpsStatus: Int? = 1,
    @ColumnInfo(name = "battery") @SerializedName("battery") var batteryInPercentage: String? = null,
    @ColumnInfo(name = "network") @SerializedName("mobile_network") var networkType: String? = null,
    @ColumnInfo(name = "timestamp") @SerializedName("timestamp") var timestamp: Long = Date().time
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString(),
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readLong()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(locationId)
        parcel.writeString(userId)
        parcel.writeValue(latitude)
        parcel.writeValue(longitude)
        parcel.writeString(attendanceId)
        parcel.writeValue(gpsStatus)
        parcel.writeString(batteryInPercentage)
        parcel.writeString(networkType)
        parcel.writeLong(timestamp)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<LocationRequest> {
        override fun createFromParcel(parcel: Parcel): LocationRequest {
            return LocationRequest(parcel)
        }

        override fun newArray(size: Int): Array<LocationRequest?> {
            return arrayOfNulls(size)
        }
    }
}
