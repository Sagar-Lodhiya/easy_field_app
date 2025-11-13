//package com.easyfield.location
//
//import android.Manifest
//import android.app.Notification
//import android.app.NotificationChannel
//import android.app.NotificationManager
//import android.content.Context
//import android.content.Intent
//import android.content.SharedPreferences
//import android.content.pm.PackageManager
//import android.graphics.Color
//import android.location.Location
//import android.location.LocationManager
//import android.net.ConnectivityManager
//import android.net.Network
//import android.net.NetworkCapabilities
//import android.net.NetworkRequest
//import android.os.Build
//import android.os.Looper
//import android.util.Log
//import android.widget.Toast
//import androidx.annotation.RequiresApi
//import androidx.core.app.ActivityCompat
//import androidx.core.app.NotificationCompat
//import androidx.core.location.LocationManagerCompat
//import androidx.lifecycle.LifecycleService
//import androidx.lifecycle.lifecycleScope
//import androidx.work.ExistingPeriodicWorkPolicy
//import androidx.work.PeriodicWorkRequestBuilder
//import androidx.work.WorkManager
//import com.easyfield.database.AppDatabase
//import com.easyfield.location.models.BulkLocationRequest
//import com.easyfield.location.models.SaveLocationRequest
//import com.easyfield.login.response.loginresponse.User
//import com.easyfield.network.RetrofitClient
//import com.easyfield.utils.AppConstants
//import com.easyfield.utils.PreferenceHelper
//import com.easyfield.utils.extension.getAddressString
//import com.easyfield.utils.extension.log
//
//import com.google.android.gms.location.FusedLocationProviderClient
//import com.google.android.gms.location.Granularity
//import com.google.android.gms.location.LocationAvailability
//import com.google.android.gms.location.LocationCallback
//import com.google.android.gms.location.LocationRequest
//import com.google.android.gms.location.LocationResult
//import com.google.android.gms.location.LocationServices
//import com.google.android.gms.location.Priority
//import com.lezasolutions.callenza.utils.extension.getDeviceBattery
//import com.lezasolutions.callenza.utils.extension.getNetworkType
//import com.lezasolutions.callenza.utils.extension.hasInternetConnection
//import kotlinx.coroutines.launch
//import okhttp3.RequestBody.Companion.toRequestBody
//import java.util.concurrent.TimeUnit
//
//class BackUpNewLocationService : LifecycleService() {
//
//    private lateinit var fusedLocationClient: FusedLocationProviderClient
//
//    private lateinit var locationManager: LocationManager
//
//
//
//    companion object {
//        const val TAG = "LocationService"
//    }
//
//
//    private lateinit var connectivityManager: ConnectivityManager
//    private val networkCallback: ConnectivityManager.NetworkCallback =
//        object : ConnectivityManager.NetworkCallback() {
//            override fun onAvailable(network: Network) {
//                super.onAvailable(network)
//                val db = AppDatabase.getInstance(this@BackUpNewLocationService)
//                val list = db.locationDao().getAllLocations()
//
//                Log.w("TagVIshal","Service_Startedif${list.size}")
//
//                if (list.isNotEmpty()) {
//                    sendBulkLocationData(list)
//                }
//            }
//
//            override fun onLost(network: Network) {
//                super.onLost(network)
//                log(TAG) { "Internet connection is lost" }
//            }
//        }
//
//    private val locationRequest: LocationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY,
//
//        10000).apply {
//
//    }.build()
//
////    private val locationRequest: LocationRequest = LocationRequest.Builder(Priority.PRIORITY_BALANCED_POWER_ACCURACY,
////
////        5000)
////
////        .apply {
////
////        }.build()
//
////        .setWaitForAccurateLocation(true)
//
////        .setMinUpdateDistanceMeters(5.0f)
//
//    private var locationCallback: LocationCallback = object : LocationCallback() {
//
//        private var lastLocation: Location? = null
//
//        @RequiresApi(Build.VERSION_CODES.O)
//        override fun onLocationResult(locationResult: LocationResult) {
//            val locationList = locationResult.locations
//            if (locationList.isNotEmpty()) {
//
//                val location = locationList.last()
//
//                Log.w("sendLocation",""+location.latitude+"long"+location.longitude)
//
//
//                lastLocation?.let { previous ->
//                    val distance = previous.distanceTo(location)
//                    Log.d("sendLocation", "Moved: $distance meters")
//
//                    if(distance>5){
//
//                    }
//                }
//
//                lastLocation=location
//                if (applicationContext.hasInternetConnection()) {
//                    sendLocationInfo(location)
//                    saveDummyLocationData(location)
//                }
//                else{
//                    saveLocationData(location)
//                }
//
//            }
//        }
//    }
//
//
//    override fun onCreate() {
//        super.onCreate()
//
//        Log.w("TagVIshal","Service_Started")
//
//        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
//        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
//        startlocationService()
//
//        initInternetConnectionListener()
//
//        val workRequest = PeriodicWorkRequestBuilder<RestartLocationWorker>(15, TimeUnit.MINUTES)
//            .addTag("location_worker")
//            .build()
//
//        WorkManager.getInstance(this@BackUpNewLocationService).enqueueUniquePeriodicWork(
//            "location_restart",
//            ExistingPeriodicWorkPolicy.KEEP,
//            workRequest
//        )
//    }
//    fun startlocationService(){
//        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O){
//            Log.w("TagVIshal","Service_Startedif")
//            createNotificationChanel()
//        }
//        else{
//            Log.w("TagVIshal","Service_Startedelse")
//            startForeground(1, Notification())
//        }
//
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
//            != PackageManager.PERMISSION_GRANTED
//            && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
//            != PackageManager.PERMISSION_GRANTED) {
//
//            Toast.makeText(applicationContext, "Permission required", Toast.LENGTH_LONG).show()
//            return
//        }
//        else
//        {
//
//            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
////            fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
////                .addOnSuccessListener { location ->
////                    // fresh location
////
////
////                    sendLocationInfo(location)
////                }
//        }
//    }
//
//
//    private fun initInternetConnectionListener() {
//        connectivityManager = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
//
//        when {
//            true -> {
//                connectivityManager.registerDefaultNetworkCallback(networkCallback)
//            }
//
//            true -> {
//                val builder = NetworkRequest.Builder()
//                    .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
//                    .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
//
//                connectivityManager.registerNetworkCallback(builder.build(), networkCallback)
//            }
//        }
//    }
//
//    @RequiresApi(Build.VERSION_CODES.O)
//    private fun createNotificationChanel() {
//        val notificationChannelId = "123"
//        val channelName = "Axis Seeds"
//        val chan = NotificationChannel(notificationChannelId, channelName, NotificationManager.IMPORTANCE_NONE)
//        chan.lightColor = Color.BLUE
//        chan.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
//        val manager = (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)
//        manager.createNotificationChannel(chan)
//        val notificationBuilder =
//            NotificationCompat.Builder(this, notificationChannelId)
//        val notification: Notification = notificationBuilder.setOngoing(true)
//            .setContentTitle("Axis SeedsLocation")
//            .setPriority(NotificationManager.IMPORTANCE_MIN)
//            .setCategory(Notification.CATEGORY_SERVICE)
//            .build()
//        startForeground(2, notification)
//    }
//    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
//
////        startlocationService()
//        super.onStartCommand(intent, flags, startId)
//
//        return START_STICKY
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        fusedLocationClient.removeLocationUpdates(locationCallback)
//    }
//
//
//    private fun sendLocationInfo(location: Location?) {
//
//        Log.w("sendLocation","true")
//
////        saveLocationData(location)
//        location?.let {
//            log(TAG) { it.getAddressString(applicationContext) }
//        }
//        lifecycleScope.launchWhenStarted {
//            val locationRequest = com.easyfield.location.models.LocationRequest().apply {
//                val user = PreferenceHelper.getObject<User>(AppConstants.PREF_KEY_USER)
//                userId = user?.id.toString()
//                latitude = location?.latitude ?: 0.0
//                longitude = location?.longitude ?: 0.0
//
//                attendanceId = PreferenceHelper[AppConstants.PREF_KEY_ATTENDANCE_ID]
//
//
//                gpsStatus =
//                    if (LocationManagerCompat.isLocationEnabled(locationManager)) 1 else 0
//                batteryInPercentage = "${getDeviceBattery()}"
//                networkType = getNetworkType()
//
////                Toast.makeText(this@NewLocationService, latitude.toString()+" "+longitude.toString(), Toast.LENGTH_SHORT).show()
//
//                Log.w("sendLocation","latitude"+latitude+" longitude"+longitude)
//            }
//
//
//            val response =
//                RetrofitClient.instance.sendLocation(
//                    locationRequest.latitude,locationRequest.longitude,locationRequest.attendanceId!!.toRequestBody(),
//                    if (LocationManagerCompat.isLocationEnabled(locationManager)) 1 else 0,
//                    getDeviceBattery(),getNetworkType().toRequestBody())
//            if (response.isSuccessful) {
//                response.body()?.let { resultResponse ->
//                    resultResponse.message.let {
//                        Log.w("sendLocation","true"+it.toString())
//
//                    }
//                }
//            }
//        }
//    }
//    private fun saveDummyLocationData(location: Location?) {
//
//        log(NewLocationService.TAG) { "Saving Location Data Into Database..." }
//
//        val db = AppDatabase.getInstance(applicationContext)
//        lifecycleScope.launchWhenStarted {
//            db.savelocationDao().addLocation(SaveLocationRequest().apply {
//                val user = PreferenceHelper.getObject<User>(AppConstants.PREF_KEY_USER)
//                userId = user?.id.toString()
//                latitude = location?.latitude ?: 0.0
//                longitude = location?.longitude ?: 0.0
//                attendanceId = PreferenceHelper[AppConstants.PREF_KEY_ATTENDANCE_ID]
//                gpsStatus =
//                    if (LocationManagerCompat.isLocationEnabled(locationManager)) 1 else 0
//                batteryInPercentage = "${getDeviceBattery()}"
//                networkType = getNetworkType()
//            })
//        }
//        Log.w("TagVIshal","data saved")
//    }
//
//    private fun saveLocationData(location: Location?) {
//
//        log(NewLocationService.TAG) { "Saving Location Data Into Database..." }
//
//        val db = AppDatabase.getInstance(applicationContext)
//        lifecycleScope.launchWhenStarted {
//            db.locationDao().addLocation(com.easyfield.location.models.LocationRequest().apply {
//                val user = PreferenceHelper.getObject<User>(AppConstants.PREF_KEY_USER)
//                userId = user?.id.toString()
//                latitude = location?.latitude ?: 0.0
//                longitude = location?.longitude ?: 0.0
//                attendanceId = PreferenceHelper[AppConstants.PREF_KEY_ATTENDANCE_ID]
//                gpsStatus =
//                    if (LocationManagerCompat.isLocationEnabled(locationManager)) 1 else 0
//                batteryInPercentage = "${getDeviceBattery()}"
//                networkType = getNetworkType()
//            })
//        }
//        Log.w("TagVIshal","data saved")
//    }
//    private fun sendBulkLocationData(list: List<com.easyfield.location.models.LocationRequest>) {
//
//        Log.w("TagVIshal","done")
//
//
//        val sharedPreferences: SharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
//
//        val punch_id=sharedPreferences.getString("punch_id","")!!
//
//
//        Log.w("TagVIshal","punch_id_service"+sharedPreferences.getString("punch_id","")!!)
//
//
//        val bulkLocationRequest=BulkLocationRequest(list,punch_id)
//
//        lifecycleScope.launchWhenStarted {
//            val response =
//                RetrofitClient.instance.bulkPostLocation(bulkLocationRequest)
//            if (response.isSuccessful) {
//                response.body()?.let { resultResponse ->
//                    resultResponse.message?.let {
//                        log(TAG) { it }
//                    }
//                    Log.w("TagVIshal","sucessapi"+resultResponse.status)
//
//
//
//                    if (resultResponse.status == 200) {
//                        val db = AppDatabase.getInstance(applicationContext)
//                        val arrayOfIds = list.map { it.locationId }.toTypedArray()
//                        db.locationDao().deleteLocations(arrayOfIds)
//
//                        Log.w("TagVIshal","Removed SYNCED data from database successfully")
//
//
//                    }
//                }
//            }
//        }
//    }
//}
