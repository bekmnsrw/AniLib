package com.bekmnsrw.feature.profile.impl.usecase

import com.bekmnsrw.feature.profile.api.model.AnimeRates
import com.bekmnsrw.feature.profile.api.repository.ProfileRepository
import com.bekmnsrw.feature.profile.api.usecase.GetUserAnimeRatesUseCase
import kotlinx.coroutines.flow.Flow

class GetUserAnimeRatesUseCaseImpl(
    private val profileRepository: ProfileRepository
) : GetUserAnimeRatesUseCase {

    override suspend fun invoke(
        id: Int
    ) : Flow<List<AnimeRates>> = profileRepository.getUserAnimeRates(
        id = id
    )
}
