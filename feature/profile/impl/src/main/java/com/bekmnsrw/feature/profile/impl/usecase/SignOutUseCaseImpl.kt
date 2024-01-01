package com.bekmnsrw.feature.profile.impl.usecase

import com.bekmnsrw.feature.profile.api.repository.ProfileRepository
import com.bekmnsrw.feature.profile.api.usecase.SignOutUseCase
import kotlinx.coroutines.flow.Flow

internal class SignOutUseCaseImpl(
    private val profileRepository: ProfileRepository
) : SignOutUseCase {

    override suspend fun invoke(): Flow<Int> = profileRepository.signOut()
}
