package com.example.activediet.db

import androidx.room.TypeConverter
import com.example.activediet.data.Nutrients
import com.google.gson.Gson

class Converters {
    @TypeConverter
    fun stringToNutrients(value: String): Nutrients {
        return Gson().fromJson(value, Nutrients::class.java)
    }

    @TypeConverter
    fun nutrientsToString(nutrients: Nutrients): String {
        return Gson().toJson(nutrients)
    }
}