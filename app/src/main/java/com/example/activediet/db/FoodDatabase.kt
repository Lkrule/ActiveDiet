package com.example.activediet.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.activediet.data.FoodSearch

@Database(entities = [FoodSearch::class], version = 1)
@TypeConverters(Converters::class)
abstract class FoodDatabase : RoomDatabase() {
    abstract fun getFoodDao(): FoodDao
}