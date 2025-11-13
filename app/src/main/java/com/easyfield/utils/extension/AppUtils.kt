package com.lezasolutions.callenza.utils.extension

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.BatteryManager
import android.os.Build
import android.provider.Settings
import android.telephony.TelephonyManager
import android.telephony.TelephonyManager.NETWORK_TYPE_1xRTT
import android.telephony.TelephonyManager.NETWORK_TYPE_CDMA
import android.telephony.TelephonyManager.NETWORK_TYPE_EDGE
import android.telephony.TelephonyManager.NETWORK_TYPE_EHRPD
import android.telephony.TelephonyManager.NETWORK_TYPE_EVDO_0
import android.telephony.TelephonyManager.NETWORK_TYPE_EVDO_A
import android.telephony.TelephonyManager.NETWORK_TYPE_EVDO_B
import android.telephony.TelephonyManager.NETWORK_TYPE_GPRS
import android.telephony.TelephonyManager.NETWORK_TYPE_GSM
import android.telephony.TelephonyManager.NETWORK_TYPE_HSDPA
import android.telephony.TelephonyManager.NETWORK_TYPE_HSPA
import android.telephony.TelephonyManager.NETWORK_TYPE_HSPAP
import android.telephony.TelephonyManager.NETWORK_TYPE_HSUPA
import android.telephony.TelephonyManager.NETWORK_TYPE_IDEN
import android.telephony.TelephonyManager.NETWORK_TYPE_IWLAN
import android.telephony.TelephonyManager.NETWORK_TYPE_LTE
import android.telephony.TelephonyManager.NETWORK_TYPE_NR
import android.telephony.TelephonyManager.NETWORK_TYPE_TD_SCDMA
import android.telephony.TelephonyManager.NETWORK_TYPE_UMTS
import android.telephony.TelephonyManager.NETWORK_TYPE_UNKNOWN
import android.view.View
import android.view.WindowManager
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import java.net.InetSocketAddress
import java.net.Socket

fun Fragment.getAppVersionName(): String? {
    return requireContext().packageManager?.getPackageInfo(
        requireContext().packageName,
        0
    )?.versionName
}

fun Fragment.formatAmount(amount: Double): String {
    return when {
        amount >= 1_00_00_000 -> String.format("%.2f Cr", amount / 1_00_00_000) // Crore
        amount >= 1_00_000 -> String.format("%.2f L", amount / 1_00_000) // Lakh
        amount >= 1_000 -> String.format("%.2f K", amount / 1_000) // Thousand
        else -> String.format("%.2f", amount) // Default
    }
}

fun Context.getDeviceBattery(): Int {
    val bm = this.getSystemService(Context.BATTERY_SERVICE) as BatteryManager
    return bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
}

fun Context.getNetworkType(): String {
    val telephonyManager = this.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
    if (ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_PHONE_STATE
        ) == PackageManager.PERMISSION_GRANTED
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            when (telephonyManager.dataNetworkType) {
                NETWORK_TYPE_EDGE,
                NETWORK_TYPE_GPRS,
                NETWORK_TYPE_CDMA,
                NETWORK_TYPE_IDEN,
                NETWORK_TYPE_1xRTT,
                NETWORK_TYPE_TD_SCDMA,
                NETWORK_TYPE_GSM -> return "2G"
                NETWORK_TYPE_UMTS,
                NETWORK_TYPE_HSDPA,
                NETWORK_TYPE_HSPA,
                NETWORK_TYPE_HSPAP,
                NETWORK_TYPE_EVDO_0,
                NETWORK_TYPE_EVDO_A,
                NETWORK_TYPE_EVDO_B,
                NETWORK_TYPE_EHRPD,
                NETWORK_TYPE_HSUPA -> return "3G"
                NETWORK_TYPE_IWLAN -> return "WiFi Calling"
                NETWORK_TYPE_LTE -> return "4G"
                NETWORK_TYPE_NR -> return "5G"
                NETWORK_TYPE_UNKNOWN -> return "Unknown"
                else -> return "Unknown"
            }
        } else {
            return "Unknown"
        }
    } else {
        return "Unknown"
    }
}

fun Fragment.getDeviceInformation(): String {
    return "${Build.MODEL} ${Build.VERSION.RELEASE} ${Build.MANUFACTURER} ${getAppVersionName()}"
}

@SuppressLint("HardwareIds")
fun Fragment.getUniqueSecureDeviceId(): String {
    return Settings.Secure.getString(requireActivity().contentResolver, Settings.Secure.ANDROID_ID)
}

fun Fragment.getAppVersionCode(): Int? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        requireContext().packageManager?.getPackageInfo(
            requireContext().packageName,
            0
        )?.longVersionCode?.toInt()
    } else {
        requireContext().packageManager?.getPackageInfo(
            requireContext().packageName,
            0
        )?.versionCode
    }
}


fun Fragment.toast(text: String) {
    Toast.makeText(this.context, text, Toast.LENGTH_LONG).show()
}
fun Activity.toast(text: String) {
    Toast.makeText(this, text, Toast.LENGTH_LONG).show()
}
fun Fragment.showProgressBar(progressBar: ProgressBar?, boolean: Boolean? = true) {
    if (boolean == true) {
        activity?.window?.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        )

        progressBar?.show()
    } else {
        hideProgressBar(progressBar)
    }
}
fun Fragment.hideProgressBar(progressBar: ProgressBar?) {
    activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    progressBar?.hide()
}
fun ProgressBar.show() {
    this.visibility = View.VISIBLE
}
fun ProgressBar.hide() {
    this.visibility = View.GONE
}
fun Context.hasInternetConnection(): Boolean {
    val connectivityManager = getSystemService(
        Context.CONNECTIVITY_SERVICE
    ) as ConnectivityManager
    val activeNetwork = connectivityManager.activeNetwork ?: return false
    val capabilities =
        connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
    return when {
        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
        else -> false
    }

}
