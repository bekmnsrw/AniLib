package com.bekmnsrw.feature.profile.impl.usecase

import com.bekmnsrw.feature.profile.api.model.AnimeRates
import com.bekmnsrw.feature.profile.api.repository.ProfileRepository
import com.bekmnsrw.feature.profile.api.usecase.GetUserAnimeByStatusUseCase
import kotlinx.coroutines.flow.Flow

class GetUserAnimeByStatusUseCaseImpl(
    private val profileRepository: ProfileRepository
) : GetUserAnimeByStatusUseCase {
    override suspend fun invoke(
        id: Int, status: String
    ): Flow<List<AnimeRates>> = profileRepository.getUserAnimeByStatus(
        id = id,
        status = status
    )
}
