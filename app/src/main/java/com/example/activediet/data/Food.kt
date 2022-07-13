package com.example.activediet.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "food_table")
data class Food (
    @PrimaryKey(autoGenerate = true) val key: Int,
    var date: String,
    var meal: Int,
    val name: String,
    var amount: Int,
    val cals: Float,
    val carbs: Float,
    val proteins: Float,
    val fats: Float
)