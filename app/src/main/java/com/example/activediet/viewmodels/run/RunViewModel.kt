package com.example.activediet.viewmodels.run

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.activediet.db.Run
import com.example.activediet.repos.RunRepository
import com.example.activediet.utilities.SortType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class RunViewModel @Inject constructor(
    private val runRepository: RunRepository
) : ViewModel() {

    private val runsSortedByDate = runRepository.allRunsSortedBy("timestamp")
    private val runsSortedByDist = runRepository.allRunsSortedBy("distance")
    private val runsSortedByCalsBurned = runRepository.allRunsSortedBy("calories")
    private val runsSortedByTimeInMs = runRepository.allRunsSortedBy("time_ms")
    private val runsSortedByAvgSpeed = runRepository.allRunsSortedBy("speed")


    val runs = MediatorLiveData<List<Run>>()

    var sortType = SortType.DATE

    init {
        runs.addSource(runsSortedByDate) { result ->
            if (sortType == SortType.DATE) {
                result?.let { runs.value = it }
            }
        }
        runs.addSource(runsSortedByAvgSpeed) { result ->
            if (sortType == SortType.AVG_SPEED) {
                result?.let { runs.value = it }
            }
        }
        runs.addSource(runsSortedByCalsBurned) { result ->
            if (sortType == SortType.CALORIES_BURNED) {
                result?.let { runs.value = it }
            }
        }
        runs.addSource(runsSortedByDist) { result ->
            if (sortType == SortType.DISTANCE) {
                result?.let { runs.value = it }
            }
        }
        runs.addSource(runsSortedByTimeInMs) { result ->
            if (sortType == SortType.RUNNING_TIME) {
                result?.let { runs.value = it }
            }
        }
    }

    fun sortRuns(sortType: SortType) = when (sortType) {
        SortType.DATE -> runsSortedByDate.value?.let { runs.value = it }
        SortType.RUNNING_TIME -> runsSortedByTimeInMs.value?.let { runs.value = it }
        SortType.AVG_SPEED -> runsSortedByAvgSpeed.value?.let { runs.value = it }
        SortType.DISTANCE -> runsSortedByDist.value?.let { runs.value = it }
        SortType.CALORIES_BURNED -> runsSortedByCalsBurned.value?.let { runs.value = it }
    }.also {
        this.sortType = sortType
    }


    fun insertRun(run: Run) = viewModelScope.launch {
        runRepository.insertRun(run)
    }

    fun deleteRun(run: Run) = viewModelScope.launch {
        runRepository.deleteRun(run)
    }

}