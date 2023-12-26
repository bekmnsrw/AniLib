package com.bekmnsrw.feature.auth.impl.usecase.local

import androidx.datastore.preferences.core.Preferences
import com.bekmnsrw.feature.auth.api.repository.AuthRepository
import com.bekmnsrw.feature.auth.api.usecase.local.SaveUserIdUseCase

class SaveUserIdUseCaseImpl(
    private val authRepository: AuthRepository
) : SaveUserIdUseCase {

    override suspend fun invoke(id: Int): Preferences = authRepository.saveUserId(id = id)
}
