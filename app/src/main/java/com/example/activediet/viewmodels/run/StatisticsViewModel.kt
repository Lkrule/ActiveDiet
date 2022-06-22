package com.example.activediet.viewmodels.run

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.activediet.data.Run
import com.example.activediet.repos.RunRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val runRepository: RunRepository
): ViewModel() {

    val runs = MediatorLiveData<List<Run>>()

    val totalTimeInMs = runRepository.getTotalTimeInMs()
    val totalAvgSpeed = runRepository.getTotalAvgSpeed()
    val totalDist = runRepository.getTotalDist()
    val totalCalsBurned = runRepository.getTotalCalsBurned()

    fun sortBy( query : String): LiveData<List<Run>> {
        return runRepository.allRunsSortedBy(query)
    }

    var sortType = "timestamp"

    init {
        runs.addSource(sortBy("timestamp")) { result ->
            if(sortType == "timestamp") {
                result?.let { runs.value = it }
            }
        }
        runs.addSource(sortBy("speed")) { result ->
            if(sortType == "speed") {
                result?.let { runs.value = it }
            }
        }
        runs.addSource(sortBy("calories")) { result ->
            if(sortType == "calories") {
                result?.let { runs.value = it }
            }
        }
        runs.addSource(sortBy("distance")) { result ->
            if(sortType == "distance") {
                result?.let { runs.value = it }
            }
        }
        runs.addSource(sortBy("time_ms")) { result ->
            if(sortType == "time_ms") {
                result?.let { runs.value = it }
            }
        }

    }

    fun sortRuns(sortType: String) = when(sortType) {
        "timestamp" -> sortBy("timestamp").value?.let { runs.value = it }
        "time_ms" -> sortBy("time_ms").value?.let { runs.value = it }
        "speed" -> sortBy("speed").value?.let { runs.value = it }
        "distance" -> sortBy("distance").value?.let { runs.value = it }
        "calories" -> sortBy("calories").value?.let { runs.value = it }
        else -> {}
    }.also {
        this.sortType = sortType
    }

}