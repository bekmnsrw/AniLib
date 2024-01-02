package com.bekmnsrw.feature.auth.api.repository

import androidx.datastore.preferences.core.Preferences
import com.bekmnsrw.feature.auth.api.model.AccessToken
import com.bekmnsrw.feature.auth.api.model.AuthCode
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    suspend fun getAuthCode(
        clientId: String,
        redirectUri: String,
        responseType: String,
        scope: String
    ): Flow<AuthCode>

    suspend fun getAccessToken(
        grantType: String,
        clientId: String,
        clientSecret: String,
        authCode: String,
        redirectUri: String
    ): Flow<AccessToken>

    suspend fun refreshAccessToken(
        grantType: String,
        clientId: String,
        clientSecret: String,
        refreshToken: String
    ): Flow<AccessToken>

    suspend fun isAuthenticated(): Flow<Boolean?>

    suspend fun onAuthentication(): Preferences

    suspend fun getUserId(): Flow<Int?>

    suspend fun saveUserId(id: Int): Preferences
    suspend fun onSignOut(): Preferences
}
