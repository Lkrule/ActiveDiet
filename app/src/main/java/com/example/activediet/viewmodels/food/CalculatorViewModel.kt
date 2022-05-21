package com.example.activediet.viewmodels.food

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.activediet.utilities.BMRCalculator

class CalculatorViewModel : ViewModel() {
    private val _bmrLiveData: MutableLiveData<Double> = MutableLiveData()
    val bmrLiveData: LiveData<Double> = _bmrLiveData

    fun calculateBMR(
        gender: Int,
        weight: Float,
        height: Float,
        age: Int,
        activity: Int,
        goal: Int
    ) {
        _bmrLiveData.value = BMRCalculator.calcBMRForGoal(
            gender, weight, height, age, activity, goal
        )
    }
}