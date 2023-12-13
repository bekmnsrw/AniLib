package com.bekmnsrw.feature.auth.impl.data.datasource.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AuthDataStore(
    private val context: Context
) {

    companion object {
        private const val DATASTORE_NAME = "AUTH_DATASTORE"

        private const val IS_FIRST_APP_LAUNCH_KEY_NAME = "IS_FIRST_APP_LAUNCH"
        private const val IS_AUTHENTICATED_KEY_NAME = "IS_AUTHENTICATED"
        private const val ACCESS_TOKEN_KEY_NAME = "AUTH_TOKEN"
        private const val REFRESH_TOKEN_KEY_NAME = "REFRESH_TOKEN"

        private val IS_FIRST_APP_LAUNCH_KEY = booleanPreferencesKey(name = IS_FIRST_APP_LAUNCH_KEY_NAME)
        private val IS_AUTHENTICATED_KEY = booleanPreferencesKey(name = IS_AUTHENTICATED_KEY_NAME)
        private val ACCESS_TOKEN_KEY = stringPreferencesKey(name = ACCESS_TOKEN_KEY_NAME)
        private val REFRESH_TOKEN_KEY = stringPreferencesKey(name = REFRESH_TOKEN_KEY_NAME)

        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
            name = DATASTORE_NAME
        )
    }

    fun isFirstAppLaunch(): Flow<Boolean?> = context.dataStore.data.map {
        it.get(key = IS_FIRST_APP_LAUNCH_KEY)
    }

    suspend fun onFirstAppLaunch(): Preferences = context.dataStore.edit {
        it[IS_FIRST_APP_LAUNCH_KEY] = false
    }

    fun isAuthenticated(): Flow<Boolean?> = context.dataStore.data.map {
        it.get(key = IS_AUTHENTICATED_KEY)
    }

    suspend fun onAuthentication(): Preferences = context.dataStore.edit {
        it[IS_AUTHENTICATED_KEY] = true
    }

    fun getAccessToken(): Flow<String?> = context.dataStore.data.map {
        it.get(key = ACCESS_TOKEN_KEY)
    }

    fun getRefreshToken(): Flow<String?> = context.dataStore.data.map {
        it.get(key = REFRESH_TOKEN_KEY)
    }

    suspend fun saveAccessToken(accessToken: String): Preferences = context.dataStore.edit {
        it[ACCESS_TOKEN_KEY] = accessToken
    }

    suspend fun saveRefreshToken(refreshToken: String): Preferences = context.dataStore.edit {
        it[REFRESH_TOKEN_KEY] = refreshToken
    }
}
