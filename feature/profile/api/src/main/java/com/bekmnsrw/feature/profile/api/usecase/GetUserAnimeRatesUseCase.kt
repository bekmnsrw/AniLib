package com.bekmnsrw.feature.profile.api.usecase

import com.bekmnsrw.feature.profile.api.model.AnimeRates
import kotlinx.coroutines.flow.Flow

interface GetUserAnimeRatesUseCase {

    suspend operator fun invoke(id: Int): Flow<List<AnimeRates>>
}
