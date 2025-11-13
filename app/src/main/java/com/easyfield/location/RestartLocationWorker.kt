package com.easyfield.location

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import androidx.work.Worker
import androidx.work.WorkerParameters

class RestartLocationWorker(appContext: Context, workerParams: WorkerParameters) :
    Worker(appContext, workerParams) {
    override fun doWork(): Result {
        val intent = Intent(applicationContext, NewLocationService::class.java)
        ContextCompat.startForegroundService(applicationContext, intent)
        return Result.success()
    }
}