package com.example.activediet.viewmodels.run

import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.activediet.data.Run
import com.example.activediet.repos.RunRepository
import com.example.activediet.utilities.track
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class RunViewModel @Inject constructor(
    private val runRepository: RunRepository
) : ViewModel() {

    fun insertRun(run: Run) = viewModelScope.launch {
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