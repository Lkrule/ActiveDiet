package com.example.activediet.viewmodels.food

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

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
        _bmrLiveData.value = calcBMRForGoal(
            gender, weight, height, age, activity, goal
        )
    }

    // Harris-Benedict formula
    private val energyFormula = listOf(1.2, 1.375, 1.55, 1.725, 1.9)
    // goal
    private val goalFormula = listOf(-300, 0, 300)

    private fun calcBMRForGoal(
        gender: Int,
        weight: Float,
        height: Float,
        age: Int,
        activity: Int,
        goal: Int
    ): Double {
        val genderVar = if(gender == 1) 5 else -161
        val bmr = 9.99f * weight + 6.25f * height - 4.92 * age + genderVar
        return bmr * energyFormula[activity - 1] + goalFormula[goal - 1]
    }
}