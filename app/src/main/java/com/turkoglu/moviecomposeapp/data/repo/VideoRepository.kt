package com.turkoglu.moviecomposeapp.data.repo

import com.turkoglu.moviecomposeapp.data.remote.MovieAPI
import com.turkoglu.moviecomposeapp.data.remote.dto.MovieVideoDto
import javax.inject.Inject

class VideoRepository @Inject constructor(private val api : MovieAPI){
    suspend fun getVideo(movieId : Int):MovieVideoDto{
        return api.getMovieVideo(movieId = movieId)
    }
}