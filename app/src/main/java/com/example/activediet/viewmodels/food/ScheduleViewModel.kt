package com.example.activediet.viewmodels.food

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.activediet.data.IngredientSearch
import com.example.activediet.repos.ScheduleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScheduleViewModel @Inject constructor(
    private val repo: ScheduleRepository
) : ViewModel() {
    private val _productsArray: Array<MutableLiveData<List<IngredientSearch>>> =
        Array(5) { MutableLiveData<List<IngredientSearch>>() }
    val productsArray: Array<LiveData<List<IngredientSearch>>> =
        Array(_productsArray.size) { i -> _productsArray[i] }


    val test = repo.getProducts()


    fun loadProducts(mealID: Int, date: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val products = repo.loadProducts(mealID, date)
            _productsArray[mealID].postValue(products)
        }
    }

    fun deleteProduct(ingredient: IngredientSearch, mealIndex: Int) {
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