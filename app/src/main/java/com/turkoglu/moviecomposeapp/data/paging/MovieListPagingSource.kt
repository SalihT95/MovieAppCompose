package com.turkoglu.moviecomposeapp.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.turkoglu.moviecomposeapp.data.remote.dto.MovieListResponseDto
import com.turkoglu.moviecomposeapp.data.remote.dto.toMovieList
import com.turkoglu.moviecomposeapp.domain.model.Movie
import java.io.IOException

class MovieListPagingSource(
    private val apiCall: suspend (page: Int) -> MovieListResponseDto,
    private val isSinglePage: Boolean = false,
    private val cacheProvider: (() -> MovieListResponseDto?)? = null
) : PagingSource<Int, Movie>() {

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? = state.anchorPosition

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        return try {
            val nextPage = if (isSinglePage) 1 else params.key ?: 1
            val response = if (nextPage == 1 && cacheProvider != null) {
                cacheProvider.invoke() ?: apiCall(1)
            } else {
                apiCall(nextPage)
            }
            val movieList = response.toMovieList()

            LoadResult.Page(
                data = movieList,
                prevKey = if (nextPage == 1 || isSinglePage) null else nextPage - 1,
                nextKey = if (movieList.isEmpty() || isSinglePage) null else nextPage + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}
