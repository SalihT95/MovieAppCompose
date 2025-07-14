package com.turkoglu.moviecomposeapp.data.repo

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.turkoglu.moviecomposeapp.data.paging.PagingActionMovies
import com.turkoglu.moviecomposeapp.data.paging.PagingActionMoviesHome
import com.turkoglu.moviecomposeapp.data.paging.PagingAnimationMovies
import com.turkoglu.moviecomposeapp.data.paging.PagingAnimationMoviesHome
import com.turkoglu.moviecomposeapp.data.paging.PagingComedyMovies
import com.turkoglu.moviecomposeapp.data.paging.PagingComedyMoviesHome
import com.turkoglu.moviecomposeapp.data.paging.PagingDramaMovies
import com.turkoglu.moviecomposeapp.data.paging.PagingDramaMoviesHome
import com.turkoglu.moviecomposeapp.data.paging.PagingFantasyMovies
import com.turkoglu.moviecomposeapp.data.paging.PagingFantasyMoviesHome
import com.turkoglu.moviecomposeapp.data.paging.PagingHistoryMovies
import com.turkoglu.moviecomposeapp.data.paging.PagingHistoryMoviesHome
import com.turkoglu.moviecomposeapp.data.paging.PagingNowPlaying
import com.turkoglu.moviecomposeapp.data.paging.PagingNowPlayingHome
import com.turkoglu.moviecomposeapp.data.paging.PagingPopularMovies
import com.turkoglu.moviecomposeapp.data.paging.PagingPopularMoviesHome
import com.turkoglu.moviecomposeapp.data.paging.PagingTopRatedMovies
import com.turkoglu.moviecomposeapp.data.paging.PagingTopRatedMoviesHome
import com.turkoglu.moviecomposeapp.data.paging.PagingUpComingMovies
import com.turkoglu.moviecomposeapp.data.paging.PagingUpComingMoviesHome
import com.turkoglu.moviecomposeapp.data.paging.PagingWarMovies
import com.turkoglu.moviecomposeapp.data.paging.PagingWarMoviesHome
import com.turkoglu.moviecomposeapp.data.paging.SearchPagingSource
import com.turkoglu.moviecomposeapp.data.remote.MovieAPI
import com.turkoglu.moviecomposeapp.data.remote.dto.CreditsDto
import com.turkoglu.moviecomposeapp.data.remote.dto.MovieDetailDto
import com.turkoglu.moviecomposeapp.domain.model.Movie
import com.turkoglu.moviecomposeapp.domain.model.Search
import com.turkoglu.moviecomposeapp.util.Resource
import kotlinx.coroutines.flow.Flow
import java.io.IOException

import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(private val api : MovieAPI){
    fun getMovies(useIncreasingPage: Boolean): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(enablePlaceholders = false , pageSize = 100) ,
            pagingSourceFactory = {
                if (useIncreasingPage){
                    PagingPopularMoviesHome(api)
                }else PagingPopularMovies(api)
            }
        ).flow
    }

    fun getTopRatedMovies(useIncreasingPage: Boolean): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(enablePlaceholders = false , pageSize = 100) ,
            pagingSourceFactory = {
               if (useIncreasingPage){
                   PagingTopRatedMoviesHome(api)
               }else PagingTopRatedMovies(api)
            }
        ).flow
    }

    fun getNowPlayingMovies(useIncreasingPage: Boolean): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(enablePlaceholders = false , pageSize = 100) ,
            pagingSourceFactory = {
                if (useIncreasingPage){
                    PagingNowPlayingHome(api)
                }else PagingNowPlaying(api)
            }
        ).flow
    }

    fun getUpcomingMovies(useIncreasingPage: Boolean): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(enablePlaceholders = false , pageSize = 100) ,
            pagingSourceFactory = {
                if (useIncreasingPage){
                    PagingUpComingMoviesHome(api)
                }else PagingUpComingMovies(api)
            }
        ).flow
    }

    suspend fun getMovieDetail(movieId: Int): MovieDetailDto {
        return api.getMovieDetail(movieId = movieId)
    }

    suspend fun getMovieCasts(movieId : Int) : Resource<CreditsDto>{
        val response = try {
           api.getMovieCredits(movieId = movieId)
        }catch (e: IOException){
            return Resource.Error(message = "Unknown error occurred" )
        }
        return Resource.Success(response)
    }

    fun multiSearch(queryParam: String): Flow<PagingData<Search>> {
        return Pager(
            config = PagingConfig(enablePlaceholders = false, pageSize = 27),
            pagingSourceFactory = {
                SearchPagingSource(api, queryParam)
            }
        ).flow
    }

    fun getActionMovies(useIncreasingPage: Boolean): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(enablePlaceholders = false , pageSize = 100) ,
            pagingSourceFactory = {
                if (useIncreasingPage){
                    PagingActionMoviesHome(api)
                }else PagingActionMovies(api)
            }
        ).flow
    }

    fun getAnimationMovies(useIncreasingPage: Boolean):Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(enablePlaceholders = false , pageSize = 100) ,
            pagingSourceFactory = {
                if (useIncreasingPage){
                    PagingAnimationMoviesHome(api)
                }else PagingAnimationMovies(api)
            }
        ).flow
    }

    fun getComedyMovies(useIncreasingPage: Boolean): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(enablePlaceholders = false , pageSize = 100) ,
            pagingSourceFactory = {
                if (useIncreasingPage) {
                    PagingComedyMoviesHome(api)
                }else PagingComedyMovies(api)
            }
        ).flow
    }

    fun getDramaMovies(useIncreasingPage: Boolean): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(enablePlaceholders = false , pageSize = 100) ,
            pagingSourceFactory = {
                if (useIncreasingPage){
                    PagingDramaMoviesHome(api)
                }else PagingDramaMovies(api)
            }
        ).flow
    }

    fun getFantasyMovies(useIncreasingPage: Boolean):Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(enablePlaceholders =  false , pageSize = 100) ,
            pagingSourceFactory = {
                if (useIncreasingPage){
                    PagingFantasyMoviesHome(api)
                }else PagingFantasyMovies(api)
            }
        ).flow
    }

    fun getHistoryMovies(useIncreasingPage: Boolean): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(enablePlaceholders = false , pageSize = 100) ,
            pagingSourceFactory = {
                if (useIncreasingPage) {
                    PagingHistoryMoviesHome(api)
                }else PagingHistoryMovies(api)
            }
        ).flow
    }

    fun getWarMovies(useIncreasingPage: Boolean): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(enablePlaceholders = false , pageSize = 100) ,
            pagingSourceFactory = {
                if (useIncreasingPage){
                    PagingWarMoviesHome(api)
                }else PagingWarMovies(api)
            }
        ).flow
    }
}