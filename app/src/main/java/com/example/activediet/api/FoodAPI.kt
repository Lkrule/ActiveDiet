package com.example.activediet.api

import com.example.activediet.data.IngredientSearch
import com.example.activediet.data.IngredientSearchResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface FoodAPI {
    @GET("food/ingredients/search")
    suspend fun searchIngredients(
        @Query("query") query: String,
        @Query("offset") offset: Int,
        @Query("metaInformation") metaInformation: Boolean
    ) : Response<IngredientSearchResponse>

    @GET("food/ingredients/{id}/information")
    suspend fun getIngredientInfo(
        @Path("id") id: Int,
        @Query("amount") amount: Int,
        @Query("unit") unit: String
    ) : Response<IngredientSearch>
}