package com.example.activediet.viewmodels.food

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.activediet.data.IngredientSearch
import com.example.activediet.db.IngredientsDao
import com.example.activediet.repos.SearchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val ingredientDao: IngredientsDao
) : ViewModel() {
    fun addIngredient(meal: Int, date: String, ingredient: IngredientSearch, amount: Int) {
        ingredient.meal = meal
        ingredient.amount = amount.toFloat()
        ingredient.nutrients.updateAll(amount)
        ingredient.date = date
        viewModelScope.launch {
            ingredientDao.insertAll(ingredient)
        }
    }
}