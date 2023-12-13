package com.bekmnsrw.feature.auth.impl.usecase.local

import com.bekmnsrw.feature.auth.api.repository.AuthRepository
import com.bekmnsrw.feature.auth.api.usecase.local.IsFirstAppLaunchUseCase
import kotlinx.coroutines.flow.Flow

internal class IsFirstAppLaunchUseCaseImpl(
    private val authRepository: AuthRepository
) : IsFirstAppLaunchUseCase {

    override suspend fun invoke(): Flow<Boolean?> = authRepository.isFirstAppLaunch()
}
