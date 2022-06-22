package com.example.activediet.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import com.example.activediet.utilities.Constants.NOTIFICATION_CHANNEL_ID
import com.example.activediet.utilities.Constants.NOTIFICATION_CHANNEL_NAME
import com.example.activediet.utilities.tracks
import com.google.android.gms.location.FusedLocationProviderClient
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



    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            when(it.action){
                "ACTION_START_OR_RESUME_SERVICE" -> {

                    if(isFirstRun){
                        isFirstRun = false
                    }
                    else{
                        Timber.d("ACTION_START_OR_RESUME_SERVICE")
                    }

                }
                "ACTION_PAUSE_SERVICE" -> {
                    Timber.d("ACTION_PAUSE_SERVICE")
                }
                "ACTION_STOP_SERVICE" -> {

                }
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }




    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager: NotificationManager){
        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME, NotificationManager.IMPORTANCE_LOW)

        notificationManager.createNotificationChannel(channel)
    }
}