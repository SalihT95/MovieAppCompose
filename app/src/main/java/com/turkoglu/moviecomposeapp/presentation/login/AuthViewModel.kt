package com.turkoglu.moviecomposeapp.presentation.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.turkoglu.moviecomposeapp.data.local.UserPrefs
import com.turkoglu.moviecomposeapp.domain.model.UserAccount
import com.turkoglu.moviecomposeapp.util.AvatarUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val userPrefs: UserPrefs
) : ViewModel() {

    var loginState by mutableStateOf<LoginUiState>(LoginUiState.Idle)
        private set

    var savedEmail by mutableStateOf(userPrefs.getUsername() ?: "")
        private set

    var savedPassword by mutableStateOf(userPrefs.getPassword() ?: "")
        private set

    val rememberMe = userPrefs.getRememberMe()
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)

    init {
        checkCurrentUser()
    }

    private fun checkCurrentUser() {
        if (auth.currentUser != null) {
            fetchUserInfo(auth.currentUser!!.uid)
        }
    }

    // --- FIREBASE KAYIT (REGISTER) ---
    fun register(email: String, password: String, username: String) = viewModelScope.launch {
        loginState = LoginUiState.Loading
        try {
            val authResult = auth.createUserWithEmailAndPassword(email, password).await()
            val user = authResult.user ?: throw Exception("Kullanıcı oluşturulamadı")

            // Rastgele Avatar Seçimi
            val randomAvatar = AvatarUtils.getRandomAvatar()

            val userMap = hashMapOf(
                "userId" to user.uid,
                "username" to username,
                "email" to email,
                "avatarUrl" to randomAvatar, // ARTIK URL DEĞİL, ID KEY KAYDEDİYORUZ
                "createdAt" to System.currentTimeMillis()
            )

            firestore.collection("users").document(user.uid)
                .set(userMap)
                .await()

            val newAccount = UserAccount(
                id = user.uid,
                username = username,
                name = username,
                avatarUrl = randomAvatar,
                includeAdult = false,
                iso31661 = "TR",
                iso6391 = "tr",
                isGuest = false
            )

            loginState = LoginUiState.Success(user.uid, newAccount)

        } catch (e: Exception) {
            loginState = LoginUiState.Error(e.localizedMessage ?: "Kayıt başarısız")
        }
    }

    fun updateRememberMe(value: Boolean) = viewModelScope.launch {
        userPrefs.saveRememberMe(value)
    }

    // --- FIREBASE LOGIN ---
    fun login(identifier: String, password: String) = viewModelScope.launch {
        loginState = LoginUiState.Loading
        try {
            var emailToLogin = identifier.trim()

            if (!identifier.contains("@")) {
                val querySnapshot = firestore.collection("users")
                    .whereEqualTo("username", identifier.trim())
                    .limit(1)
                    .get()
                    .await()

                if (querySnapshot.isEmpty) {
                    throw Exception("Bu kullanıcı adı ile bir hesap bulunamadı.")
                }

                emailToLogin = querySnapshot.documents.first().getString("email")
                    ?: throw Exception("Bu hesaba bağlı bir e-posta bulunamadı.")
            }

            val authResult = auth.signInWithEmailAndPassword(emailToLogin, password).await()
            val user = authResult.user ?: throw Exception("Kullanıcı bulunamadı")

            if (rememberMe.value) {
                userPrefs.saveCredentials(identifier, password)
            } else {
                userPrefs.saveCredentials("", "")
            }

            fetchUserInfo(user.uid)

        } catch (e: Exception) {
            loginState = LoginUiState.Error(e.localizedMessage ?: "Giriş başarısız")
        }
    }

    // --- FIREBASE GUEST LOGIN ---
    fun loginAsGuest() = viewModelScope.launch {
        loginState = LoginUiState.Loading
        try {
            val authResult = auth.signInAnonymously().await()
            val user = authResult.user ?: throw Exception("Misafir girişi yapılamadı")

            val guestAccount = UserAccount(
                id = user.uid,
                username = "Misafir",
                name = "Guest User",
                avatarUrl = AvatarUtils.GUEST_AVATAR_KEY, // "ghost"
                includeAdult = false,
                iso31661 = "TR",
                iso6391 = "tr",
                isGuest = true
            )

            loginState = LoginUiState.Success(user.uid, guestAccount)

        } catch (e: Exception) {
            loginState = LoginUiState.Error(e.localizedMessage ?: "Misafir hatası")
        }
    }

    // --- FIRESTORE VERİ ÇEKME ---
    private fun fetchUserInfo(uid: String) = viewModelScope.launch {
        try {
            val docSnapshot = firestore.collection("users").document(uid).get().await()

            if (docSnapshot.exists()) {
                val username = docSnapshot.getString("username") ?: "Kullanıcı"
                val profileImage = docSnapshot.getString("avatarUrl") // "avatarUrl" keyini kullanıyoruz

                val account = UserAccount(
                    id = uid,
                    username = username,
                    name = username,
                    avatarUrl = profileImage,
                    includeAdult = false,
                    iso31661 = "TR",
                    iso6391 = "tr",
                    isGuest = false
                )
                loginState = LoginUiState.Success(uid, account)
            } else {
                val account = UserAccount(
                    id = uid,
                    username = emailToUsername(auth.currentUser?.email),
                    name = "",
                    avatarUrl = null,
                    includeAdult = false,
                    iso31661 = "TR",
                    iso6391 = "tr",
                    isGuest = false
                )
                loginState = LoginUiState.Success(uid, account)
            }
        } catch (e: Exception) {
            loginState = LoginUiState.Error("Kullanıcı verisi alınamadı: ${e.message}")
        }
    }

    private fun emailToUsername(email: String?): String {
        return email?.substringBefore("@") ?: "Kullanıcı"
    }

    fun signOut() {
        auth.signOut()
        viewModelScope.launch {
            userPrefs.saveCredentials("", "")
        }
        loginState = LoginUiState.Idle
    }
}