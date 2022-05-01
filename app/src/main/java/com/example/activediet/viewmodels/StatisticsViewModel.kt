package com.example.activediet.viewmodels

import androidx.lifecycle.ViewModel
import com.example.activediet.repos.RunRepository
import javax.inject.Inject

class StatisticsViewModel @Inject constructor(
    val runRepository: RunRepository
): ViewModel() {

    val totalTimeRun = runRepository.getTotalTimeInMillis()
    val totalDistance = runRepository.getTotalDistance()
    val totalCaloriesBurned = runRepository.getTotalCaloriesBurned()
    val totalAvgSpeed = runRepository.getTotalAvgSpeed()
    val runsSortedByDate = runRepository.getAllRunsSortedByDate()
}