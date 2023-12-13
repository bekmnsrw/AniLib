package com.bekmnsrw.feature.auth.impl.usecase.local

import androidx.datastore.preferences.core.Preferences
import com.bekmnsrw.feature.auth.api.repository.AuthRepository
import com.bekmnsrw.feature.auth.api.usecase.local.OnFirstAppLaunchUseCase

internal class OnFirstAppLaunchUseCaseImpl(
    private val authRepository: AuthRepository
) : OnFirstAppLaunchUseCase {

    override suspend fun invoke(): Preferences = authRepository.onFirstAppLaunch()
}
