package com.bekmnsrw.feature.home.impl.usecase

import com.bekmnsrw.feature.home.api.model.details.AnimeDetails
import com.bekmnsrw.feature.home.api.repository.HomeRepository
import com.bekmnsrw.feature.home.api.usecase.GetAnimeUseCase
import kotlinx.coroutines.flow.Flow

internal class GetAnimeUseCaseImpl(
    private val homeRepository: HomeRepository
) : GetAnimeUseCase {

    override suspend fun invoke(
        id: Int
    ): Flow<AnimeDetails> = homeRepository.getAnime(id = id)
}
