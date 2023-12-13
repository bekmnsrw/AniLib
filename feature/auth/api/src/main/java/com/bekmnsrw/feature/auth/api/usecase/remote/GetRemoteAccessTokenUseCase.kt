package com.bekmnsrw.feature.auth.api.usecase.remote

import com.bekmnsrw.feature.auth.api.model.AccessToken
import kotlinx.coroutines.flow.Flow

interface GetRemoteAccessTokenUseCase {

    suspend operator fun invoke(
        grantType: String,
        clientId: String,
        clientSecret: String,
        authCode: String,
        redirectUri: String
    ): Flow<AccessToken>
}
