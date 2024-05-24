package com.turkoglu.composedeneme.presentation.login

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.turkoglu.composedeneme.data.repo.Login
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginRepo : Login,
    @ApplicationContext private val context: Context) : ViewModel() {

    //private val _state = mutableStateOf(LoginState())
    //val state: State<LoginState> = _state


    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("login_prefs", Context.MODE_PRIVATE)

    fun login(username: String, password: String) {
        // Kullanıcı giriş simülasyonu
        viewModelScope.launch {
            // Örnek bir asenkron işlem (API isteği, veritabanı kontrolü, vb.)
            // Simülasyon için bir süre bekleyelim.
            withContext(Dispatchers.IO) {
                delay(1000)

                // Simüle edilmiş kullanıcı girişi kontrolü
                val savedUsername = sharedPreferences.getString("username", "")
                val savedPassword = sharedPreferences.getString("password", "")

                println("kayıt edilen = $savedUsername")
                println("kayıt edilen = $savedPassword")
                println("kayıt edilen = ${getRememberMeStatus()}")
                if (username == savedUsername && password == savedPassword) {
                    // Kullanıcı girişi başarılı
                    println("Login successful")
                } else {
                    // Kullanıcı girişi başarısız
                    println("Login failed")
                }
            }
        }
    }

    fun saveRememberMeStatus(rememberMe: Boolean) {
        // SharedPreferences üzerinde hatırlama durumunu kaydet
        sharedPreferences.edit().putBoolean("remember_me", rememberMe).apply()
        println("kayıt edildi rememberMe : $rememberMe")
        //_state.value.isRemember = rememberMe
    }

    fun getRememberMeStatus(): Boolean {
        // SharedPreferences üzerinden hatırlama durumunu al
        return sharedPreferences.getBoolean("remember_me", true)
    }

    fun saveCredentials(username: String, password: String) {
        // Kullanıcı adı ve şifreyi SharedPreferences'e kaydet
        sharedPreferences.edit()
            .putString("username", username)
            .putString("password", password)
            .apply()
    }




    private fun getReququestToken(){
        viewModelScope.launch { loginRepo.getCreateRequestToken() }
    }
    private fun getCreateSession(){
        viewModelScope.launch { loginRepo.createSession() }
    }
    private fun getCreateSessionWL(username : String , password : String){
        viewModelScope.launch{ loginRepo.createSessionWithLogin(username , password )}
    }

    private fun getAccountDetail(){
        viewModelScope.launch { loginRepo.getAccountDetail() }
    }
}