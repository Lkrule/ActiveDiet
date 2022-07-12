package com.example.activediet.services

import android.annotation.SuppressLint
import android.content.Intent
import android.location.Location
import android.os.Looper
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.activediet.utilities.Constants.PAUSE_SERVICE
import com.example.activediet.utilities.Constants.START_OR_RESUME_SERVICE
import com.example.activediet.utilities.Constants.STOP_SERVICE
import com.example.activediet.utilities.run.TrackingUtility
import com.example.activediet.utilities.tracks
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


@AndroidEntryPoint
class TrackingService : LifecycleService() {




    @Inject
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    companion object {
        val timeRunInMs = MutableLiveData<Long>()
        val isTracking = MutableLiveData<Boolean>()
        val pathPoints = MutableLiveData<tracks>()
    }

    private var lapTime = 0L
    private var timeRun = 0L
    private var timeStarted = 0L

    override fun onCreate() {
        super.onCreate()
        // reset
        postValues()
        fusedLocationProviderClient = FusedLocationProviderClient(this)

        isTracking.observe(this, Observer {
            updateLocationTracking(it)
        })
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            when(it.action){
                START_OR_RESUME_SERVICE -> startTimer()
                PAUSE_SERVICE -> pauseService()
                STOP_SERVICE -> killService()
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }



    private fun addEmptyTracks() = pathPoints.value?.apply {
        add(mutableListOf())
        pathPoints.postValue(this)
    } ?: pathPoints.postValue(mutableListOf(mutableListOf()))


    private val locationCallback = object : LocationCallback(){
        override fun onLocationResult(result: LocationResult) {
            super.onLocationResult(result)
            if(isTracking.value!!){
                result.locations.let { locations ->
                    for (location in locations)
                        addPathPoint(location)
                }
            }
        }
    }


    private fun startTimer(){
        addEmptyTracks()
        timeStarted = System.currentTimeMillis()
        isTracking.postValue(true)

        CoroutineScope(Dispatchers.Main).launch {
            while (isTracking.value!!){
                // time difference between now and timeStarted
                lapTime = System.currentTimeMillis() - timeStarted
                // post new lapTime
                timeRunInMs.postValue(timeRun + lapTime)
                delay(50L)
            }

            timeRun += lapTime
        }
    }


    private fun pauseService() {
        isTracking.postValue(false)
    }


    private fun postValues(){
        isTracking.postValue(false)
        pathPoints.postValue(mutableListOf())
        timeRunInMs.postValue(0L)
    }


    private fun killService(){
        pauseService()
        postValues()
        stopSelf()
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



    @SuppressLint("MissingPermission")
    private fun updateLocationTracking(isTracking: Boolean){
        if(isTracking){
            val request = LocationRequest.create().apply {
                interval = 5000L
                fastestInterval = 2000L
                priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            }

            fusedLocationProviderClient.requestLocationUpdates(
                request,
                locationCallback,
                Looper.getMainLooper()
            )
        }
        else fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }
}