package com.bekmnsrw.feature.home.impl.usecase

import com.bekmnsrw.feature.home.api.model.Anime
import com.bekmnsrw.feature.home.api.repository.HomeRepository
import com.bekmnsrw.feature.home.api.usecase.GetSimilarAnimeListUseCase
import kotlinx.coroutines.flow.Flow

class GetSimilarAnimeListUseCaseImpl(
    private val homeRepository: HomeRepository
) : GetSimilarAnimeListUseCase {

    override suspend fun invoke(
        id: Int,
        limit: Int
    ): Flow<List<Anime>> = homeRepository.getSimilarAnimeList(
        id = id,
        limit = limit
    )
}
