package com.turkoglu.composedeneme.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.turkoglu.composedeneme.data.remote.MovieAPI
import com.turkoglu.composedeneme.domain.model.Search
import retrofit2.HttpException
import java.io.IOException

class SearchPagingSource(private val api: MovieAPI, private val query: String) :
    PagingSource<Int, Search>() {
    override fun getRefreshKey(state: PagingState<Int, Search>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Search> {
        return try {
            val nextPage = params.key ?: 1
            val searchResultList = api.getSearchMovies(nextPage, query)
            LoadResult.Page(
                data = searchResultList.searches,
                prevKey = if (nextPage == 1) null else nextPage - 1,
                nextKey = if (searchResultList.searches.isEmpty()) null else searchResultList.page + 1
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }
}