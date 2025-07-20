package com.turkoglu.moviecomposeapp.presentation.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.turkoglu.moviecomposeapp.data.local.UserPreferenceManager
import com.turkoglu.moviecomposeapp.data.remote.dto.auth.RequestCreateSession
import com.turkoglu.moviecomposeapp.data.remote.dto.auth.RequestCreateSessionWL
import com.turkoglu.moviecomposeapp.data.remote.dto.auth.toUserAccount
import com.turkoglu.moviecomposeapp.data.repo.AuthRepository
import com.turkoglu.moviecomposeapp.domain.model.UserAccount
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository,
    private val userPrefs: UserPreferenceManager
) : ViewModel() {

    var loginState by mutableStateOf<LoginUiState>(LoginUiState.Idle)
        private set

    var savedUsername by mutableStateOf(userPrefs.getUsername() ?: "")
        private set

    var savedPassword by mutableStateOf(userPrefs.getPassword() ?: "")
        private set

    var rememberMe by mutableStateOf(userPrefs.getRememberMe())
        private set

    fun updateRememberMe(value: Boolean) {
        rememberMe = value
        userPrefs.saveRememberMe(value)
    }

    fun saveCredentialsIfRemembered(username: String, password: String) {
        if (rememberMe) {
            userPrefs.saveUsername(username)
            userPrefs.savePassword(password)
        }
    }

    fun clearCredentials() {
        //userPrefs.clearAll()
        userPrefs.saveRememberMe(false)
        //userPrefs.saveUsername("")
        userPrefs.savePassword("")
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

            if (rememberMe) {
                userPrefs.saveUsername(username)
                userPrefs.savePassword(password)
            } else {
                //userPrefs.clearAll()
                userPrefs.savePassword("")
                userPrefs.saveRememberMe(false)
            }

            val account = repository.getAccountDetail(sessionId).toUserAccount()

            loginState = LoginUiState.Success(sessionId, account)
            println("Login başarılı: Session ID: $sessionId, Account: $account")
        } catch (e: Exception) {
            loginState = LoginUiState.Error(e.localizedMessage ?: "Hata oluştu")
        }
    }
}

sealed class LoginUiState {
    object Idle : LoginUiState()
    object Loading : LoginUiState()
    data class Success(val sessionId: String, val account: UserAccount) : LoginUiState()
    data class Error(val message: String) : LoginUiState()
}
