package com.example.activediet.data

data class Nutrients(
    val nutrients: List<Nutrient>
) {
    fun getCalories() = nutrients.first {
        it.name == "Calories"
    }

    fun getFat() = nutrients.first {
        it.name == "Fat"
    }

    fun getProtein() = nutrients.first {
        it.name == "Protein"
    }

    fun getCarbs() = nutrients.first {
        it.name == "Carbohydrates"
    }

    fun updateFood(amount: Int) {
        nutrients.first {
            it.name == "Calories"
        }.amount *= (amount / 100.0f)
        nutrients.first {
            it.name == "Fat"
        }.amount *= (amount / 100.0f)
        nutrients.first {
            it.name == "Protein"
        }.amount *= (amount / 100.0f)
        nutrients.first {
            it.name == "Carbohydrates"
        }.amount *= (amount / 100.0f)
    }
}
