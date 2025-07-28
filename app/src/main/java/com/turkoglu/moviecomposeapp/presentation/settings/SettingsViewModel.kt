package com.turkoglu.moviecomposeapp.presentation.settings

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.turkoglu.moviecomposeapp.data.local.UserPreferenceManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val userPrefs: UserPreferenceManager
) : ViewModel() {
    private val _languageChanged = mutableStateOf(false)
    val languageChanged = _languageChanged

    fun changeLanguage(lang: String) {
        userPrefs.saveLanguage(lang)
        _languageChanged.value = true
    }

    fun acknowledgeLanguageChange() {
        _languageChanged.value = false
    }
}
