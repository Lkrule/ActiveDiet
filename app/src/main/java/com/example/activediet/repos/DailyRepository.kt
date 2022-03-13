package com.example.activediet.repos

import com.example.activediet.data.IngredientSearch
import com.example.activediet.db.IngredientsDao
import javax.inject.Inject

class DailyRepository @Inject constructor(
    private val ingredientDao: IngredientsDao
) {
    suspend fun loadProducts(mealID: Int, date: String): List<IngredientSearch> {
        return ingredientDao.loadByMeal(mealID, date)
    }

    suspend fun removeProduct(ingredient: IngredientSearch) {
        ingredientDao.delete(ingredient)
    }
}