package com.example.activediet.data

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "running_table")
data class Run(
    @PrimaryKey(autoGenerate = true) val id: Int,
    var Image: Bitmap? = null,
    var TimeStamp: Long = 0L,
    var AvgSpeedInKmh: Float = 0f,
    var DistInMeters: Int = 0,
    var TimeInMs: Long = 0L,
    var CalBurned: Int = 0
)