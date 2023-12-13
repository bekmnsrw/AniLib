package com.bekmnsrw.feature.auth.impl.usecase.local

import androidx.datastore.preferences.core.Preferences
import com.bekmnsrw.feature.auth.api.usecase.local.SaveLocalAccessTokenUseCase
import com.bekmnsrw.feature.auth.impl.data.datasource.local.AuthDataStore

internal class SaveLocalAccessTokenUseCaseImpl(
    private val authDataStore: AuthDataStore
) : SaveLocalAccessTokenUseCase {

    override suspend fun invoke(
        accessToken: String
    ): Preferences = authDataStore.saveAccessToken(
        accessToken = accessToken
    )
}
