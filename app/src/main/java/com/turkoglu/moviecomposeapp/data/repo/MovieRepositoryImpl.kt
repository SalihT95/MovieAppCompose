package com.turkoglu.moviecomposeapp.data.repo

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.turkoglu.moviecomposeapp.data.paging.GenrePagingSource
import com.turkoglu.moviecomposeapp.data.paging.PagingNowPlaying
import com.turkoglu.moviecomposeapp.data.paging.PagingNowPlayingHome
import com.turkoglu.moviecomposeapp.data.paging.PagingPopularMovies
import com.turkoglu.moviecomposeapp.data.paging.PagingPopularMoviesHome
import com.turkoglu.moviecomposeapp.data.paging.PagingTopRatedMovies
import com.turkoglu.moviecomposeapp.data.paging.PagingTopRatedMoviesHome
import com.turkoglu.moviecomposeapp.data.paging.PagingUpComingMovies
import com.turkoglu.moviecomposeapp.data.paging.PagingUpComingMoviesHome
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

class MovieRepositoryImpl @Inject constructor(private val api: MovieAPI) {

    internal fun genrePager(genreId: Int, useIncreasingPage: Boolean): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(enablePlaceholders = false, pageSize = 100),
            pagingSourceFactory = {
                GenrePagingSource(api, genreId, isPaginated = useIncreasingPage)
            }
        ).flow
    }

    fun getMovies(useIncreasingPage: Boolean): Flow<PagingData<Movie>> =
        Pager(PagingConfig(enablePlaceholders = false, pageSize = 100)) {
            if (useIncreasingPage) PagingPopularMoviesHome(api)
            else PagingPopularMovies(api)
        }.flow

    fun getTopRatedMovies(useIncreasingPage: Boolean): Flow<PagingData<Movie>> =
        Pager(PagingConfig(enablePlaceholders = false, pageSize = 100)) {
            if (useIncreasingPage) PagingTopRatedMoviesHome(api)
            else PagingTopRatedMovies(api)
        }.flow

    fun getNowPlayingMovies(useIncreasingPage: Boolean): Flow<PagingData<Movie>> =
        Pager(PagingConfig(enablePlaceholders = false, pageSize = 100)) {
            if (useIncreasingPage) PagingNowPlayingHome(api)
            else PagingNowPlaying(api)
        }.flow

    fun getUpcomingMovies(useIncreasingPage: Boolean): Flow<PagingData<Movie>> =
        Pager(PagingConfig(enablePlaceholders = false, pageSize = 100)) {
            if (useIncreasingPage) PagingUpComingMoviesHome(api)
            else PagingUpComingMovies(api)
        }.flow

    suspend fun getMovieDetail(movieId: Int): MovieDetailDto = api.getMovieDetail(movieId)

    suspend fun getMovieCasts(movieId: Int): Resource<CreditsDto> = try {
        Resource.Success(api.getMovieCredits(movieId))
    } catch (e: IOException) {
        Resource.Error("Unknown error occurred")
    }

    fun multiSearch(queryParam: String): Flow<PagingData<Search>> =
        Pager(PagingConfig(enablePlaceholders = false, pageSize = 27)) {
            SearchPagingSource(api, queryParam)
        }.flow

    fun getActionMovies(useIncreasingPage: Boolean) = genrePager(28, useIncreasingPage)
    fun getAdventureMovies(useIncreasingPage: Boolean) = genrePager(12, useIncreasingPage)
    fun getAnimationMovies(useIncreasingPage: Boolean) = genrePager(16, useIncreasingPage)
    fun getComedyMovies(useIncreasingPage: Boolean) = genrePager(35, useIncreasingPage)
    fun getCrimeMovies(useIncreasingPage: Boolean) = genrePager(80, useIncreasingPage)
    fun getDocumentaryMovies(useIncreasingPage: Boolean) = genrePager(99, useIncreasingPage)
    fun getDramaMovies(useIncreasingPage: Boolean) = genrePager(18, useIncreasingPage)
    fun getFamilyMovies(useIncreasingPage: Boolean) = genrePager(10751, useIncreasingPage)
    fun getFantasyMovies(useIncreasingPage: Boolean) = genrePager(14, useIncreasingPage)
    fun getHistoryMovies(useIncreasingPage: Boolean) = genrePager(36, useIncreasingPage)
    fun getHorrorMovies(useIncreasingPage: Boolean) = genrePager(27, useIncreasingPage)
    fun getMusicMovies(useIncreasingPage: Boolean) = genrePager(10402, useIncreasingPage)
    fun getMysteryMovies(useIncreasingPage: Boolean) = genrePager(9648, useIncreasingPage)
    fun getRomanceMovies(useIncreasingPage: Boolean) = genrePager(10749, useIncreasingPage)
    fun getScienceFictionMovies(useIncreasingPage: Boolean) = genrePager(878, useIncreasingPage)
    fun getTvMovieMovies(useIncreasingPage: Boolean) = genrePager(10770, useIncreasingPage)
    fun getThrillerMovies(useIncreasingPage: Boolean) = genrePager(53, useIncreasingPage)
    fun getWarMovies(useIncreasingPage: Boolean) = genrePager(10752, useIncreasingPage)
    fun getWesternMovies(useIncreasingPage: Boolean) = genrePager(37, useIncreasingPage)

}
