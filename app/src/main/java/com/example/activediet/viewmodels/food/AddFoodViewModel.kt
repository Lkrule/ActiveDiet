package com.example.activediet.viewmodels.food


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.activediet.data.Food
import com.example.activediet.repos.FoodRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddFoodViewModel @Inject constructor(
    private val repo: FoodRepository
) : ViewModel() {
    fun addFood(date: String, meal: Int, name: String,
                amount: Int,cals: Float,carbs: Float,
                proteins: Float,fats: Float ) {

        val food = Food(0,date,meal,name
            ,amount,
            cals * (amount / 100.0f),
            carbs * (amount / 100.0f),
            proteins * (amount / 100.0f),
            fats * (amount / 100.0f))
        viewModelScope.launch {
            repo.insertProducts(food)
        }
    }
}