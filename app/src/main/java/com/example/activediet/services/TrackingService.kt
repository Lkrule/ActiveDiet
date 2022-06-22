package com.example.activediet.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.location.Location
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import com.example.activediet.utilities.Constants.ACTION_PAUSE_SERVICE
import com.example.activediet.utilities.Constants.ACTION_START_OR_RESUME_SERVICE
import com.example.activediet.utilities.Constants.ACTION_STOP_SERVICE
import com.example.activediet.utilities.Constants.NOTIFICATION_CHANNEL_ID
import com.example.activediet.utilities.Constants.NOTIFICATION_CHANNEL_NAME
import com.example.activediet.utilities.tracks
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject


@AndroidEntryPoint
class TrackingService : LifecycleService() {

    var isFirstRun = true
    var serviceKilled = false




    @Inject
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    @Inject
    lateinit var baseNotificationBuilder: NotificationCompat.Builder

    lateinit var currentNotificationBuilder: NotificationCompat.Builder

    private val timeRunInSec = MutableLiveData<Long>()


    companion object {
        val timeRunInMs = MutableLiveData<Long>()
        val isTracking = MutableLiveData<Boolean>()
        val pathPoints = MutableLiveData<tracks>()
    }


    private fun addEmptyTracks() = pathPoints.value?.apply {
        add(mutableListOf())
        pathPoints.postValue(this)
    } ?: pathPoints.postValue(mutableListOf(mutableListOf()))


    val locationCallback = object : LocationCallback(){
        override fun onLocationResult(result: LocationResult) {
            super.onLocationResult(result)
            if(isTracking.value!!){
                result.locations.let { locations ->
                    for (location in locations){
                        addPathPoint(location)
                        Timber.d("NEW LOCATION: ${location.latitude}, ${location.longitude}")
                    }
                }
            }
        }
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            when(it.action){
                ACTION_START_OR_RESUME_SERVICE -> {

                    if(isFirstRun){
                        startForegroundService()
                        isFirstRun = false
                    }
                    else{
                        Timber.d("ACTION_START_OR_RESUME_SERVICE")
                        //startTimer()
                    }

                }
                ACTION_PAUSE_SERVICE -> {
                    Timber.d("ACTION_PAUSE_SERVICE")
                    //pauseService()
                }
                ACTION_STOP_SERVICE -> {
                    //killService()
                }
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }



    private fun addPathPoint(location: Location?){
        location?.let {
            val position = LatLng(location.altitude, location.longitude)
            pathPoints.value?.apply {
                last().add(position)
                pathPoints.postValue(this)
            }
        }
    }


    private fun startForegroundService(){

    }



    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager: NotificationManager){
        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME, NotificationManager.IMPORTANCE_LOW)

        notificationManager.createNotificationChannel(channel)
    }
}