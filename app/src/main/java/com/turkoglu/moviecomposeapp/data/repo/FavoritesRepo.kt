package com.turkoglu.moviecomposeapp.data.repo

import com.turkoglu.moviecomposeapp.data.local.AppDatabase
import com.turkoglu.moviecomposeapp.domain.model.Favorite
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FavoritesRepo @Inject constructor(private val database: AppDatabase) {

    fun getFavorites(): Flow<List<Favorite>> {
        return database.favoriteDao.getAllFavorites()
    }

    fun getAFavorite(mediaId: Int): Flow<Favorite?> {
        return database.favoriteDao.getAFavorite(mediaId)
    }

    suspend fun insertFavorite(favorite: Favorite) {
        database.favoriteDao.insertFavorite(favorite)
    }

    suspend fun deleteOneFavorite(favorite: Favorite) {
        database.favoriteDao.deleteAFavorite(favorite)
    }

    suspend fun deleteAllFavorites() {
        database.favoriteDao.deleteAllFavorites()
    }
}