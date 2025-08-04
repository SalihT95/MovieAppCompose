package com.turkoglu.moviecomposeapp.data.repo

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.turkoglu.moviecomposeapp.data.paging.GenrePagingSource
import com.turkoglu.moviecomposeapp.data.paging.MovieListPagingSource
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

    internal fun genrePager(genreId: Int, useSinglePage: Boolean): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(enablePlaceholders = false, pageSize = 100),
            pagingSourceFactory = {
                GenrePagingSource(api, genreId, isSinglePage = useSinglePage)
            }
        ).flow
    }
    fun getPopularMovies(useSinglePage: Boolean): Flow<PagingData<Movie>> =
        Pager(PagingConfig(enablePlaceholders = false, pageSize = 100)) {
            MovieListPagingSource(api::getPopularMovies, isSinglePage = useSinglePage)
        }.flow

    fun getTopRatedMovies(useSinglePage: Boolean): Flow<PagingData<Movie>> =
        Pager(PagingConfig(enablePlaceholders = false, pageSize = 100)) {
            MovieListPagingSource(api::getTopRatedMovies, isSinglePage = useSinglePage)
        }.flow

    fun getNowPlayingMovies(useSinglePage: Boolean): Flow<PagingData<Movie>> =
        Pager(PagingConfig(enablePlaceholders = false, pageSize = 100)) {
            MovieListPagingSource(api::getNowPlayingMovies, isSinglePage = useSinglePage)
        }.flow

    fun getUpcomingMovies(useSinglePage: Boolean): Flow<PagingData<Movie>> =
        Pager(PagingConfig(enablePlaceholders = false, pageSize = 100)) {
            MovieListPagingSource(api::getUpcomingMovies, isSinglePage = useSinglePage)
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

    fun getActionMovies(useSinglePage: Boolean) = genrePager(28, useSinglePage)
    fun getAdventureMovies(useSinglePage: Boolean) = genrePager(12, useSinglePage)
    fun getAnimationMovies(useSinglePage: Boolean) = genrePager(16, useSinglePage)
    fun getComedyMovies(useSinglePage: Boolean) = genrePager(35, useSinglePage)
    fun getCrimeMovies(useSinglePage: Boolean) = genrePager(80, useSinglePage)
    fun getDocumentaryMovies(useSinglePage: Boolean) = genrePager(99, useSinglePage)
    fun getDramaMovies(useSinglePage: Boolean) = genrePager(18, useSinglePage)
    fun getFamilyMovies(useSinglePage: Boolean) = genrePager(10751, useSinglePage)
    fun getFantasyMovies(useSinglePage: Boolean) = genrePager(14, useSinglePage)
    fun getHistoryMovies(useSinglePage: Boolean) = genrePager(36, useSinglePage)
    fun getHorrorMovies(useSinglePage: Boolean) = genrePager(27, useSinglePage)
    fun getMusicMovies(useSinglePage: Boolean) = genrePager(10402, useSinglePage)
    fun getMysteryMovies(useSinglePage: Boolean) = genrePager(9648, useSinglePage)
    fun getRomanceMovies(useSinglePage: Boolean) = genrePager(10749, useSinglePage)
    fun getScienceFictionMovies(useSinglePage: Boolean) = genrePager(878, useSinglePage)
    fun getTvMovieMovies(useSinglePage: Boolean) = genrePager(10770, useSinglePage)
    fun getThrillerMovies(useSinglePage: Boolean) = genrePager(53, useSinglePage)
    fun getWarMovies(useSinglePage: Boolean) = genrePager(10752, useSinglePage)
    fun getWesternMovies(useSinglePage: Boolean) = genrePager(37, useSinglePage)

}
