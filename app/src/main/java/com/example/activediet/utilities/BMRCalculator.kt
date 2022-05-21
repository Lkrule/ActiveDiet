package com.example.activediet.utilities

// singleton static object
object BMRCalculator {
    private val BenedictFormula = listOf(1.2, 1.375, 1.55, 1.725, 1.9)
    private val goalFormula = listOf(-300, 0, 300)

    fun validateGender(input: Int): Boolean = input > 0

    fun validateWeight(input: String): Boolean = input.isNotEmpty()

    fun validateHeight(input: String): Boolean = input.isNotEmpty()

    fun validateAge(input: String): Boolean = input.isNotEmpty()

    fun validateActivity(input: Int): Boolean = input > 0

    fun validateGoal(input: Int): Boolean = input > 0

    fun calcBMRForGoal(
        gender: Int,
        weight: Float,
        height: Float,
        age: Int,
        activity: Int,
        goal: Int
    ): Double {
        val bmr = calcBMR(gender, weight, height, age)
        return calcEat(bmr, activity) + goalFormula[goal - 1]
    }

    private fun calcBMR(
        gender: Int,
        weight: Float,
        height: Float,
        age: Int
    ) : Double {
        val genderVar = if(gender == 1) 5 else -161
        return 9.99f * weight + 6.25f * height - 4.92 * age + genderVar
    }

    private fun calcEat(
        bmr: Double,
        activity: Int
    ) : Double {
        return bmr * BenedictFormula[activity - 1]
    }
}