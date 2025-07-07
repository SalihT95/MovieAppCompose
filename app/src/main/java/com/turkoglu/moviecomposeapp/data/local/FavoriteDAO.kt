package com.turkoglu.moviecomposeapp.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDAO {

    @Insert
    suspend fun insertFavorite(favorite: Favorite)

    @Query("SELECT * FROM favoritestable ORDER BY mediaId DESC")
    fun getAllFavorites(): Flow<List<Favorite>>

    @Query("SELECT * FROM favoritestable WHERE mediaId = :mediaId LIMIT 1")
    fun getAFavorite(mediaId: Int): Flow<Favorite?>

    @Delete
    suspend fun deleteAFavorite(favorite: Favorite)

    @Query("DELETE FROM favoritestable")
    suspend fun deleteAllFavorites()
}