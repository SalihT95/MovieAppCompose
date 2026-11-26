package com.turkoglu.moviecomposeapp.data.repo

import com.turkoglu.moviecomposeapp.data.remote.MovieAPI
import com.turkoglu.moviecomposeapp.data.remote.dto.toGenre
import com.turkoglu.moviecomposeapp.domain.model.Genre
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GenreRepository @Inject constructor(
    private val api: MovieAPI
) {

    private var cachedGenres: List<Genre>? = null

    fun getGenres(): Flow<List<Genre>> = flow {
        val cached = cachedGenres
        if (!cached.isNullOrEmpty()) {
            emit(cached)
        } else {
            val list = api.getGenreList().genres.map { it.toGenre() }
            cachedGenres = list
            emit(list)
        }
    }

    suspend fun preloadGenres(): List<Genre> {
        return try {
            val list = api.getGenreList().genres.map { it.toGenre() }
            cachedGenres = list
            list
        } catch (e: Exception) {
            cachedGenres ?: emptyList()
        }
    }

    fun hasCachedGenres(): Boolean = !cachedGenres.isNullOrEmpty()

    fun clearCache() {
        cachedGenres = null
    }
}
