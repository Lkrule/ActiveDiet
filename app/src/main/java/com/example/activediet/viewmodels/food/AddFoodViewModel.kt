package com.example.activediet.viewmodels.food


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.activediet.data.IngredientSearch
import com.example.activediet.db.IngredientsDao
import com.example.activediet.repos.ScheduleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddFoodViewModel @Inject constructor(
    private val repo: ScheduleRepository
) : ViewModel() {
    fun addIngredient(meal: Int, date: String, ingredient: IngredientSearch, amount: Int) {
        ingredient.meal = meal
        ingredient.amount = amount.toFloat()
        ingredient.nutrients.updateAll(amount)
        ingredient.date = date
        viewModelScope.launch {
            repo.insertProducts(ingredient)
        }
    }
}