package com.bekmnsrw.feature.profile.impl.usecase

import com.bekmnsrw.feature.profile.api.model.WhoAmI
import com.bekmnsrw.feature.profile.api.repository.ProfileRepository
import com.bekmnsrw.feature.profile.api.usecase.GetProfileUseCase
import kotlinx.coroutines.flow.Flow

class GetProfileUseCaseImpl(
    private val profileRepository: ProfileRepository
) : GetProfileUseCase {

    override suspend fun invoke(): Flow<WhoAmI> = profileRepository.getProfile()
}
