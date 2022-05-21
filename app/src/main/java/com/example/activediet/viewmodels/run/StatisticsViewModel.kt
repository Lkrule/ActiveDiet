package com.example.activediet.viewmodels.run

import androidx.lifecycle.ViewModel
import com.example.activediet.repos.RunRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    val runRepository: RunRepository
): ViewModel() {

    val totalTimeRun = runRepository.getTotalTimeInMillis()
    val totalDistance = runRepository.getTotalDistance()
    val totalCaloriesBurned = runRepository.getTotalCaloriesBurned()
    val totalAvgSpeed = runRepository.getTotalAvgSpeed()
    val runsSortedByDate = runRepository.getAllRunsSortedByDate()
}