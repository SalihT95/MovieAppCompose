package com.turkoglu.moviecomposeapp.presentation.fav

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.turkoglu.moviecomposeapp.data.local.UserPrefs
import com.turkoglu.moviecomposeapp.data.repo.MovieRepositoryImpl
import com.turkoglu.moviecomposeapp.data.repo.UserRepository
import com.turkoglu.moviecomposeapp.domain.model.Movie
import com.turkoglu.moviecomposeapp.util.LanguagePreference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val movieRepository: MovieRepositoryImpl,
    private val userPrefs: UserPrefs,
    private val auth: FirebaseAuth
) : ViewModel() {

    // Favorite entity yerine Movie model listesi
    private val _favorites = MutableStateFlow<List<Movie>>(emptyList())
    val favorites: StateFlow<List<Movie>> = _favorites

    init {
        getFavorites()
    }

    private fun getFavorites() {
        val uid = auth.currentUser?.uid
        if (uid != null) {
            viewModelScope.launch {
                combine(
                    userRepository.getFavoritesFlow(uid),
                    userPrefs.getLanguage().distinctUntilChanged()
                ) { movieList, lang ->
                    LanguagePreference.selectedLanguage = lang
                    movieList
                }.collectLatest { movieList ->
                    _favorites.value = movieList
                    if (movieList.isNotEmpty()) {
                        val localizedMovies = coroutineScope {
                            movieList.map { movie ->
                                async(Dispatchers.IO) {
                                    val movieId = movie.id ?: return@async movie
                                    try {
                                        val detail = movieRepository.getMovieDetail(movieId)
                                        movie.copy(
                                            title = detail.title.ifBlank { movie.title },
                                            description = detail.overview.ifBlank { movie.description },
                                            posterPath = detail.poster_path.ifBlank { movie.posterPath },
                                            backdropPath = detail.backdrop_path.ifBlank { movie.backdropPath },
                                            releaseDate = detail.release_date.ifBlank { movie.releaseDate ?: "" },
                                            voteAverage = detail.vote_average
                                        )
                                    } catch (e: Exception) {
                                        movie
                                    }
                                }
                            }.awaitAll()
                        }
                        _favorites.value = localizedMovies
                    }
                }
            }
        } else {
            _favorites.value = emptyList()
        }
    }

    fun deleteOneFavorite(movie: Movie) {
        val uid = auth.currentUser?.uid ?: return
        viewModelScope.launch {
            movie.id?.let { userRepository.removeFavorite(uid, it) }
        }
    }

    fun deleteAllFavorites() {
        val uid = auth.currentUser?.uid ?: return
        viewModelScope.launch {
            userRepository.deleteAllFavorites(uid)
        }
    }
}
