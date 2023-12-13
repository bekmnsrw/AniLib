package com.bekmnsrw.feature.profile.impl.usecase.remote

import com.bekmnsrw.feature.profile.api.model.WhoAmI
import com.bekmnsrw.feature.profile.api.repository.ProfileRepository
import com.bekmnsrw.feature.profile.api.usecase.remote.GetProfileUseCase
import kotlinx.coroutines.flow.Flow

class GetProfileUseCaseImpl(
    private val profileRepository: ProfileRepository
) : GetProfileUseCase {

    override suspend fun invoke(): Flow<WhoAmI> = profileRepository.getProfile()
}
