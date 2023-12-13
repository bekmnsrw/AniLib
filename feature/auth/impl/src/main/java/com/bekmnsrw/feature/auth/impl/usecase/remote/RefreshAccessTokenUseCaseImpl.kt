package com.bekmnsrw.feature.auth.impl.usecase.remote

import com.bekmnsrw.feature.auth.api.model.AccessToken
import com.bekmnsrw.feature.auth.api.repository.AuthRepository
import com.bekmnsrw.feature.auth.api.usecase.remote.RefreshAccessTokenUseCase
import kotlinx.coroutines.flow.Flow

internal class RefreshAccessTokenUseCaseImpl(
    private val authRepository: AuthRepository
) : RefreshAccessTokenUseCase {

    override suspend fun invoke(
        grantType: String,
        clientId: String,
        clientSecret: String,
        refreshToken: String
    ): Flow<AccessToken> = authRepository.refreshAccessToken(
        grantType = grantType,
        clientId = clientId,
        clientSecret = clientSecret,
        refreshToken = refreshToken
    )
}
