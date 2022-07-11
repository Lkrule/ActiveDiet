package com.example.activediet.repos

import com.example.activediet.data.FoodSearch
import com.example.activediet.db.FoodDao
import javax.inject.Inject

class ScheduleRepository @Inject constructor(
    private val ingredientDao: FoodDao
) {
    suspend fun loadProducts(mealID: Int, date: String) =
        ingredientDao.loadByMeal(mealID, date)

    suspend fun removeProduct(food: FoodSearch) =
        ingredientDao.delete(food)

    fun getProducts() =
        ingredientDao.loadAllMeals()

    suspend fun insertProducts(ingredients: FoodSearch) =
        ingredientDao.insertAll(ingredients)
}