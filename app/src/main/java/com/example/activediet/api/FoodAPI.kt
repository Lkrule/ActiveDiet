package com.example.activediet.api

import com.example.activediet.data.IngredientSearch
import com.example.activediet.data.IngredientSearchResponse
import com.example.activediet.utilities.Constants.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface FoodAPI {
    @GET("/food/ingredients/search?apiKey=${API_KEY}&number=10")
    suspend fun searchIngredients(@Query("query") ingredient: String)
    : Response<IngredientSearchResponse>

    @GET("/food/ingredients/{id}/information/?apiKey=${API_KEY}&unit=g&amount=100")
    suspend fun getIngredientInfo(@Path("id") id: Int)
    : Response<IngredientSearch>
}