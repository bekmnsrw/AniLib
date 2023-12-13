package com.bekmnsrw.feature.auth.impl.usecase.local

import androidx.datastore.preferences.core.Preferences
import com.bekmnsrw.feature.auth.api.usecase.local.SaveLocalRefreshTokenUseCase
import com.bekmnsrw.feature.auth.impl.data.datasource.local.AuthDataStore

internal class SaveLocalRefreshTokenUseCaseImpl(
    private val authDataStore: AuthDataStore
) : SaveLocalRefreshTokenUseCase {

    override suspend fun invoke(
        refreshToken: String
    ): Preferences = authDataStore.saveRefreshToken(
        refreshToken = refreshToken
    )
}
