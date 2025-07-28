package com.turkoglu.moviecomposeapp.data.local

import android.content.Context
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys

class UserPreferenceManager(context: Context) {

    private val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)

    private val prefs = EncryptedSharedPreferences.create(
        "user_secure_prefs",
        masterKeyAlias,
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    companion object {
        private const val KEY_SESSION_ID = "session_id"
        private const val KEY_USERNAME = "username"
        private const val KEY_PASSWORD = "password"
        private const val KEY_REMEMBER_ME = "remember_me"
        private const val KEY_REQUEST_TOKEN = "request_token"
        private const val KEY_LANGUAGE = "selected_language"
    }

    fun saveSessionId(sessionId: String) = prefs.edit { putString(KEY_SESSION_ID, sessionId) }
    fun getSessionId(): String? = prefs.getString(KEY_SESSION_ID, null)

    fun saveUsername(username: String) = prefs.edit { putString(KEY_USERNAME, username) }
    fun getUsername(): String? = prefs.getString(KEY_USERNAME, null)

    fun savePassword(password: String) = prefs.edit { putString(KEY_PASSWORD, password) }
    fun getPassword(): String? = prefs.getString(KEY_PASSWORD, null)

    fun saveRememberMe(rememberMe: Boolean) = prefs.edit { putBoolean(KEY_REMEMBER_ME, rememberMe) }
    fun getRememberMe(): Boolean = prefs.getBoolean(KEY_REMEMBER_ME, false)

    fun saveRequestToken(token: String) = prefs.edit { putString(KEY_REQUEST_TOKEN, token) }
    fun getRequestToken(): String? = prefs.getString(KEY_REQUEST_TOKEN, null)

    fun saveLanguage(languageCode: String) =
        prefs.edit { putString("selected_language", languageCode) }

    fun getLanguage(): String? = prefs.getString(KEY_LANGUAGE, null)

    fun clearAll() = prefs.edit { clear() }
}
