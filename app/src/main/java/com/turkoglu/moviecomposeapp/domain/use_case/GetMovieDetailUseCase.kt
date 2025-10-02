package com.turkoglu.moviecomposeapp.domain.use_case


import com.turkoglu.moviecomposeapp.data.remote.dto.toMovie
import com.turkoglu.moviecomposeapp.data.repo.MovieRepositoryImpl
import com.turkoglu.moviecomposeapp.domain.model.MovieDetail
import com.turkoglu.moviecomposeapp.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

class GetMovieDetailUseCase @Inject constructor(private val repo: MovieRepositoryImpl) {
    fun executeGetMovieDetail(movieId: Int): Flow<Resource<MovieDetail>> = flow {
        try {
            emit(Resource.Loading())
            val movieDetail = repo.getMovieDetail(movieId)
            emit(Resource.Success(movieDetail.toMovie()))

        } catch (e: IOException) {
            emit(Resource.Error(message = "No internet connection"))
        }
    }
}