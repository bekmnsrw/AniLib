package com.bekmnsrw.feature.home.api.usecase

import com.bekmnsrw.feature.home.api.model.Anime
import kotlinx.coroutines.flow.Flow

interface GetSimilarAnimeListUseCase {

    suspend operator fun invoke(
        id: Int,
        limit: Int
    ): Flow<List<Anime>>
}
