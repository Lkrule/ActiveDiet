package com.example.activediet.viewmodels.food

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.activediet.db.IngredientsDao
import com.example.activediet.utilities.Constants
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.math.roundToInt

@HiltViewModel
class CalculatorViewModel @Inject constructor(
        var sharedPrefs: SharedPreferences
        ) : ViewModel() {

    private val _bmr: MutableLiveData<Double> = MutableLiveData()
    val bmr: LiveData<Double> = _bmr


    private val _gender = sharedPrefs.getInt(Constants.KEY_GENDER, 0)
    private val _weight = sharedPrefs.getFloat(Constants.KEY_WEIGHT, 0f).toString().toFloat()
    private val  _height = sharedPrefs.getFloat(Constants.KEY_HEIGHT, 0f).toString().toFloat()
    private val  _age = sharedPrefs.getFloat(Constants.KEY_AGE, 0f).toString().toFloat().toInt()


    // bmr
    fun calcBMR(activity: Int, goal: Int) {
        val genderVar = if(_gender == 1) 5 else -161
        val bmr = 9.99f * _weight + 6.25f * _height - 4.92 * _age + genderVar
        _bmr.value =  bmr * energyFormula[activity - 1] + goalFormula[goal - 1]
    }

    // Harris-Benedict formula
    private val energyFormula = listOf(1.2, 1.375, 1.55, 1.725, 1.9)
    // goal
    private val goalFormula = listOf(-300, 0, 300)

    fun calcBMI(): Float {
        val bmi = ( _weight / (_height/100 * _height/ 100))
        return  ((bmi * 100.0).roundToInt() / 100.0).toFloat()
    }

}