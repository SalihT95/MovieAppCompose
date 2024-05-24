package com.turkoglu.composedeneme.data.remote

import com.turkoglu.composedeneme.data.remote.dto.AccountDetails
import com.turkoglu.composedeneme.data.remote.dto.CreateRequestToken
import com.turkoglu.composedeneme.data.remote.dto.CreateSession
import com.turkoglu.composedeneme.data.remote.dto.CreditsDto
import com.turkoglu.composedeneme.data.remote.dto.MovieDetailDto
import com.turkoglu.composedeneme.data.remote.dto.MovieVideoDto
import com.turkoglu.composedeneme.data.remote.dto.MoviesDto
import com.turkoglu.composedeneme.data.remote.dto.MultiSearchDto
import com.turkoglu.composedeneme.data.remote.dto.RequestCreateSession
import com.turkoglu.composedeneme.data.remote.dto.RequestCreateSessionWL
import com.turkoglu.composedeneme.util.Constants.API_KEY
import com.turkoglu.composedeneme.util.Constants.DEFAULT_PAGE
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieAPI {

    @GET("movie/{movieId}")
    suspend fun getMovieDetail(
        @Path("movieId") movieId: Int,
        @Query("api_key") apiKey: String = API_KEY
    ): MovieDetailDto

    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("page") page: Int = DEFAULT_PAGE,
        @Query("api_key") apiKey: String = API_KEY
    ): MoviesDto


    @GET("movie/top_rated")
    suspend fun getTopRatedMovies(
        @Query("page") page: Int = DEFAULT_PAGE,
        @Query("api_key") apiKey: String = API_KEY
    ): MoviesDto

    @GET("movie/now_playing")
    suspend fun getNowPlayingMovies(
        @Query("page") page: Int = DEFAULT_PAGE,
        @Query("api_key") apiKey: String = API_KEY
    ): MoviesDto

    @GET("movie/upcoming")
    suspend fun getUpcomingMovies(
        @Query("page") page: Int = DEFAULT_PAGE,
        @Query("api_key") apiKey: String = API_KEY
    ): MoviesDto

    @GET("discover/movie")
    suspend fun getGenresMovies(
        @Query("api_key") apiKey: String = API_KEY,
        @Query("with_genres") genre: Int,
        @Query("page") page: Int = DEFAULT_PAGE
    ): MoviesDto

    @GET("movie/{movie_id}/credits")
    suspend fun getMovieCredits(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String = API_KEY
    ): CreditsDto

    @GET("search/movie")
    suspend fun getSearchMovies(
        @Query("page") page: Int = DEFAULT_PAGE,
        @Query("query") query: String,
        @Query("api_key") apiKey: String = API_KEY
    ): MultiSearchDto

    @GET("movie/{movie_id}/videos")
    suspend fun getMovieVideo(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String = API_KEY
    ) : MovieVideoDto



    @GET("authentication/token/new")
    suspend fun createRequestToken(@Query("api_key") apiKey: String = API_KEY): CreateRequestToken?

    @GET("/3/account")
    suspend fun getAccountDetail(
        @Query("api_key") apiKey: String = API_KEY,
        @Query("session_id") sessionId: String
    ): AccountDetails

    @POST("authentication/token/validate_with_login")
    suspend fun createSessionWithLogin(
        @Query("api_key") apiKey:String = API_KEY,
        @Body requestCreateSessionWIthLogin: RequestCreateSessionWL
    ): CreateRequestToken

    @POST("authentication/session/new")
    suspend fun createSession(
        @Query("api_key") apiKey: String = API_KEY,
        @Body requestCreateSession: RequestCreateSession
    ): CreateSession






}