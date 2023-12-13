package com.bekmnsrw.feature.auth.impl.usecase.local

import com.bekmnsrw.feature.auth.api.usecase.local.GetLocalAccessTokenUseCase
import com.bekmnsrw.feature.auth.impl.data.datasource.local.AuthDataStore
import kotlinx.coroutines.flow.Flow

internal class GetLocalAccessTokenUseCaseImpl(
    private val authDataStore: AuthDataStore
) : GetLocalAccessTokenUseCase {

    override suspend fun invoke(): Flow<String?> = authDataStore.getAccessToken()
}
