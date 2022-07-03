package com.example.activediet.utilities

import android.graphics.Color
import com.google.android.gms.maps.model.LatLng

typealias track = MutableList<LatLng>
typealias tracks = MutableList<track>

object Constants {

    const val RUNNING_DATABASE_NAME = "running_db"
    const val REQUEST_CODE_LOCATION_PERMISSION = 0
    const val KEY_FIRST_TIME_TOGGLE = "KEY_FIRST_TIME_TOGGLE"

    // shared preferences
    const val KEY_NAME = "KEY_NAME"
    const val KEY_GENDER = "KEY_GENDER"
    const val KEY_WEIGHT = "KEY_WEIGHT"
    const val KEY_HEIGHT = "KEY_HEIGHT"
    const val KEY_AGE = "KEY_AGE"
    const val MEALS_COUNT = 5


    const val MAP_ZOOM = 15F
    const val BMR = "BMR"

    const val BASE_URL = "https://api.spoonacular.com/"
    const val API_KEY = "005f2b02140b4c28a6c1da806cb27c76"
    const val GOOGLE_KEY = "AIzaSyD3JDRSAea-I2E2KRJmJb0GxIhTZJ55LB0"

    // service
    const val ACTION_START_OR_RESUME_SERVICE = "ACTION_START_OR_RESUME_SERVICE"
    const val ACTION_PAUSE_SERVICE = "ACTION_PAUSE_SERVICE"
    const val ACTION_STOP_SERVICE = "ACTION_STOP_SERVICE"
}