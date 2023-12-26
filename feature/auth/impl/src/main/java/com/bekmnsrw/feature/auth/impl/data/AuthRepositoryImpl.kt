package com.bekmnsrw.feature.auth.impl.data

import androidx.datastore.preferences.core.Preferences
import com.bekmnsrw.feature.auth.api.model.AccessToken
import com.bekmnsrw.feature.auth.api.model.AuthCode
import com.bekmnsrw.feature.auth.api.repository.AuthRepository
import com.bekmnsrw.feature.auth.impl.data.datasource.local.AuthDataStore
import com.bekmnsrw.feature.auth.impl.data.datasource.remote.AuthApi
import com.bekmnsrw.feature.auth.impl.data.datasource.remote.request.GetAccessTokenRequestBody
import com.bekmnsrw.feature.auth.impl.data.datasource.remote.request.GetAuthCodeRequestBody
import com.bekmnsrw.feature.auth.impl.data.datasource.remote.request.RefreshAccessTokenRequestBody
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

internal class AuthRepositoryImpl(
    private val authApi: AuthApi,
    private val authDataStore: AuthDataStore
) : AuthRepository {

    override suspend fun getAuthCode(
        clientId: String,
        redirectUri: String,
        responseType: String,
        scope: String
    ): Flow<AuthCode> = flow {
        emit(
            authApi.getAuthCode(
                GetAuthCodeRequestBody(
                    clientId = clientId,
                    redirectUri = redirectUri,
                    responseType = responseType,
                    scope = scope
                )
            ).toAuthCode()
        )
    }

    override suspend fun getAccessToken(
        grantType: String,
        clientId: String,
        clientSecret: String,
        authCode: String,
        redirectUri: String
    ): Flow<AccessToken> = flow {
        emit(
            authApi.getAccessToken(
                GetAccessTokenRequestBody(
                    grantType = grantType,
                    clientId = clientId,
                    clientSecret = clientSecret,
                    authCode = authCode,
                    redirectUri = redirectUri
                )
            ).toAccessToken()
        )
    }

    override suspend fun refreshAccessToken(
        grantType: String,
        clientId: String,
        clientSecret: String,
        refreshToken: String
    ): Flow<AccessToken> = flow {
        emit(
            authApi.refreshAccessToken(
                RefreshAccessTokenRequestBody(
                    grantType = grantType,
                    clientId = clientId,
                    clientSecret = clientSecret,
                    refreshToken = refreshToken
                )
            ).toAccessToken()
        )
    }

    override suspend fun isFirstAppLaunch(): Flow<Boolean?> = authDataStore.isFirstAppLaunch()

    override suspend fun onFirstAppLaunch(): Preferences = authDataStore.onFirstAppLaunch()

    override suspend fun isAuthenticated(): Flow<Boolean?> = authDataStore.isAuthenticated()

    override suspend fun onAuthentication(): Preferences = authDataStore.onAuthentication()

    override suspend fun getUserId(): Flow<Int?> = authDataStore.getUserId()

    override suspend fun saveUserId(id: Int): Preferences = authDataStore.saveUserId(id = id)
}
