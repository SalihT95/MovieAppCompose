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

    // AccountDetails flow
    private val _accountDetails = MutableStateFlow<UserAccount?>(null)
    val accountDetails: StateFlow<UserAccount?> = _accountDetails

    private val _selectedLanguage = MutableStateFlow(LanguagePreference.selectedLanguage)
    val selectedLanguage: StateFlow<String> = _selectedLanguage

    fun setAccount(details: UserAccount) {
        _accountDetails.value = details
    }

    // Room'dan gelen kullanıcı bilgisi
    private val _currentUser = MutableStateFlow<UserAccount?>(null)
    val currentUser: StateFlow<UserAccount?> = _currentUser

    init {
        loadLanguagePreference()
        loadUser()
    }

    private fun loadLanguagePreference() {
        viewModelScope.launch {
            userPrefs.getLanguage().collect { savedLang ->
                _selectedLanguage.value = savedLang
                LanguagePreference.selectedLanguage = savedLang
            }
        }
    }

    // UserViewModel.kt içine bu fonksiyonu ekle:

    fun updateUserProfile(userId: String, newUsername: String) {
        viewModelScope.launch {
            // 1. Firestore'u güncelle
            userRepository.updateUserField(userId, "username", newUsername)

            // 2. Local State'i güncelle (Anlık yansıması için)
            _currentUser.value = _currentUser.value?.copy(username = newUsername)

            // 3. Room DB'yi güncelle
            _currentUser.value?.let { userRepository.saveUser(it) }
        }
    }

    fun updateLanguage(lang: String) {
        viewModelScope.launch {
            // StateFlow güncellemesi
            _selectedLanguage.value = lang
            LanguagePreference.selectedLanguage = lang
            userPrefs.saveLanguage(lang)

            // UserAccount güncellemesi
            _currentUser.value = currentUser.value?.copy(
                iso31661 = lang,
                iso6391 = lang
            )

            // DB kaydı
            currentUser.value?.let { user ->
                userRepository.saveUser(user.copy(iso31661 = lang, iso6391 = lang))
            }
        }
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
}
