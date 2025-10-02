package com.turkoglu.moviecomposeapp.data.repo

import com.turkoglu.moviecomposeapp.data.remote.MovieAPI
import com.turkoglu.moviecomposeapp.data.remote.dto.auth.AccountDetails
import com.turkoglu.moviecomposeapp.data.remote.dto.auth.CreateRequestToken
import com.turkoglu.moviecomposeapp.data.remote.dto.auth.CreateSession
import com.turkoglu.moviecomposeapp.data.remote.dto.auth.GuestSessionResponse
import com.turkoglu.moviecomposeapp.data.remote.dto.auth.RequestCreateSession
import com.turkoglu.moviecomposeapp.data.remote.dto.auth.RequestCreateSessionWL
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val api: MovieAPI
) {
    suspend fun createRequestToken(): CreateRequestToken? {
        return api.createRequestToken()
    }

    suspend fun validateLogin(request: RequestCreateSessionWL): CreateRequestToken {
        return api.createSessionWithLogin(requestCreateSessionWIthLogin = request)
    }

    suspend fun createSession(request: RequestCreateSession): CreateSession {
        return api.createSession(requestCreateSession = request)
    }

    suspend fun createGuestSession(): GuestSessionResponse {
        return api.createGuestSession()
    }

    suspend fun getAccountDetail(sessionId: String): AccountDetails {
        return api.getAccountDetail(sessionId = sessionId)
    }
}

