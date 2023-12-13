package com.bekmnsrw.feature.auth.impl.usecase.remote

import com.bekmnsrw.feature.auth.api.model.AccessToken
import com.bekmnsrw.feature.auth.api.repository.AuthRepository
import com.bekmnsrw.feature.auth.api.usecase.remote.GetRemoteAccessTokenUseCase
import kotlinx.coroutines.flow.Flow

internal class GetRemoteAccessTokenUseCaseImpl(
    private val authRepository: AuthRepository
) : GetRemoteAccessTokenUseCase {

    override suspend fun invoke(
        grantType: String,
        clientId: String,
        clientSecret: String,
        authCode: String,
        redirectUri: String
    ): Flow<AccessToken> = authRepository.getAccessToken(
        grantType = grantType,
        clientId = clientId,
        clientSecret = clientSecret,
        authCode = authCode,
        redirectUri = redirectUri
    )
}
