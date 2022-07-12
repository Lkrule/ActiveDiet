package com.example.activediet.data

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "running_table")
data class Run(
    var image: Bitmap? = null,
    var date: String,
    var speed: Float = 0f,
    var dist: Int = 0,
    var time: Long = 0L,
    var cals: Int = 0
){
    @PrimaryKey(autoGenerate = true) var id: Int = 0
}