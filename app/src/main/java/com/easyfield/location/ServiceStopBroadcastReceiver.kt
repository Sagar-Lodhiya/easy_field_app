package com.easyfield.location

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log


internal class ServiceStopBroadcastReceiver : BroadcastReceiver() {

    companion object {
        const val TAG = "ServiceStopBroadcastReceiver"
    }

    @SuppressLint("LongLogTag")
    override fun onReceive(context: Context, intent: Intent) {
        Log.e(TAG, "Received broadcast to stop location updates")
        context.stopService(Intent(context, NewLocationService::class.java))
    }
}