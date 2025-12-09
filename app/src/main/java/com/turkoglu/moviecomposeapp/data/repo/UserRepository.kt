package com.turkoglu.moviecomposeapp.data.repo

import com.google.firebase.firestore.FirebaseFirestore
import com.turkoglu.moviecomposeapp.data.local.AppDatabase
import com.turkoglu.moviecomposeapp.domain.model.Movie
import com.turkoglu.moviecomposeapp.domain.model.UserAccount
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
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

    // 1. Favoriye Ekle (Senin Movie modelindeki alanları kaydediyoruz)
    suspend fun addFavorite(userId: String, movie: Movie) {
        try {
            val movieMap = hashMapOf(
                "id" to movie.id,
                "title" to movie.title,
                "description" to movie.description, // Senin modelde description, API'de overview
                "posterPath" to movie.posterPath,
                "backdropPath" to movie.backdropPath,
                "releaseDate" to movie.releaseDate,
                "voteAverage" to movie.voteAverage,
                "addedAt" to System.currentTimeMillis()
            )

            firestore.collection("users")
                .document(userId)
                .collection("favorites")
                .document(movie.id.toString()) // ID'yi döküman adı yapıyoruz
                .set(movieMap)
                .await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // 2. Favoriden Çıkar
    suspend fun removeFavorite(userId: String, movieId: Int) {
        try {
            firestore.collection("users")
                .document(userId)
                .collection("favorites")
                .document(movieId.toString())
                .delete()
                .await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // 3. Kontrol Et (Kalp dolu mu boş mu?)
    suspend fun isFavorite(userId: String, movieId: Int): Boolean {
        return try {
            val doc = firestore.collection("users")
                .document(userId)
                .collection("favorites")
                .document(movieId.toString())
                .get()
                .await()
            doc.exists()
        } catch (e: Exception) {
            false
        }
    }

    // 4. Favori Listesini Canlı Çekme (Real-time Flow)
    fun getFavoritesFlow(userId: String): Flow<List<Movie>> = callbackFlow {
        val listener = firestore.collection("users")
            .document(userId)
            .collection("favorites")
            .orderBy("addedAt", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val movies = snapshot?.documents?.mapNotNull { doc ->
                    try {
                        Movie(
                            id = doc.getLong("id")?.toInt() ?: 0,
                            title = doc.getString("title") ?: "",
                            description = doc.getString("description") ?: "",
                            posterPath = doc.getString("posterPath") ?: "",
                            backdropPath = doc.getString("backdropPath") ?: "",
                            releaseDate = doc.getString("releaseDate"),
                            voteAverage = doc.getDouble("voteAverage"),
                        )
                    } catch (e: Exception) {
                        e.printStackTrace()
                        null
                    }
                } ?: emptyList()

                trySend(movies)
            }
        awaitClose { listener.remove() }
    }

    // 5. Tüm Favorileri Sil
    suspend fun deleteAllFavorites(userId: String) {
        try {
            val collectionRef =
                firestore.collection("users").document(userId).collection("favorites")
            val snapshot = collectionRef.get().await()
            val batch = firestore.batch()
            for (doc in snapshot.documents) {
                batch.delete(doc.reference)
            }
            batch.commit().await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}