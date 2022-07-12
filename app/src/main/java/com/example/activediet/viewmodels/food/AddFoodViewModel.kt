package com.example.activediet.viewmodels.food


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.activediet.data.FoodSearch
import com.example.activediet.repos.FoodRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddFoodViewModel @Inject constructor(
    private val repo: FoodRepository
) : ViewModel() {
    fun addIngredient(meal: Int, date: String, food: FoodSearch, amount: Int) {
        food.meal = meal
        food.amount = amount.toFloat()
        food.nutrients.updateFood(amount)
        food.date = date
        viewModelScope.launch {
            repo.insertProducts(food)
        }
    }
}