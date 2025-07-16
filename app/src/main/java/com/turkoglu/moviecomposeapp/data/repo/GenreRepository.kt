package com.turkoglu.moviecomposeapp.data.repo

import com.turkoglu.moviecomposeapp.data.remote.MovieAPI
import com.turkoglu.moviecomposeapp.data.remote.dto.toGenre
import com.turkoglu.moviecomposeapp.domain.model.Genre
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GenreRepository @Inject constructor(
    private val api: MovieAPI
) {

    fun getGenres(): Flow<Result<List<Genre>>> = flow {
        emit(runCatching {
            api.getGenreList().genres.map { it.toGenre() }
        })
    }
}
