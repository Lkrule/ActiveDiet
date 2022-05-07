package com.example.activediet.module

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.example.activediet.api.FoodAPI
import com.example.activediet.db.AppDatabase
import com.example.activediet.db.IngredientsDao
import com.example.activediet.db.RunningDatabase
import com.example.activediet.utilities.run.Constants.KEY_FIRST_TIME_TOGGLE
import com.example.activediet.utilities.run.Constants.KEY_NAME
import com.example.activediet.utilities.run.Constants.KEY_WEIGHT
import com.example.activediet.utilities.run.Constants.RUNNING_DATABASE_NAME
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

const val BASE_URL = "https://api.spoonacular.com/"
const val API_KEY = "005f2b02140b4c28a6c1da806cb27c76"

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().apply {
            addInterceptor(
                Interceptor { chain ->
                    var request = chain.request()
                    val url = request.url.newBuilder().addQueryParameter("apiKey", API_KEY).build()
                    request = request.newBuilder().url(url).build()
                    chain.proceed(request)
                }
            )
        }.build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun provideAPI(retrofit: Retrofit) : FoodAPI {
        return retrofit.create(FoodAPI::class.java)
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "ingredients_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideIngredientsDao(db: AppDatabase): IngredientsDao {
        return db.ingredientDao()
    }

    @Provides
    @Singleton
    fun provideSharedPrefs(@ApplicationContext context: Context) : SharedPreferences {
        return context.getSharedPreferences("PREFS", Context.MODE_PRIVATE)
    }

    // test

    @Singleton
    @Provides
    fun provideRunningDatabase(
        @ApplicationContext app: Context
    ) = Room.databaseBuilder(
        app,
        RunningDatabase::class.java,
        RUNNING_DATABASE_NAME
    ).build()

    @Singleton
    @Provides
    fun provideRunDao(db: RunningDatabase) = db.getRunDao()


    @Singleton
    @Provides
    fun provideName(sharedPref: SharedPreferences) = sharedPref.getString(KEY_NAME, "") ?: ""

    @Singleton
    @Provides
    fun provideWeight(sharedPref: SharedPreferences) = sharedPref.getFloat(KEY_WEIGHT, 80f)

    @Singleton
    @Provides
    fun provideFirstTimeToggle(sharedPref: SharedPreferences) =
        sharedPref.getBoolean(KEY_FIRST_TIME_TOGGLE, true)
}