package com.example.activediet.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.activediet.data.FoodSearch

@Dao
interface FoodDao {
    @Query("SELECT * FROM FoodSearch WHERE date = (:date) AND meal IN (:meal)")
    suspend fun loadByMeal(meal: Int, date: String): List<FoodSearch>

    @Query("SELECT * FROM FoodSearch")
    fun loadAllMeals() : LiveData<List<FoodSearch>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg foods: FoodSearch)

    @Delete
    suspend fun delete(food: FoodSearch)
}