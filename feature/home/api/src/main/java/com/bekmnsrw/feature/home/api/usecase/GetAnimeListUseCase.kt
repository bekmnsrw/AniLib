package com.bekmnsrw.feature.home.api.usecase

import com.bekmnsrw.feature.home.api.model.Anime

interface GetAnimeListUseCase {

    suspend operator fun invoke(
        limit: Int,
        status: String,
        order: String
    ): List<Anime>
}
