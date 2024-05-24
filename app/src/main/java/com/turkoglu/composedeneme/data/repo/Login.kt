package com.turkoglu.composedeneme.data.repo

import com.turkoglu.composedeneme.data.remote.MovieAPI
import com.turkoglu.composedeneme.data.remote.dto.AccountDetails
import com.turkoglu.composedeneme.data.remote.dto.CreateRequestToken
import com.turkoglu.composedeneme.data.remote.dto.CreateSession
import com.turkoglu.composedeneme.data.remote.dto.RequestCreateSession
import com.turkoglu.composedeneme.data.remote.dto.RequestCreateSessionWL
import com.turkoglu.composedeneme.util.CustomSharedPreferences
import com.turkoglu.composedeneme.util.Resource
import java.io.IOException
import javax.inject.Inject

class Login @Inject constructor(private val api: MovieAPI) {

    suspend fun getCreateRequestToken() : Resource<CreateRequestToken>{
        val response = try {
            api.createRequestToken()
        }catch (e : IOException){
            return Resource.Error(message = "Don't broooo")
        }
        CustomSharedPreferences.customSharedPreferences!!.setAccessToken(response!!.request_token)
        return Resource.Success(response)
    }

    suspend fun createSessionWithLogin(username: String, password: String) : Resource<CreateRequestToken>{
        val body = RequestCreateSessionWL(
            username = username,
            password = password,
            CustomSharedPreferences.customSharedPreferences!!.getAccessToken()
        )
        val response = try {
            api.createSessionWithLogin(requestCreateSessionWIthLogin = body)
        }catch (e : IOException){
            return Resource.Error(message = "doo")
        }
        CustomSharedPreferences.customSharedPreferences!!.setAccessToken(response.request_token)
        return Resource.Success(response)
    }

    suspend fun createSession() : Resource<CreateSession>{
        val body = RequestCreateSession(CustomSharedPreferences.customSharedPreferences!!.getAccessToken())
        val response = try {
            api.createSession(requestCreateSession = body)
        }catch (e : IOException){
            return Resource.Error("mıdıgo")
        }
        CustomSharedPreferences.customSharedPreferences!!.setUserSession(response.sessionId)
        return Resource.Success(response)
    }

    suspend fun getAccountDetail() : Resource<AccountDetails>{
        val response2 = try {
            api.getAccountDetail(sessionId = CustomSharedPreferences.customSharedPreferences!!.getUserSession() )
        }catch (e :IOException){
            return Resource.Error("dont")
        }
        return Resource.Success(response2)
    }





}