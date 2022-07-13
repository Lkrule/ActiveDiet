package com.example.activediet.services

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Looper
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.activediet.utilities.Constants.PAUSE_SERVICE
import com.example.activediet.utilities.Constants.START_OR_RESUME_SERVICE
import com.example.activediet.utilities.Constants.STOP_SERVICE
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
import javax.inject.Inject


@AndroidEntryPoint
class TrackingService : LifecycleService() {




    @Inject
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    companion object {
        val timeRun = MutableLiveData<Long>()
        val isTracking = MutableLiveData<Boolean>()
        val tracks = MutableLiveData<tracks>()
    }

    private var lap = 0L
    private var time = 0L
    private var timeStart = 0L

    @SuppressLint("VisibleForTests")
    override fun onCreate() {
        super.onCreate()
        // reset
        postValues()
        fusedLocationProviderClient = FusedLocationProviderClient(this)

        isTracking.observe(this) {
            updateLocationTracking(it)
        }
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



    // every pause can create new track
    private fun addEmptyTrack() = tracks.value?.apply {
        add(mutableListOf())
        tracks.postValue(this)
    } ?: tracks.postValue(mutableListOf(mutableListOf()))


    private val locationCallback = object : LocationCallback(){
        override fun onLocationResult(result: LocationResult) {
            super.onLocationResult(result)
            if(isTracking.value!!){
                result.locations.let { locations ->
                    for (location in locations) {
                        // add locations
                        tracks.value?.apply {
                            last().add(LatLng(location.altitude, location.longitude))
                            tracks.postValue(this)
                        }
                    }
                }
            }
        }
    }


    private fun startTimer(){
        addEmptyTrack()
        timeStart = System.currentTimeMillis()
        isTracking.postValue(true)

        CoroutineScope(Dispatchers.Main).launch {
            while (isTracking.value!!){
                // time difference between now and timeStarted
                lap = System.currentTimeMillis() - timeStart
                // post new lapTime
                timeRun.postValue(time + lap)
                delay(50L)
            }

            time += lap
        }
    }


    private fun pauseService() {
        isTracking.postValue(false)
    }


    private fun postValues(){
        isTracking.postValue(false)
        tracks.postValue(mutableListOf())
        timeRun.postValue(0L)
    }


    private fun killService(){
        pauseService()
        postValues()
        stopSelf()
    }




    @SuppressLint("MissingPermission")
    private fun updateLocationTracking(isTracking: Boolean){
        if(isTracking){
            // get gps info every interval
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