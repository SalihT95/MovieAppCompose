package com.turkoglu.moviecomposeapp.domain.use_case

import com.turkoglu.moviecomposeapp.data.remote.dto.toVideo
import com.turkoglu.moviecomposeapp.data.repo.VideoRepository
import com.turkoglu.moviecomposeapp.domain.model.VideoModel
import com.turkoglu.moviecomposeapp.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetVideoUrlUseCase @Inject constructor(private val repo: VideoRepository) {
    fun executeGetMovieVideo(movieId: Int): Flow<Resource<List<VideoModel>>> = flow {
        val video = repo.getVideo(movieId)
        emit(Resource.Success(video.toVideo()))
    }
}