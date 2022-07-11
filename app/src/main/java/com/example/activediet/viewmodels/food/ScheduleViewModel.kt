package com.example.activediet.viewmodels.food

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.activediet.adapters.MealsAdapter
import com.example.activediet.adapters.ProductAdapter
import com.example.activediet.data.IngredientSearch
import com.example.activediet.data.MealTotals
import com.example.activediet.repos.ScheduleRepository
import com.example.activediet.utilities.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScheduleViewModel @Inject constructor(
    private val repo: ScheduleRepository
) : ViewModel() , ProductAdapter.ProductAdapterListener{
    private val _productsArray: Array<MutableLiveData<List<IngredientSearch>>> =
        Array(5) { MutableLiveData<List<IngredientSearch>>() }
    val productsArray: Array<LiveData<List<IngredientSearch>>> =
        Array(_productsArray.size) { i -> _productsArray[i] }




    private val products =
        Array<MutableList<IngredientSearch>>(Constants.MEALS_COUNT) { mutableListOf() }
    private val totalsList =
        Array(Constants.MEALS_COUNT) { MealTotals(0f, 0f, 0f, 0f) }

    val allProducts = repo.getProducts()


    fun loadProducts(mealID: Int, date: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val products = repo.loadProducts(mealID, date)
            _productsArray[mealID].postValue(products)
        }
    }


    override fun onProductRemoveClick(ingredient: IngredientSearch, mealIndex: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            repo.removeProduct(ingredient)
            _productsArray[mealIndex].value?.let {
                val list = it.toMutableList()
                list.remove(ingredient)
                _productsArray[mealIndex].postValue(list)
            }
        }
    }


}