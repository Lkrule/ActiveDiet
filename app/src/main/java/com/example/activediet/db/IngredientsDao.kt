package com.example.activediet.db

import androidx.room.*
import com.example.activediet.data.IngredientSearch

@Dao
interface IngredientsDao {
    @Query("SELECT * FROM IngredientSearch WHERE date = (:date) AND meal IN (:meal)")
    suspend fun loadByMeal(meal: Int, date: String): List<IngredientSearch>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg ingredients: IngredientSearch)

    @Delete
    suspend fun delete(ingredient: IngredientSearch)
}