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

    private val runsSortedByDate = sortBy("timestamp")
    private val runsSortedByDist = sortBy("distance")
    private val runsSortedByCalsBurned = sortBy("calories")
    private val runsSortedByTimeInMs = sortBy("time_ms")
    private val runsSortedByAvgSpeed = sortBy("speed")

    var sortType = "timestamp"

    init {
        runs.addSource(runsSortedByDate) { result ->
            if(sortType == "timestamp") {
                result?.let { runs.value = it }
            }
        }
        runs.addSource(runsSortedByAvgSpeed) { result ->
            if(sortType == "speed") {
                result?.let { runs.value = it }
            }
        }
        runs.addSource(runsSortedByCalsBurned) { result ->
            if(sortType == "calories") {
                result?.let { runs.value = it }
            }
        }
        runs.addSource(runsSortedByDist) { result ->
            if(sortType == "distance") {
                result?.let { runs.value = it }
            }
        }
        runs.addSource(runsSortedByTimeInMs) { result ->
            if(sortType == "time_ms") {
                result?.let { runs.value = it }
            }
        }

    }

    fun sortRuns(sortType: String) = when(sortType) {
        "timestamp" -> runsSortedByDate.value?.let { runs.value = it }
        "time_ms" -> runsSortedByTimeInMs.value?.let { runs.value = it }
        "speed" -> runsSortedByAvgSpeed.value?.let { runs.value = it }
        "distance" -> runsSortedByDist.value?.let { runs.value = it }
        "calories" -> runsSortedByCalsBurned.value?.let { runs.value = it }
        else -> {}
    }.also {
        this.sortType = sortType
    }


    fun remove(){
        viewModelScope.launch{
            for(run in runs.value!!) {
                runRepository.deleteRun(run)
            }
        }
    }


}