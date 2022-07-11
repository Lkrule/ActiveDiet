package com.example.activediet.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class FoodSearch (
    @PrimaryKey(autoGenerate = true) val key: Int,
    var date: String,
    val id: Int,
    var meal: Int,
    val name: String,
    var amount: Float,
    val unit: String,
    @SerializedName("nutrition") val nutrients: Nutrients
)