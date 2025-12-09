package com.turkoglu.moviecomposeapp.presentation.fav

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.turkoglu.moviecomposeapp.data.repo.UserRepository
import com.turkoglu.moviecomposeapp.domain.model.Movie
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavViewModel @Inject constructor(
    private val userRepository: UserRepository,
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
            // Firebase'den gelen Flow'u dinle
            userRepository.getFavoritesFlow(uid)
                .onEach { movieList ->
                    _favorites.value = movieList
                }
                .launchIn(viewModelScope)
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