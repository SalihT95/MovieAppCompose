package com.turkoglu.moviecomposeapp.presentation.fav

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.turkoglu.moviecomposeapp.data.local.Favorite
import com.turkoglu.moviecomposeapp.data.repo.FavoritesRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavViewModel @Inject constructor(
    private val repo: FavoritesRepo,
) : ViewModel() {
    val favorites = repo.getFavorites()

    fun insertFavorite(favorite: Favorite) {
        viewModelScope.launch {
            repo.insertFavorite(favorite)
        }
    }

    fun getAFavorite(mediaId: Int): Flow<Favorite?> {
        return repo.getAFavorite(mediaId)
    }

    fun deleteOneFavorite(favorite: Favorite) {
        viewModelScope.launch {
            repo.deleteOneFavorite(favorite)
        }
    }

    fun deleteAllFavorites() {
        viewModelScope.launch {
            repo.deleteAllFavorites()
        }
    }
}