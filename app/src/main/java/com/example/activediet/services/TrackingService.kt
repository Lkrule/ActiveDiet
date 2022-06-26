package com.example.activediet.services

import android.annotation.SuppressLint
import android.content.Intent
import android.location.Location
import android.os.Looper
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.activediet.utilities.Constants.ACTION_PAUSE_SERVICE
import com.example.activediet.utilities.Constants.ACTION_START_OR_RESUME_SERVICE
import com.example.activediet.utilities.Constants.ACTION_STOP_SERVICE
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

    var isFirstRun = true
    var serviceKilled = false




    @Inject
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient


    private val timeRunInSec = MutableLiveData<Long>()


    companion object {
        val timeRunInMs = MutableLiveData<Long>()
        val isTracking = MutableLiveData<Boolean>()
        val pathPoints = MutableLiveData<tracks>()
    }


    override fun onCreate() {
        super.onCreate()
        postInitialValues()
        fusedLocationProviderClient = FusedLocationProviderClient(this)

        isTracking.observe(this, Observer {
            updateLocationTracking(it)
        })
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            when(it.action){
                ACTION_START_OR_RESUME_SERVICE -> {
                    if(isFirstRun){
                        startTimer()
                        isTracking.postValue(true)
                        isFirstRun = false
                    }
                    else{
                        Timber.d("ACTION_START_OR_RESUME_SERVICE")
                        startTimer()
                    }
                }

                ACTION_PAUSE_SERVICE -> pauseService()
                ACTION_STOP_SERVICE -> killService()
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
                    for (location in locations){
                        addPathPoint(location)
                        Timber.d("NEW LOCATION: ${location.latitude}, ${location.longitude}")
                    }
                }
            }
        }
    }

    private var isTimerEnable = false
    private var lapTime = 0L
    private var timeRun = 0L
    private var timeStarted = 0L
    private var lastSecondsTimeStamp = 0L


    private fun startTimer(){
        addEmptyTracks()
        isTracking.postValue(true)
        timeStarted = System.currentTimeMillis()
        isTimerEnable = true

        CoroutineScope(Dispatchers.Main).launch {
            while (isTracking.value!!){
                // time difference between now and timeStarted
                lapTime = System.currentTimeMillis() - timeStarted

                // post new lapTime
                timeRunInMs.postValue(timeRun + lapTime)

                if (timeRunInMs.value!! >= lastSecondsTimeStamp + 1000L){
                    timeRunInSec.postValue(timeRunInSec.value!! + 1)
                    lastSecondsTimeStamp += 1000L
                }
                delay(50L)
            }

            timeRun += lapTime
        }
    }


    private fun pauseService() {
        isTracking.postValue(false)
        isTimerEnable = false
    }


    private fun postInitialValues(){
        isTracking.postValue(false)
        pathPoints.postValue(mutableListOf())
        timeRunInSec.postValue(0L)
        timeRunInMs.postValue(0L)
    }


    private fun killService(){
        serviceKilled = true
        isFirstRun = true
        pauseService()
        postInitialValues()
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
        if(isTracking && TrackingUtility.hasLocationPermissions(this)){
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
        else{
            fusedLocationProviderClient.removeLocationUpdates(locationCallback)
        }
    }
}