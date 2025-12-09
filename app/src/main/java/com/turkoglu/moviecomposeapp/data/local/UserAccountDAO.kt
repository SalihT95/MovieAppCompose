package com.turkoglu.moviecomposeapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.turkoglu.moviecomposeapp.domain.model.UserAccount
import kotlinx.coroutines.flow.Flow

@Dao
interface UserAccountDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(userAccount: UserAccount)

    @Query("SELECT * FROM user_account WHERE id = :id")
    fun getUserById(id: String): Flow<UserAccount>

    @Query("SELECT * FROM user_account LIMIT 1")
    fun getUserAccount(): Flow<UserAccount?>

    @Query("DELETE FROM user_account")
    suspend fun clear()
}
