package com.example.activediet.viewmodels.food

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.activediet.api.SpoonacularAPI
import com.example.activediet.data.FoodSearch
import com.example.activediet.data.Nutrient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val api: SpoonacularAPI
) : ViewModel() {
    var isTextChange = false

    private var productsResults = listOf<FoodSearch>()

    private var _products = MutableLiveData<List<String>>()
    val products: LiveData<List<String>> = _products

    private var _nutrients = MutableLiveData<List<Nutrient>>()
    val nutrients: LiveData<List<Nutrient>> = _nutrients


    fun afterTextChanged(text: String) {
        isTextChange = true
        viewModelScope.launch {
            val response = api.searchIngredients(text)
            productsResults = response.body()!!.results
            val productsList = mutableListOf<String>()
            productsResults.forEach {
                productsList.add(it.name)
            }
            _products.value = productsList
        }
    }

    fun launch(position: Int) {
        viewModelScope.launch {
            val response = api.getIngredientInfo(productsResults[position].id)
            response.body()!!.nutrients
        }
    }
}