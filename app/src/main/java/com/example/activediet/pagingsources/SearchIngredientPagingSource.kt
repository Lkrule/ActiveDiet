package com.example.activediet.pagingsources

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.activediet.api.FoodAPI
import com.example.activediet.data.IngredientSearch
import java.lang.Exception

class SearchIngredientPagingSource(
    private val api: FoodAPI,
    private val query: String,
    private val metaInformation: Boolean
) : PagingSource<Int, IngredientSearch>() {
    override fun getRefreshKey(state: PagingState<Int, IngredientSearch>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, IngredientSearch> {
        val pos = params.key ?: 0

        return try {
            val response = api.searchIngredients(query, pos, metaInformation)
            val results = response.body()!!.results

            val resultsWithInfo = mutableListOf<IngredientSearch>()


            results.forEach {
                val response = api.getIngredientInfo(id = it.id, amount = 100, unit = "g")
                resultsWithInfo.add(response.body()!!)
            }

            LoadResult.Page(
                data = resultsWithInfo,
                prevKey = if (pos == 0) null else pos - 10,
                nextKey = if (results.isEmpty()) null else pos + 10
            )
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }
}