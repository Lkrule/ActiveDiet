package com.example.activediet.data

data class Meal(
    var name: String,
    var cals: Float,
    var fats: Float,
    var carbs: Float,
    var proteins: Float
) {
    fun clearAll() {
        cals = 0f
        fats = 0f
        carbs = 0f
        proteins = 0f
    }

    fun update(cals: Float, fats: Float, carbs: Float, proteins: Float) {
        this.cals = cals
        this.fats = fats
        this.carbs = carbs
        this.proteins = proteins
    }
}
