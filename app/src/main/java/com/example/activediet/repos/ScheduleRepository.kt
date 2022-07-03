package com.example.activediet.repos

import com.example.activediet.data.IngredientSearch
import com.example.activediet.db.IngredientsDao
import javax.inject.Inject

class ScheduleRepository @Inject constructor(
    private val ingredientDao: IngredientsDao
) {
    suspend fun loadProducts(mealID: Int, date: String) =
        ingredientDao.loadByMeal(mealID, date)

    suspend fun removeProduct(ingredient: IngredientSearch) =
        ingredientDao.delete(ingredient)

    fun getProducts() =
        ingredientDao.loadAllMeals()

    suspend fun insertProducts(ingredients: IngredientSearch) =
        ingredientDao.insertAll(ingredients)
}