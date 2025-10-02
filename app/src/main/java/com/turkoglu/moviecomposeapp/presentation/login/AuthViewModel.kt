package com.turkoglu.moviecomposeapp.presentation.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.turkoglu.moviecomposeapp.data.local.UserPrefs
import com.turkoglu.moviecomposeapp.data.remote.dto.auth.RequestCreateSession
import com.turkoglu.moviecomposeapp.data.remote.dto.auth.RequestCreateSessionWL
import com.turkoglu.moviecomposeapp.data.remote.dto.auth.toUserAccount
import com.turkoglu.moviecomposeapp.data.repo.AuthRepository
import com.turkoglu.moviecomposeapp.domain.model.UserAccount
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository,
    private val userPrefs: UserPrefs
) : ViewModel() {

    var loginState by mutableStateOf<LoginUiState>(LoginUiState.Idle)
        private set

    var savedUsername by mutableStateOf(userPrefs.getUsername() ?: "")
        private set

    var savedPassword by mutableStateOf(userPrefs.getPassword() ?: "")
        private set

    // rememberMe artık Flow → StateFlow’a çeviriyoruz
    val rememberMe = userPrefs.getRememberMe()
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)

    fun updateRememberMe(value: Boolean) = viewModelScope.launch {
        userPrefs.saveRememberMe(value)
    }

    fun saveCredentialsIfRemembered(username: String, password: String) {
        viewModelScope.launch {
            if (rememberMe.value) {
                userPrefs.saveCredentials(username, password)
            }
        }
    }

    fun clearCredentials() {
        viewModelScope.launch {
            userPrefs.saveRememberMe(false)
            userPrefs.saveCredentials(savedUsername, "")
        }
    }

    fun login(username: String, password: String) = viewModelScope.launch {
        loginState = LoginUiState.Loading
        try {
            val tokenResponse = repository.createRequestToken()
            val token = tokenResponse?.requestToken ?: throw Exception("Token alınamadı")
            userPrefs.saveRequestToken(token)

            val validateResponse = repository.validateLogin(
                RequestCreateSessionWL(username, password, token)
            )
            if (!validateResponse.success) throw Exception("Login doğrulanamadı")

            val sessionResponse = repository.createSession(RequestCreateSession(token))
            val sessionId = sessionResponse.sessionId
            userPrefs.saveSessionId(sessionId)

            if (rememberMe.value) {
                userPrefs.saveCredentials(username, password)
            } else {
                userPrefs.saveCredentials(username, "")
                userPrefs.saveRememberMe(false)
            }

            val account = repository.getAccountDetail(sessionId).toUserAccount()

            loginState = LoginUiState.Success(sessionId, account)
            println("Login başarılı: Session ID: $sessionId, Account: $account")
        } catch (e: Exception) {
            loginState = LoginUiState.Error(e.localizedMessage ?: "Hata oluştu")
        }
    }

    fun loginAsGuest() = viewModelScope.launch {
        try {
            loginState = LoginUiState.Loading

            // Guest session oluştur
            val guestSessionResponse = repository.createGuestSession()
            val guestSessionId = guestSessionResponse.guestSessionId
                ?: throw Exception("Guest session alınamadı")

            userPrefs.saveSessionId(guestSessionId)

            // Guest UserAccount oluştur
            val guestAccount = UserAccount(
                id = -1, // DB için unique, normal userId yerine -1 veya random koyabilirsin
                username = "Guest",
                name = null,
                avatarUrl = null,
                includeAdult = false,
                iso31661 = "TR",
                iso6391 = "tr",
                isGuest = true
            )

            loginState = LoginUiState.Success(guestSessionId, guestAccount)
        } catch (e: Exception) {
            loginState = LoginUiState.Error(e.localizedMessage ?: "Guest login başarısız")
        }
    }

}

sealed class LoginUiState {
    object Idle : LoginUiState()
    object Loading : LoginUiState()
    data class Success(val sessionId: String, val account: UserAccount) : LoginUiState()
    data class Error(val message: String) : LoginUiState()
}
