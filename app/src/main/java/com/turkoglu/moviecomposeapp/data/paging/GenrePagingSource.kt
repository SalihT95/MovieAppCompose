package com.turkoglu.moviecomposeapp.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.turkoglu.moviecomposeapp.data.remote.MovieAPI
import com.turkoglu.moviecomposeapp.data.remote.dto.toMovieList
import com.turkoglu.moviecomposeapp.domain.model.Movie
import java.io.IOException

class GenrePagingSource(
    private val api: MovieAPI,
    private val genreId: Int,
    private val isPaginated: Boolean = false
) : PagingSource<Int, Movie>() {

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? = state.anchorPosition

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        return try {
            val nextPage = if (isPaginated) 1 else params.key ?: 1
            val response = api.getGenresMovies(page = nextPage, genre = genreId)
            LoadResult.Page(
                data = response.toMovieList(),
                prevKey = if (nextPage == 1 || isPaginated) null else nextPage - 1,
                nextKey = if (response.toMovieList()
                        .isEmpty() || isPaginated
                ) null else nextPage + 1
            )
        } catch (e: IOException) {
            LoadResult.Error(e)
        }
    }
}
