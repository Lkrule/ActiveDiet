package com.example.activediet.repos

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.activediet.api.FoodAPI
import com.example.activediet.data.IngredientSearch
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchRepository @Inject constructor(
    private val api: FoodAPI
) {
    fun searchIngredients(
        query: String,
        metaInformation: Boolean
    ) { return }
}