package com.example.activediet.viewmodels.food

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.activediet.adapters.FoodAdapter
import com.example.activediet.data.FoodSearch
import com.example.activediet.data.MealTotals
import com.example.activediet.repos.FoodRepository
import com.example.activediet.utilities.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScheduleViewModel @Inject constructor(
    private val repo: FoodRepository
) : ViewModel() , FoodAdapter.FoodProductAdapter{
    private val _productsArray: Array<MutableLiveData<List<FoodSearch>>> =
        Array(5) { MutableLiveData<List<FoodSearch>>() }
    val productsArray: Array<LiveData<List<FoodSearch>>> =
        Array(_productsArray.size) { i -> _productsArray[i] }




    private val products =
        Array<MutableList<FoodSearch>>(Constants.MEALS_COUNT) { mutableListOf() }
    private val totalsList =
        Array(Constants.MEALS_COUNT) { MealTotals(0f, 0f, 0f, 0f) }

    val allProducts = repo.getProducts()


    fun loadProducts(mealID: Int, date: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val products = repo.loadProducts(mealID, date)
            _productsArray[mealID].postValue(products)
        }
    }


    override fun onFoodRemoveClick(food: FoodSearch, mealIndex: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            repo.removeProduct(food)
            _productsArray[mealIndex].value?.let {
                val list = it.toMutableList()
                list.remove(food)
                _productsArray[mealIndex].postValue(list)
            }
        }
    }


}