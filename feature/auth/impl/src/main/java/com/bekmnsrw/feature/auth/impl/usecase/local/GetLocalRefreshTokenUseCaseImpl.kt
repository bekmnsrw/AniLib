package com.bekmnsrw.feature.auth.impl.usecase.local

import com.bekmnsrw.feature.auth.api.usecase.local.GetLocalRefreshTokenUseCase
import com.bekmnsrw.feature.auth.impl.data.datasource.local.AuthDataStore
import kotlinx.coroutines.flow.Flow

internal class GetLocalRefreshTokenUseCaseImpl(
    private val authDataStore: AuthDataStore
) : GetLocalRefreshTokenUseCase {

    override suspend fun invoke(): Flow<String?> = authDataStore.getRefreshToken()
}
