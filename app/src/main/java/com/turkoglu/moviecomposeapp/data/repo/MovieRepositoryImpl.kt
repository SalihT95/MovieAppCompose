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
import com.turkoglu.moviecomposeapp.data.remote.dto.PersonDetailDto
import com.turkoglu.moviecomposeapp.data.remote.dto.toDomain
import com.turkoglu.moviecomposeapp.domain.model.Movie
import com.turkoglu.moviecomposeapp.domain.model.MovieCast
import com.turkoglu.moviecomposeapp.domain.model.Search
import com.turkoglu.moviecomposeapp.util.Resource
import kotlinx.coroutines.flow.Flow
import java.io.IOException
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val api: MovieAPI
) {

    companion object {
        private const val DEFAULT_PAGE_SIZE = 27
        private const val LARGE_PAGE_SIZE = 100
    }

    // 🔹 Ortak pager helper
    private fun <T : Any> buildPager(
        pageSize: Int = DEFAULT_PAGE_SIZE,
        factory: () -> androidx.paging.PagingSource<Int, T>
    ): Flow<PagingData<T>> {
        return Pager(
            config = PagingConfig(enablePlaceholders = false, pageSize = pageSize),
            pagingSourceFactory = factory
        ).flow
    }

    // 🔹 Genre bazlı filmler
    fun getMoviesByGenre(genreId: Int, useSinglePage: Boolean): Flow<PagingData<Movie>> =
        buildPager(pageSize = LARGE_PAGE_SIZE) {
            GenrePagingSource(api, genreId, isSinglePage = useSinglePage)
        }

    // 🔹 Popüler, Top Rated, vb.
    fun getPopularMovies(useSinglePage: Boolean): Flow<PagingData<Movie>> =
        buildPager(pageSize = LARGE_PAGE_SIZE) {
            MovieListPagingSource(api::getPopularMovies, isSinglePage = useSinglePage)
        }

    fun getTopRatedMovies(useSinglePage: Boolean): Flow<PagingData<Movie>> =
        buildPager(pageSize = LARGE_PAGE_SIZE) {
            MovieListPagingSource(api::getTopRatedMovies, isSinglePage = useSinglePage)
        }

    fun getNowPlayingMovies(useSinglePage: Boolean): Flow<PagingData<Movie>> =
        buildPager(pageSize = LARGE_PAGE_SIZE) {
            MovieListPagingSource(api::getNowPlayingMovies, isSinglePage = useSinglePage)
        }

    fun getUpcomingMovies(useSinglePage: Boolean): Flow<PagingData<Movie>> =
        buildPager(pageSize = LARGE_PAGE_SIZE) {
            MovieListPagingSource(api::getUpcomingMovies, isSinglePage = useSinglePage)
        }

    // 🔹 Film detayları & cast
    suspend fun getMovieDetail(movieId: Int): MovieDetailDto = api.getMovieDetail(movieId)

    // 🔹 Film oyuncuları
    suspend fun getMovieCasts(movieId: Int): Resource<CreditsDto> = try {
        Resource.Success(api.getMovieCredits(movieId))
    } catch (e: IOException) {
        Resource.Error("Network error occurred: ${e.localizedMessage}")
    } catch (e: Exception) {
        Resource.Error("Unexpected error: ${e.localizedMessage}")
    }

    // 🔹 Person detayları
    suspend fun getPersonDetail(personId: Int): Resource<PersonDetailDto> = try {
        Resource.Success(api.getPersonDetail(personId))
    } catch (e: IOException) {
        Resource.Error("Network error occurred: ${e.localizedMessage}")
    } catch (e: Exception) {
        Resource.Error("Unexpected error: ${e.localizedMessage}")
    }

    // 🔹 Person film listesi
    suspend fun getPersonMovieCredits(personId: Int): Resource<List<MovieCast>> = try {
        Resource.Success(api.getPersonMovieCredits(personId).toDomain().cast)
    } catch (e: IOException) {
        Resource.Error("Network error occurred: ${e.localizedMessage}")
    } catch (e: Exception) {
        Resource.Error("Unexpected error: ${e.localizedMessage}")
    }

    // 🔹 Multi-search
    fun multiSearch(queryParam: String): Flow<PagingData<Search>> =
        buildPager(pageSize = DEFAULT_PAGE_SIZE) {
            SearchPagingSource(api, queryParam)
        }

    // 🔹 Genre spesifik yardımcı metodlar
    fun getActionMovies(useSinglePage: Boolean) = getMoviesByGenre(28, useSinglePage)
    fun getAdventureMovies(useSinglePage: Boolean) = getMoviesByGenre(12, useSinglePage)
    fun getAnimationMovies(useSinglePage: Boolean) = getMoviesByGenre(16, useSinglePage)
    fun getComedyMovies(useSinglePage: Boolean) = getMoviesByGenre(35, useSinglePage)
    fun getCrimeMovies(useSinglePage: Boolean) = getMoviesByGenre(80, useSinglePage)
    fun getDocumentaryMovies(useSinglePage: Boolean) = getMoviesByGenre(99, useSinglePage)
    fun getDramaMovies(useSinglePage: Boolean) = getMoviesByGenre(18, useSinglePage)
    fun getFamilyMovies(useSinglePage: Boolean) = getMoviesByGenre(10751, useSinglePage)
    fun getFantasyMovies(useSinglePage: Boolean) = getMoviesByGenre(14, useSinglePage)
    fun getHistoryMovies(useSinglePage: Boolean) = getMoviesByGenre(36, useSinglePage)
    fun getHorrorMovies(useSinglePage: Boolean) = getMoviesByGenre(27, useSinglePage)
    fun getMusicMovies(useSinglePage: Boolean) = getMoviesByGenre(10402, useSinglePage)
    fun getMysteryMovies(useSinglePage: Boolean) = getMoviesByGenre(9648, useSinglePage)
    fun getRomanceMovies(useSinglePage: Boolean) = getMoviesByGenre(10749, useSinglePage)
    fun getScienceFictionMovies(useSinglePage: Boolean) = getMoviesByGenre(878, useSinglePage)
    fun getTvMovieMovies(useSinglePage: Boolean) = getMoviesByGenre(10770, useSinglePage)
    fun getThrillerMovies(useSinglePage: Boolean) = getMoviesByGenre(53, useSinglePage)
    fun getWarMovies(useSinglePage: Boolean) = getMoviesByGenre(10752, useSinglePage)
    fun getWesternMovies(useSinglePage: Boolean) = getMoviesByGenre(37, useSinglePage)
}
