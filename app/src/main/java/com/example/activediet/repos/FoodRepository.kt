package com.example.activediet.repos

import com.example.activediet.data.FoodSearch
import com.example.activediet.db.FoodDao
import javax.inject.Inject

class FoodRepository @Inject constructor(
    private val foodDao: FoodDao
) {
    suspend fun loadProducts(mealID: Int, date: String) =
        foodDao.loadByMeal(mealID, date)

    suspend fun removeProduct(food: FoodSearch) =
        foodDao.delete(food)

    fun getProducts() =
        foodDao.loadAllMeals()

    suspend fun insertProducts(ingredients: FoodSearch) =
        foodDao.insertAll(ingredients)
}