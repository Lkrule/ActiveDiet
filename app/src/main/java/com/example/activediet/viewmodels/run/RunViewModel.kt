package com.example.activediet.viewmodels.run

import android.graphics.Bitmap
import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.activediet.data.Run
import com.example.activediet.repos.RunRepository
import com.example.activediet.utilities.track
import com.example.activediet.utilities.tracks
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Math.round
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.math.roundToLong


@HiltViewModel
class RunViewModel @Inject constructor(
    private val runRepository: RunRepository
) : ViewModel() {

    fun insertRun(bitmap : Bitmap?,pathPoints : tracks, weight : Float, time : Long) =
        viewModelScope.launch {
            var dist = 0
            for (track in pathPoints){
                dist += calcLength(track).toInt()
            }
            val dateFormat = SimpleDateFormat("dd-MM-yy", Locale.getDefault())
            val date = dateFormat.format(Calendar.getInstance().time)
            val avgSpeed = ((dist / 1000f) / (time / 1000f / 60 / 60) * 10).roundToLong() / 10f
            val cals = ((dist/1000.toFloat())* weight).toInt()
            val run = Run(bitmap,date, avgSpeed, dist, time, cals)
            runRepository.insertRun(run)
        }

    fun calcLength(track: track): Float {
        var distance = 0f
        for (i in 0..track.size - 2){
            val start = track[i]
            val end = track[i + 1]

            val result = FloatArray(1)

            // calculate distance between two coordinates

            Location.distanceBetween(
                start.latitude,
                start.longitude,
                end.latitude,
                end.longitude,
                result
            )
            distance += result[0]
        }
        return  distance
    }


}