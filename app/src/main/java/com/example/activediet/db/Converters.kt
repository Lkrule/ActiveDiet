package com.example.activediet.db

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.room.TypeConverter
import com.example.activediet.data.Nutrients
import com.google.gson.Gson
import java.io.ByteArrayOutputStream

class Converters {
    @TypeConverter
    fun stringToNutrients(value: String): Nutrients {
        return Gson().fromJson(value, Nutrients::class.java)
    }

    @TypeConverter
    fun nutrientsToString(nutrients: Nutrients): String {
        return Gson().toJson(nutrients)
    }

    @TypeConverter
    fun toBitmap(bytes: ByteArray): Bitmap {
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    }

    @TypeConverter
    fun fromBitmap(bmp: Bitmap): ByteArray {
        val outputStream = ByteArrayOutputStream()
        bmp.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        return outputStream.toByteArray()
    }
}