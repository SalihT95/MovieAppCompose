package com.turkoglu.composedeneme.domain.use_case

import com.turkoglu.composedeneme.data.remote.dto.toVideo
import com.turkoglu.composedeneme.data.repo.VideoRepository
import com.turkoglu.composedeneme.domain.model.VideoModel
import com.turkoglu.composedeneme.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetVideoUrlUseCase @Inject constructor(private val repo : VideoRepository) {
    fun executeGetMovieVideo (movieId : Int): Flow<Resource<List<VideoModel>>> = flow {
        val video = repo.getVideo(movieId)
        emit(Resource.Success(video.toVideo()))
    }
}