package com.example.activediet.repos

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.activediet.api.FoodAPI
import com.example.activediet.data.IngredientSearch
import com.example.activediet.pagingsources.SearchIngredientPagingSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchRepository @Inject constructor(
    private val api: FoodAPI
) {
    fun searchIngredients(
        query: String,
        metaInformation: Boolean
    ): Flow<PagingData<IngredientSearch>> {
        return Pager(
            PagingConfig(
                pageSize = 10,
                enablePlaceholders = false,
                maxSize = 100
            )
        ) {
            SearchIngredientPagingSource(api, query, metaInformation)
        }.flow
    }
}