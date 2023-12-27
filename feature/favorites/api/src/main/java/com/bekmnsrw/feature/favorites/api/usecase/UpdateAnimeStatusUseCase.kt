package com.bekmnsrw.feature.favorites.api.usecase

import kotlinx.coroutines.flow.Flow

interface UpdateAnimeStatusUseCase {

    suspend operator fun invoke(
        id: Int,
        status: String
    ): Flow<String>
}
