package com.bekmnsrw.feature.auth.impl.data.datasource.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class AuthDataStore(private val context: Context) {

    private companion object {
        const val DATASTORE_NAME = "AUTH_DATASTORE"

        val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = DATASTORE_NAME)

        const val IS_AUTHENTICATED_KEY_NAME = "IS_AUTHENTICATED"
        const val ACCESS_TOKEN_KEY_NAME = "AUTH_TOKEN"
        const val REFRESH_TOKEN_KEY_NAME = "REFRESH_TOKEN"
        const val USER_ID_KEY_NAME = "USER_ID"

        val IS_AUTHENTICATED_KEY = booleanPreferencesKey(name = IS_AUTHENTICATED_KEY_NAME)
        val ACCESS_TOKEN_KEY = stringPreferencesKey(name = ACCESS_TOKEN_KEY_NAME)
        val REFRESH_TOKEN_KEY = stringPreferencesKey(name = REFRESH_TOKEN_KEY_NAME)
        val USER_ID_KEY = intPreferencesKey(name = USER_ID_KEY_NAME)
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

    fun getUserId(): Flow<Int?> = context.dataStore.data.map {
        it.get(key = USER_ID_KEY)
    }

    suspend fun saveUserId(id: Int): Preferences = context.dataStore.edit {
        it[USER_ID_KEY] = id
    }

    suspend fun onSignOut(): Preferences = context.dataStore.edit {
        it[IS_AUTHENTICATED_KEY] = false
        it[ACCESS_TOKEN_KEY] = ""
        it[REFRESH_TOKEN_KEY] = ""
        it[USER_ID_KEY] = 0
    }
}
