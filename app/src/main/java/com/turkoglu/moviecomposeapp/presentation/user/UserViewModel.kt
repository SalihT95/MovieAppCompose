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

    fun setAccount(details: UserAccount) {
        _accountDetails.value = details
    }

    private val _currentUser = MutableStateFlow<UserAccount?>(null)
    val currentUser: StateFlow<UserAccount?> = _currentUser

    init {
        loadLanguagePreference()
        loadUser()
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
        val uid = _currentUser.value?.id ?: return
        viewModelScope.launch {
            // 1. Firestore güncelle
            userRepository.updateUserField(uid, "avatarUrl", newAvatarKey)

            // 2. Local State güncelle (UI anlık değişsin)
            _currentUser.value = _currentUser.value?.copy(avatarUrl = newAvatarKey)

            // 3. Room güncelle
            _currentUser.value?.let { userRepository.saveUser(it) }
        }
    }

    fun updateUserProfile(userId: String, newUsername: String) {
        viewModelScope.launch {
            userRepository.updateUserField(userId, "username", newUsername)
            _currentUser.value = _currentUser.value?.copy(username = newUsername)
            _currentUser.value?.let { userRepository.saveUser(it) }
        }
    }

    fun updateLanguage(lang: String) {
        viewModelScope.launch {
            _selectedLanguage.value = lang
            LanguagePreference.selectedLanguage = lang
            userPrefs.saveLanguage(lang)
            _currentUser.value = currentUser.value?.copy(
                iso31661 = lang,
                iso6391 = lang
            )
            currentUser.value?.let { user ->
                userRepository.saveUser(user.copy(iso31661 = lang, iso6391 = lang))
            }
        }
    }
}