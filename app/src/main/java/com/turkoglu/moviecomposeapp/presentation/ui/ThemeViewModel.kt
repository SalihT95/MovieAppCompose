package com.turkoglu.moviecomposeapp.presentation.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.turkoglu.moviecomposeapp.data.local.UserPrefs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ThemeViewModel @Inject constructor(
    private val userPrefs: UserPrefs
) : ViewModel() {

    // Theme enum ile dönüşüm
    val theme: StateFlow<ThemeMode> = userPrefs.getTheme()
        .map { value ->
            when (value) {
                "light" -> ThemeMode.LIGHT
                "dark" -> ThemeMode.DARK
                else -> ThemeMode.SYSTEM
            }
        }
        .stateIn(viewModelScope, SharingStarted.Eagerly, ThemeMode.SYSTEM)

    fun setTheme(themeMode: ThemeMode) {
        viewModelScope.launch {
            userPrefs.saveTheme(themeMode.value)
        }
    }

    val onboardingDone: StateFlow<Boolean> = userPrefs.getOnboardingDone()
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)


    fun setOnboardingDone() {
        viewModelScope.launch {
            userPrefs.saveOnboardingDone(true)
        }
    }
}
