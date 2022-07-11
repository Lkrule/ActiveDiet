package com.example.activediet.api

import com.example.activediet.data.FoodSearch
import com.example.activediet.data.FoodSearchResponse
import com.example.activediet.utilities.Constants.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

    interface SpoonacularAPI {
    @GET("/food/ingredients/search?apiKey=${API_KEY}&number=10")
    suspend fun searchIngredients(@Query("query") ingredient: String)
    : Response<FoodSearchResponse>

    @GET("/food/ingredients/{id}/information/?apiKey=${API_KEY}&unit=g&amount=100")
    suspend fun getIngredientInfo(@Path("id") id: Int)
    : Response<FoodSearch>
}