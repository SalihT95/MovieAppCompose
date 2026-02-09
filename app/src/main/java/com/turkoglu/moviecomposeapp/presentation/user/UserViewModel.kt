package com.turkoglu.moviecomposeapp.presentation.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.turkoglu.moviecomposeapp.data.local.UserPrefs
import com.turkoglu.moviecomposeapp.data.repo.UserRepository
import com.turkoglu.moviecomposeapp.domain.model.UserAccount
import com.turkoglu.moviecomposeapp.util.LanguagePreference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val userPrefs: UserPrefs
) : ViewModel() {

    private val _accountDetails = MutableStateFlow<UserAccount?>(null)
    val accountDetails: StateFlow<UserAccount?> = _accountDetails

    private val _selectedLanguage = MutableStateFlow(LanguagePreference.selectedLanguage)
    val selectedLanguage: StateFlow<String> = _selectedLanguage

    private val _currentUser = MutableStateFlow<UserAccount?>(null)
    val currentUser: StateFlow<UserAccount?> = _currentUser

    init {
        loadLanguagePreference()
        loadUser()
    }

    fun setAccount(details: UserAccount) {
        _accountDetails.value = details
    }

    private fun loadUser() {
        viewModelScope.launch {
            userRepository.getUserFlow().collect { user ->
                _currentUser.value = user
            }
        }
    }

    fun saveUser(user: UserAccount) {
        viewModelScope.launch {
            userRepository.saveUser(user)
            _currentUser.value = user
        }
    }

    fun logout() {
        viewModelScope.launch {
            userRepository.clearUser()
            _currentUser.value = null
        }
    }

    private fun loadLanguagePreference() {
        viewModelScope.launch {
            userPrefs.getLanguage().collect { savedLang ->
                _selectedLanguage.value = savedLang
                LanguagePreference.selectedLanguage = savedLang
            }
        }
    }

    // --- AVATAR GÜNCELLEME ---
    fun updateAvatar(newAvatarKey: String) {
        // currentUser null ise işlem yapma
        val user = _currentUser.value ?: return

        viewModelScope.launch {
            // 1. Firestore'u güncelle (ID garanti dolu olmalı)
            userRepository.updateUserField(user.id, "avatarUrl", newAvatarKey)

            // 2. Yeni objeyi oluştur
            val updatedUser = user.copy(avatarUrl = newAvatarKey)

            // 3. Local State'i güncelle
            _currentUser.value = updatedUser

            // 4. Room veritabanını güncelle
            userRepository.saveUser(updatedUser)
        }
    }

    // --- PROFİL İSMİ GÜNCELLEME ---
    fun updateUserProfile(userId: String, newUsername: String) {
        // currentUser null ise işlem yapma
        val user = _currentUser.value ?: return

        viewModelScope.launch {
            // 1. Firestore güncelle
            userRepository.updateUserField(userId, "username", newUsername)

            // 2. Yeni objeyi oluştur
            val updatedUser = user.copy(username = newUsername)

            // 3. Local State güncelle
            _currentUser.value = updatedUser

            // 4. Room güncelle
            userRepository.saveUser(updatedUser)
        }
    }

    fun updateLanguage(lang: String) {
        viewModelScope.launch {
            _selectedLanguage.value = lang
            LanguagePreference.selectedLanguage = lang
            userPrefs.saveLanguage(lang)

            // Kullanıcı varsa onun dil tercihini de güncelle
            _currentUser.value?.let { user ->
                val updatedUser = user.copy(
                    iso31661 = lang,
                    iso6391 = lang
                )
                _currentUser.value = updatedUser
                userRepository.saveUser(updatedUser)
            }
        }
    }
}