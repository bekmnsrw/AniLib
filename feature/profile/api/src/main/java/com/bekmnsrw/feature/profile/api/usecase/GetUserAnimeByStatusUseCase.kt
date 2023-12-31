package com.bekmnsrw.feature.profile.api.usecase

import com.bekmnsrw.feature.profile.api.model.AnimeRates
import kotlinx.coroutines.flow.Flow

interface GetUserAnimeByStatusUseCase {

    suspend operator fun invoke(
        id: Int,
        status: String
    ): Flow<List<AnimeRates>>
}
