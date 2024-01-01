package com.bekmnsrw.feature.auth.impl.usecase.local

import androidx.datastore.preferences.core.Preferences
import com.bekmnsrw.feature.auth.api.repository.AuthRepository
import com.bekmnsrw.feature.auth.api.usecase.local.OnSignOutUseCase

class OnSignOutUseCaseImpl(
    private val authRepository: AuthRepository
) : OnSignOutUseCase {

    override suspend fun invoke(): Preferences = authRepository.onSignOut()
}
