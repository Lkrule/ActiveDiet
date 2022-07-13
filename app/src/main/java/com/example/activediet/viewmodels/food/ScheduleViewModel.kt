package com.example.activediet.viewmodels.food

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.activediet.adapters.FoodAdapter
import com.example.activediet.adapters.MealsAdapter
import com.example.activediet.data.Food
import com.example.activediet.repos.FoodRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScheduleViewModel @Inject constructor(
    private val repo: FoodRepository
) : ViewModel() , FoodAdapter.ListenerFoodAdapter{
    private val _foodsArray: Array<MutableLiveData<List<Food>>> =
        Array(5) { MutableLiveData<List<Food>>() }
    val foodsArray: Array<LiveData<List<Food>>> =
        Array(_foodsArray.size) { i -> _foodsArray[i] }





    val allProducts = repo.getAllFoods()


    fun updateMeals(index: Int, date: String, adapter: MealsAdapter) {
        CoroutineScope(Dispatchers.IO).launch {
            val foods = repo.loadFoods(index, date)
            _foodsArray[index].postValue(foods)
        }
    }


    override fun onFoodRemoveClick(food: Food) {
        CoroutineScope(Dispatchers.IO).launch {
            repo.removeFood(food)
            _foodsArray[food.meal].value?.let {
                val list = it.toMutableList()
                list.remove(food)
                _foodsArray[food.meal].postValue(list)
            }
        }
    }
}