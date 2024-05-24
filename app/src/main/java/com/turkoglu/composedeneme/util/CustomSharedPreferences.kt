package com.turkoglu.composedeneme.util

import android.content.Context
import android.content.SharedPreferences

class CustomSharedPreferences private constructor(context: Context){

    private val preferences: SharedPreferences =
        context.getSharedPreferences("TMDB App", Context.MODE_PRIVATE)

    private var editor: SharedPreferences.Editor? = null
    private val IS_FIRST_OPEN = "isFirstOpen"
    private val USER_LOGIN = "isUserLogin"
    private val ACCESS_TOKEN = "userToken"
    private val USER_SESSION = "sessionId"

    companion object {
        var customSharedPreferences: CustomSharedPreferences? = null
            private set

        fun init(context: Context) {
            if (customSharedPreferences == null) {
                customSharedPreferences = CustomSharedPreferences(context)
            }
        }
    }
    fun isUserLogin(): Boolean {
        return preferences.getBoolean(USER_LOGIN, false)
    }

    fun setUserLogin(isLogin: Boolean) {
        editor = preferences.edit()
        editor?.apply {
            putBoolean(USER_LOGIN, isLogin)
            apply()
        }
    }

    fun isFirstOpen(): Boolean {
        return preferences.getBoolean(IS_FIRST_OPEN, true)
    }

    fun setFirstOpen(isFirstOpen: Boolean) {
        editor = preferences.edit()
        editor?.apply {
            putBoolean(IS_FIRST_OPEN, isFirstOpen)
            apply()
        }
    }

    fun getAccessToken(): String = preferences.getString(ACCESS_TOKEN, "Hello_app")!!

    fun setAccessToken(token: String) {
        editor = preferences.edit()
        editor?.apply {
            putString(ACCESS_TOKEN, token)
            apply()
        }
    }

    fun getUserSession(): String = preferences.getString(USER_SESSION, "Hello_app")!!

    fun setUserSession(sessionId: String) {
        editor = preferences.edit()
        editor?.apply {
            putString(USER_SESSION, sessionId)
            apply()
        }
    }

}