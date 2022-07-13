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
class HistoryViewModel @Inject constructor(
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

    private val runsSortedByDist = sortBy("distance")
    private val runsSortedByCalsBurned = sortBy("calories")
    private val runsSortedByTimeInMs = sortBy("time_ms")
    private val runsSortedByAvgSpeed = sortBy("speed")

    var type = "time_ms"

    init {
        runs.addSource(runsSortedByAvgSpeed) { result ->
            if(type == "speed") {
                result?.let { runs.value = it }
            }
        }
        runs.addSource(runsSortedByCalsBurned) { result ->
            if(type == "calories") {
                result?.let { runs.value = it }
            }
        }
        runs.addSource(runsSortedByDist) { result ->
            if(type == "distance") {
                result?.let { runs.value = it }
            }
        }
        runs.addSource(runsSortedByTimeInMs) { result ->
            if(type == "time_ms") {
                result?.let { runs.value = it }
            }
        }

    }

    fun sortRuns(sortType: String) = when(sortType) {
        "time_ms" -> runsSortedByTimeInMs.value?.let { runs.value = it }
        "speed" -> runsSortedByAvgSpeed.value?.let { runs.value = it }
        "distance" -> runsSortedByDist.value?.let { runs.value = it }
        "calories" -> runsSortedByCalsBurned.value?.let { runs.value = it }
        else -> {}
    }.also {
        this.type = sortType
    }


    fun remove() {
        if (runs.value?.isNotEmpty() == true) {
            viewModelScope.launch {
                for (run in runs.value!!) {
                    runRepository.deleteRun(run)
                }
            }
        }
    }

}