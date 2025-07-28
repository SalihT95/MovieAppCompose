package com.turkoglu.moviecomposeapp.presentation.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.turkoglu.moviecomposeapp.data.local.UserPreferenceManager
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
    private val userPrefs: UserPreferenceManager
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
        val savedLang = userPrefs.getLanguage() ?: "tr"
        _selectedLanguage.value = savedLang
        LanguagePreference.selectedLanguage = savedLang
    }

    fun updateLanguage(lang: String) {
        _selectedLanguage.value = lang
        LanguagePreference.selectedLanguage = lang
        userPrefs.saveLanguage(lang)

        viewModelScope.launch {
            userRepository.saveUser(
                UserAccount(
                    id = currentUser.value?.id ?: 0,
                    username = currentUser.value?.username ?: "",
                    name = currentUser.value?.name,
                    avatarUrl = currentUser.value?.avatarUrl,
                    includeAdult = currentUser.value?.includeAdult ?: false,
                    iso31661 = lang,
                    iso6391 = lang
                )
            )
            _currentUser.value = currentUser.value?.copy(
                iso31661 = lang,
                iso6391 = lang
            )
        }
    }


    fun loadUser() {
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
