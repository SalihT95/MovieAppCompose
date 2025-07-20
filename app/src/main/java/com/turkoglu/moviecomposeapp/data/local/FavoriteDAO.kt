package com.turkoglu.moviecomposeapp.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.turkoglu.moviecomposeapp.domain.model.Favorite
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDAO {

    @Insert
    suspend fun insertFavorite(favorite: Favorite)

    @Query("SELECT * FROM favorites_table ORDER BY mediaId DESC")
    fun getAllFavorites(): Flow<List<Favorite>>

    @Query("SELECT * FROM favorites_table WHERE mediaId = :mediaId LIMIT 1")
    fun getAFavorite(mediaId: Int): Flow<Favorite?>

    @Delete
    suspend fun deleteAFavorite(favorite: Favorite)

    @Query("DELETE FROM favorites_table")
    suspend fun deleteAllFavorites()
}