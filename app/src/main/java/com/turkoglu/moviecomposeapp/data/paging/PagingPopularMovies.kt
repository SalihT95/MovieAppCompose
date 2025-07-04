package com.turkoglu.moviecomposeapp.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.turkoglu.moviecomposeapp.data.remote.MovieAPI
import com.turkoglu.moviecomposeapp.data.remote.dto.toMovieList
import com.turkoglu.moviecomposeapp.domain.model.Movie
import java.io.IOException
import javax.inject.Inject

class PagingPopularMovies @Inject constructor(private val api: MovieAPI) : PagingSource<Int , Movie>() {
    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return  state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        return try {
            val nextPage = params.key ?: 1
            val popularMovies = api.getPopularMovies(nextPage)
            LoadResult.Page(
                data = popularMovies.toMovieList() ,
                prevKey = if (nextPage == 1) null else nextPage -1 ,
                nextKey = if (popularMovies.toMovieList().isEmpty()) null else popularMovies.page +1
            )

        } catch (e : IOException){
            return LoadResult.Error(e)
        }
    }
}

class PagingPopularMoviesHome @Inject constructor(private val api: MovieAPI) : PagingSource<Int , Movie>() {
    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return  state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        return try {
            val nextPage =  1
            val popularMovies = api.getPopularMovies(nextPage)
            LoadResult.Page(
                data = popularMovies.toMovieList() ,
                prevKey = if (nextPage == 1) null else nextPage -1 ,
                nextKey = if (popularMovies.toMovieList().isEmpty()) nextPage else null
            )

        } catch (e : IOException){
            return LoadResult.Error(e)
        }
    }
}