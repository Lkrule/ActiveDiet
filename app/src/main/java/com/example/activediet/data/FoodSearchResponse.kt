package com.example.activediet.data

import androidx.room.Entity

data class FoodSearchResponse(
    val results: List<FoodSearch>
)

@Entity
data class FoodSearch(
    var date: String,
    val id: Int,
    val name: String,
    var amount: Float,
    val unit: String,
    val nutrients: Nutrients
)

