package com.example.activediet.viewmodels.run

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.activediet.data.Run
import com.example.activediet.repos.RunRepository
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


    fun insertRun(run: Run) = viewModelScope.launch {
        runRepository.insertRun(run)
    }

    fun deleteRun(run: Run) = viewModelScope.launch {
        runRepository.deleteRun(run)
    }

}