package com.bekmnsrw.feature.auth.impl.usecase.local

import androidx.datastore.preferences.core.Preferences
import com.bekmnsrw.feature.auth.api.repository.AuthRepository
import com.bekmnsrw.feature.auth.api.usecase.local.OnAuthenticationUseCase

internal class OnAuthenticationUseCaseImpl(
    private val authRepository: AuthRepository
) : OnAuthenticationUseCase {

    override suspend fun invoke(): Preferences = authRepository.onAuthentication()
}
