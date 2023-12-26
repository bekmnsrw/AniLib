package com.bekmnsrw.feature.auth.impl.usecase.local

import com.bekmnsrw.feature.auth.api.repository.AuthRepository
import com.bekmnsrw.feature.auth.api.usecase.local.GetUserIdUseCase
import kotlinx.coroutines.flow.Flow

class GetUserIdUseCaseImpl(
    private val authRepository: AuthRepository
) : GetUserIdUseCase {

    override suspend fun invoke(): Flow<Int?> = authRepository.getUserId()
}
