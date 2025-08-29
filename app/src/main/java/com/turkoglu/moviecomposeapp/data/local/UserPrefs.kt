package com.turkoglu.moviecomposeapp.data.local

import android.content.Context
import androidx.core.content.edit
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore("user_prefs")

class UserPrefs(private val context: Context) {

    // 🔒 Güvenli alan
    private val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
    private val securePrefs = EncryptedSharedPreferences.create(
        "secure_prefs",
        masterKeyAlias,
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    companion object {
        private val KEY_REMEMBER_ME = booleanPreferencesKey("remember_me")
        private val KEY_LANGUAGE = stringPreferencesKey("selected_language")
        private val KEY_THEME = stringPreferencesKey("app_theme")
        private val KEY_ONBOARDING_DONE = booleanPreferencesKey("onboarding_done")
    }

    // 🔹 Onboarding
    suspend fun saveOnboardingDone(value: Boolean) =
        context.dataStore.edit { it[KEY_ONBOARDING_DONE] = value }

    fun getOnboardingDone(): Flow<Boolean> =
        context.dataStore.data.map { it[KEY_ONBOARDING_DONE] ?: false }

    // 🔹 Hassas veriler (EncryptedSharedPreferences)
    fun saveSessionId(sessionId: String) = securePrefs.edit { putString("session_id", sessionId) }
    fun getSessionId(): String? = securePrefs.getString("session_id", null)

    fun saveRequestToken(token: String) = securePrefs.edit { putString("request_token", token) }
    fun getRequestToken(): String? = securePrefs.getString("request_token", null)

    fun saveCredentials(username: String, password: String) {
        securePrefs.edit {
            putString("username", username)
            putString("password", password)
        }
    }

    fun getUsername(): String? = securePrefs.getString("username", null)
    fun getPassword(): String? = securePrefs.getString("password", null)

    fun clearAll() = securePrefs.edit { clear() }

    // 🔹 Kullanıcı ayarları (DataStore)
    suspend fun saveRememberMe(value: Boolean) =
        context.dataStore.edit { it[KEY_REMEMBER_ME] = value }

    fun getRememberMe(): Flow<Boolean> =
        context.dataStore.data.map { it[KEY_REMEMBER_ME] ?: false }

    suspend fun saveLanguage(languageCode: String) =
        context.dataStore.edit { it[KEY_LANGUAGE] = languageCode }

    fun getLanguage(): Flow<String> =
        context.dataStore.data.map { it[KEY_LANGUAGE] ?: "en" }

    suspend fun saveTheme(theme: String) =
        context.dataStore.edit { it[KEY_THEME] = theme }

    fun getTheme(): Flow<String> =
        context.dataStore.data.map { it[KEY_THEME] ?: "system" }
}
