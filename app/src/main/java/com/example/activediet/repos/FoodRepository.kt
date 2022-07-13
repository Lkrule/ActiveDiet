package com.example.activediet.repos

import com.example.activediet.data.Food
import com.example.activediet.db.FoodDao
import javax.inject.Inject

class FoodRepository @Inject constructor(
    private val foodDao: FoodDao
) {
    suspend fun loadProducts(mealID: Int, date: String) =
        foodDao.loadByMeal(mealID, date)

    suspend fun removeProduct(food: Food) =
        foodDao.delete(food)

    fun getProducts() =
        foodDao.loadAllMeals()

    suspend fun insertProducts(ingredients: Food) =
        foodDao.insertAll(ingredients)
}