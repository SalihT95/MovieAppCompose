package com.turkoglu.composedeneme.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.turkoglu.composedeneme.data.remote.MovieAPI
import com.turkoglu.composedeneme.data.remote.dto.toMovieList
import com.turkoglu.composedeneme.domain.model.Movie
import java.io.IOException
import javax.inject.Inject

class PagingTopRatedMovies @Inject constructor(private val api: MovieAPI) : PagingSource<Int , Movie>() {
    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        return try {
            val nextPage = params.key ?: 1
            val topratedMovies = api.getTopRatedMovies(nextPage)
            LoadResult.Page(
                data = topratedMovies.toMovieList() ,
                prevKey = if (nextPage == 1 ) null else nextPage -1 ,
                nextKey = if (topratedMovies.toMovieList().isEmpty()) null else topratedMovies.page +1
            )

        }catch (e :IOException){
            LoadResult.Error(e)
        }
    }
}

class PagingTopRatedMoviesHome @Inject constructor(private val api: MovieAPI) : PagingSource<Int , Movie>() {
    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        return try {
            val nextPage =  1
            val topratedMovies = api.getTopRatedMovies(nextPage)
            LoadResult.Page(
                data = topratedMovies.toMovieList() ,
                prevKey = if (nextPage == 1 ) null else nextPage -1 ,
                nextKey = if (topratedMovies.toMovieList().isEmpty()) nextPage else null
            )

        }catch (e :IOException){
            LoadResult.Error(e)
        }
    }
}