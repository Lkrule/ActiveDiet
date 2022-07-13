package com.example.activediet.viewmodels.run

import android.graphics.Bitmap
import android.location.Location
import android.location.LocationManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.activediet.data.Run
import com.example.activediet.repos.RunRepository
import com.example.activediet.utilities.track
import com.example.activediet.utilities.tracks
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.math.roundToLong


@HiltViewModel
class RunViewModel @Inject constructor(
    private val runRepository: RunRepository
) : ViewModel() {

    fun insertRun(bitmap : Bitmap?, tracks : tracks, weight : Float, ms : Long) =
        viewModelScope.launch {
            // dist in meters
            var dist = 0
            // every pause is trac
            for (track in tracks){
                dist += calcLength(track).toInt()
            }
            val dateFormat = SimpleDateFormat("dd-MM-yy", Locale.getDefault())
            val date = dateFormat.format(Calendar.getInstance().time)
            val avgSpeed = ((dist / 1000f) / ((ms / 1000f) / 3600) * 10).roundToLong() / 10f
            val cals = ((dist/1000.toFloat())* weight).toInt()
            val run = Run(bitmap,date, avgSpeed, dist, ms, cals)
            runRepository.insertRun(run)
        }

    private fun calcLength(track: track): Float {
        var distance = 0f
        val loc1 = Location(LocationManager.GPS_PROVIDER)
        val loc2 = Location(LocationManager.GPS_PROVIDER)
        for (i in 0..track.size - 2){
            val start = track[i]
            val end = track[i + 1]

            // calculate distance between two coordinates
            loc1.latitude = start.latitude
            loc1.longitude = start.longitude
            loc2.latitude = end.latitude
            loc2.longitude = end.longitude

            distance += loc1.distanceTo(loc2)
        }
        return  distance
    }


}