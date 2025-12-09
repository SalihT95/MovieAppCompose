package com.turkoglu.moviecomposeapp.data.repo

import com.google.firebase.firestore.FirebaseFirestore
import com.turkoglu.moviecomposeapp.data.local.AppDatabase
import com.turkoglu.moviecomposeapp.domain.model.UserAccount
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val database: AppDatabase,
    private val firestore: FirebaseFirestore // 1. Firestore Eklendi
) {

    // Local Veritabanı İşlemleri (Room)
    suspend fun saveUser(user: UserAccount) {
        database.userAccountDao.insert(user)
    }

    suspend fun clearUser() {
        database.userAccountDao.clear()
    }

    fun getUserFlow(): Flow<UserAccount?> = database.userAccountDao.getUserAccount()

    // 2. Firestore Güncelleme İşlemi (Yeni)
    // Bu fonksiyon dinamiktir: İster username, ister avatarUrl güncelleyebilirsin.
    suspend fun updateUserField(userId: String, field: String, value: Any) {
        try {
            firestore.collection("users")
                .document(userId)
                .update(field, value)
                .await() // İşlemin bitmesini bekle (Suspend function)
        } catch (e: Exception) {
            e.printStackTrace()
            // İstersen hatayı yukarı fırlatabilirsin: throw e
        }
    }
}