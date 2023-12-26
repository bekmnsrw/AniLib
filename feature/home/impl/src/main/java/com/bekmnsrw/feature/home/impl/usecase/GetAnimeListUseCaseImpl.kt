package com.bekmnsrw.feature.home.impl.usecase

import com.bekmnsrw.feature.home.api.model.Anime
import com.bekmnsrw.feature.home.api.repository.HomeRepository
import com.bekmnsrw.feature.home.api.usecase.GetAnimeListUseCase

internal class GetAnimeListUseCaseImpl(
    private val homeRepository: HomeRepository
) : GetAnimeListUseCase {

    override suspend fun invoke(
        limit: Int,
        status: String,
        order: String
    ): List<Anime> = homeRepository.getAnimeList(
        limit = limit,
        status = status,
        order = order
    )
}
