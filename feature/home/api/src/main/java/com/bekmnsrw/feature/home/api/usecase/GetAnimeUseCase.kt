package com.bekmnsrw.feature.home.api.usecase

import com.bekmnsrw.feature.home.api.model.AnimeDetails
import kotlinx.coroutines.flow.Flow

interface GetAnimeUseCase {

    suspend operator fun invoke(id: Int): Flow<AnimeDetails>
}
