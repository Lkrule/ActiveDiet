package com.example.activediet.data
import com.google.gson.annotations.SerializedName


data class FoodSearchResponse(
    val results: List<FoodSearch>
)

data class FoodSearch(
    val id: Int,
    val name: String,
    @SerializedName("nutrition") val nutrients: Nutrients
)


