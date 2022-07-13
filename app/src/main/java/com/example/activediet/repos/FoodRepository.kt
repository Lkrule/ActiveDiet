package com.example.activediet.repos

import com.example.activediet.data.Food
import com.example.activediet.db.FoodDao
import javax.inject.Inject

class FoodRepository @Inject constructor(
    private val foodDao: FoodDao
) {
    suspend fun loadFoods(mealID: Int, date: String) = foodDao.loadByMeal(mealID, date)

    suspend fun removeFood(food: Food) = foodDao.delete(food)

    fun getAllFoods() = foodDao.loadAllFoods()

    suspend fun insertFood(food: Food) = foodDao.insertAll(food)
}