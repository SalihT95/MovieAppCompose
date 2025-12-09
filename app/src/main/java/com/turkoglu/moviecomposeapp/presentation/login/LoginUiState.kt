package com.turkoglu.moviecomposeapp.presentation.login

import com.turkoglu.moviecomposeapp.domain.model.UserAccount

sealed class LoginUiState {
    // Ekran ilk açıldığında hiçbir işlem yokken
    object Idle : LoginUiState()

    // Yükleniyor (Spinner/Progress Bar dönmesi için)
    object Loading : LoginUiState()

    // Giriş Başarılı (Firebase UID ve Kullanıcı bilgilerini döner)
    data class Success(val userId: String, val account: UserAccount) : LoginUiState()

    // Hata Durumu (Hata mesajını döner)
    data class Error(val message: String) : LoginUiState()
}