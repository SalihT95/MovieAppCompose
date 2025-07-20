package com.turkoglu.moviecomposeapp.presentation.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.turkoglu.moviecomposeapp.data.repo.UserRepository
import com.turkoglu.moviecomposeapp.domain.model.UserAccount
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    // AccountDetails flow
    private val _accountDetails = MutableStateFlow<UserAccount?>(null)
    val accountDetails: StateFlow<UserAccount?> = _accountDetails

    fun setAccount(details: UserAccount) {
        _accountDetails.value = details
    }

    // Room'dan gelen kullanıcı bilgisi
    private val _currentUser = MutableStateFlow<UserAccount?>(null)
    val currentUser: StateFlow<UserAccount?> = _currentUser

    init {
        loadUser()
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
