package com.turkoglu.moviecomposeapp.data.repo

import com.turkoglu.moviecomposeapp.data.local.AppDatabase
import com.turkoglu.moviecomposeapp.domain.model.UserAccount
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserRepository @Inject constructor(private val database: AppDatabase) {

    suspend fun saveUser(user: UserAccount) {
        database.userAccountDao.insert(user)
    }

    suspend fun clearUser() {
        database.userAccountDao.clear()
    }

    fun getUserFlow(): Flow<UserAccount?> = database.userAccountDao.getUserAccount()
}
