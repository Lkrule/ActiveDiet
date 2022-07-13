package com.example.activediet.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.activediet.data.Food

@Dao
interface FoodDao {
    @Query("SELECT * FROM food_table WHERE date = (:date) AND meal IN (:meal)")
    suspend fun loadByMeal(meal: Int, date: String): List<Food>

    @Query("SELECT * FROM food_table")
    fun loadAllMeals() : LiveData<List<Food>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg foods: Food)

    @Delete
    suspend fun delete(food: Food)
}