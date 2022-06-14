package com.example.activediet.repos

import com.example.activediet.api.FoodAPI
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.internal.wait
import javax.inject.Inject

class SearchRepository @Inject constructor(
    private val api: FoodAPI
) {
    fun searchIngredients( query: String) {
        CoroutineScope(Dispatchers.Default).launch {
            val ingredients = api.searchIngredients(query)
            val ing = ingredients.body()?.results
            if(ing != null) {
                val id = ing[0].id
                val ingredientInfo = api.getIngredientInfo(id)
                val info = ingredientInfo.body()
            }
        }
    }
}