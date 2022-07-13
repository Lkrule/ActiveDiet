package com.example.activediet.viewmodels.food

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.activediet.adapters.FoodAdapter
import com.example.activediet.data.Food
import com.example.activediet.data.Meal
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
    private val _productsArray: Array<MutableLiveData<List<Food>>> =
        Array(5) { MutableLiveData<List<Food>>() }
    val productsArray: Array<LiveData<List<Food>>> =
        Array(_productsArray.size) { i -> _productsArray[i] }





    val allProducts = repo.getProducts()


    fun loadProducts(mealID: Int, date: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val products = repo.loadProducts(mealID, date)
            _productsArray[mealID].postValue(products)
        }
    }


    override fun onFoodRemoveClick(food: Food) {
        CoroutineScope(Dispatchers.IO).launch {
            repo.removeProduct(food)
            _productsArray[food.meal].value?.let {
                val list = it.toMutableList()
                list.remove(food)
                _productsArray[food.meal].postValue(list)
            }
        }
    }
}