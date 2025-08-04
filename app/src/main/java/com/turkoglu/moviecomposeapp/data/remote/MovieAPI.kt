package com.turkoglu.moviecomposeapp.data.remote

import com.turkoglu.moviecomposeapp.data.remote.dto.CreditsDto
import com.turkoglu.moviecomposeapp.data.remote.dto.GenreListDto
import com.turkoglu.moviecomposeapp.data.remote.dto.MovieDetailDto
import com.turkoglu.moviecomposeapp.data.remote.dto.MovieVideoDto
import com.turkoglu.moviecomposeapp.data.remote.dto.MovieListResponseDto
import com.turkoglu.moviecomposeapp.data.remote.dto.MultiSearchDto
import com.turkoglu.moviecomposeapp.data.remote.dto.auth.AccountDetails
import com.turkoglu.moviecomposeapp.data.remote.dto.auth.CreateRequestToken
import com.turkoglu.moviecomposeapp.data.remote.dto.auth.CreateSession
import com.turkoglu.moviecomposeapp.data.remote.dto.auth.RequestCreateSession
import com.turkoglu.moviecomposeapp.data.remote.dto.auth.RequestCreateSessionWL
import com.turkoglu.moviecomposeapp.util.Constants.DEFAULT_PAGE
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieAPI {

    @GET("movie/{movieId}")
    suspend fun getMovieDetail(
        @Path("movieId") movieId: Int
    ): MovieDetailDto

    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("page") page: Int = DEFAULT_PAGE
    ): MovieListResponseDto

    @GET("movie/top_rated")
    suspend fun getTopRatedMovies(
        @Query("page") page: Int = DEFAULT_PAGE
    ): MovieListResponseDto

    @GET("movie/now_playing")
    suspend fun getNowPlayingMovies(
        @Query("page") page: Int = DEFAULT_PAGE
    ): MovieListResponseDto

    @GET("movie/upcoming")
    suspend fun getUpcomingMovies(
        @Query("page") page: Int = DEFAULT_PAGE
    ): MovieListResponseDto

    @GET("discover/movie")
    suspend fun getGenresMovies(
        @Query("with_genres") genre: Int,
        @Query("page") page: Int = DEFAULT_PAGE
    ): MovieListResponseDto

    @GET("movie/{movie_id}/credits")
    suspend fun getMovieCredits(
        @Path("movie_id") movieId: Int
    ): CreditsDto

    @GET("search/movie")
    suspend fun getSearchMovies(
        @Query("page") page: Int = DEFAULT_PAGE,
        @Query("query") query: String
    ): MultiSearchDto

    @GET("movie/{movie_id}/videos")
    suspend fun getMovieVideo(
        @Path("movie_id") movieId: Int
    ) : MovieVideoDto

    @GET("genre/movie/list")
    suspend fun getGenreList(): GenreListDto

    @GET("authentication/token/new")
    suspend fun createRequestToken(): CreateRequestToken?

    @GET("/3/account")
    suspend fun getAccountDetail(
        @Query("session_id") sessionId: String
    ): AccountDetails

    @POST("authentication/token/validate_with_login")
    suspend fun createSessionWithLogin(
        @Body requestCreateSessionWIthLogin: RequestCreateSessionWL
    ): CreateRequestToken

    @POST("authentication/session/new")
    suspend fun createSession(
        @Body requestCreateSession: RequestCreateSession
    ): CreateSession
}
