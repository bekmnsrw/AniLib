package com.bekmnsrw.feature.auth.impl.usecase.local

import com.bekmnsrw.feature.auth.api.repository.AuthRepository
import com.bekmnsrw.feature.auth.api.usecase.local.IsAuthenticatedUseCase
import kotlinx.coroutines.flow.Flow

internal class IsAuthenticatedUseCaseImpl(
    private val authRepository: AuthRepository
) : IsAuthenticatedUseCase {

    override suspend fun invoke(): Flow<Boolean?> = authRepository.isAuthenticated()
}
