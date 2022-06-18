package com.example.activediet.viewmodels.food

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.activediet.api.FoodAPI
import com.example.activediet.data.IngredientSearch
import com.example.activediet.db.IngredientsDao
import com.example.activediet.repos.DailyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val api: FoodAPI
) : ViewModel() {
    var isTextChange = false
    var isProductPicked = false

    private var productsResult = listOf<IngredientSearch>()

    private var _products = MutableLiveData<List<String>>()
    val products: LiveData<List<String>> = _products

    private var _productId = MutableLiveData<Int>()
    val productId: LiveData<Int> = _productId


    fun afterTextChanged(text: String) {
        isTextChange = true
        CoroutineScope(Dispatchers.IO).launch {
            val response = api.searchIngredients(text)
            val products = response.body()!!.results
            val results = mutableListOf<IngredientSearch>()
            products.forEach {
                val product = api.getIngredientInfo(id = it.id)
                results.add(product.body()!!)
            }
        }
    }
}