package com.example.activediet.utilities

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import kotlin.math.log

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        showPushNotification() // implement showing notification in this function
    }
    private fun showPushNotification(){
        print("THIS IS ALARM TEST")
    }
}