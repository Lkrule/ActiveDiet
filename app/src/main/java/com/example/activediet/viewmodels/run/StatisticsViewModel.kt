package com.example.activediet.viewmodels.run

import androidx.lifecycle.ViewModel
import com.example.activediet.repos.RunRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val runRepository: RunRepository
): ViewModel() {

    val totalTimeInMs = runRepository.getTotalTimeInMs()
    val totalAvgSpeed = runRepository.getTotalAvgSpeed()
    val runsSortedByDate = runRepository.allRunsSortedBy("timestamp")
    val totalDist = runRepository.getTotalDist()
    val totalCalsBurned = runRepository.getTotalCalsBurned()

}