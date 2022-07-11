package com.example.activediet.module

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.example.activediet.api.SpoonacularAPI
import com.example.activediet.db.FoodDatabase
import com.example.activediet.db.FoodDao
import com.example.activediet.db.RunDAO
import com.example.activediet.db.RunDatabase
import com.example.activediet.utilities.Constants.API_KEY
import com.example.activediet.utilities.Constants.BASE_URL
import com.example.activediet.utilities.Constants.FOOD_DATABASE_NAME
import com.example.activediet.utilities.Constants.RUN_DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton



@Module
@InstallIn(SingletonComponent::class)
class AppModule {


    @Provides
    @Singleton
    fun provideAPI() : SpoonacularAPI {
        // build http client
        val okHttpClient = OkHttpClient.Builder().apply {
            addInterceptor(
                Interceptor { chain ->
                    var request = chain.request()
                    val url = request.url.newBuilder().addQueryParameter("apiKey", API_KEY).build()
                    request = request.newBuilder().url(url).build()
                    chain.proceed(request)
                }
            )
        }.build()
        // build retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
        // return Spoonacular API
        return retrofit.create(SpoonacularAPI::class.java)
    }

    // food dao
    @Provides
    @Singleton
    fun provideIngredientsDao(@ApplicationContext context: Context): FoodDao {
        val db = Room.databaseBuilder(
            context,
            FoodDatabase::class.java,
            FOOD_DATABASE_NAME
        ).build()
        return db.getFoodDao()
    }


    // run dao
    @Singleton
    @Provides
    fun provideRunDao(@ApplicationContext context: Context) : RunDAO {
        val db = Room.databaseBuilder(
            context,
            RunDatabase::class.java,
            RUN_DATABASE_NAME
        ).build()
        return db.getRunDao()
    }


    @Provides
    @Singleton
    fun provideSharedPrefs(@ApplicationContext context: Context) : SharedPreferences {
        return context.getSharedPreferences("PREFS", Context.MODE_PRIVATE)
    }

}