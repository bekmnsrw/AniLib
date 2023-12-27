package com.bekmnsrw.feature.favorites.impl.usecase

import com.bekmnsrw.feature.favorites.api.repository.FavoritesRepository
import com.bekmnsrw.feature.favorites.api.usecase.UpdateAnimeStatusUseCase
import kotlinx.coroutines.flow.Flow

class UpdateAnimeStatusUseCaseImpl(
    private val favoritesRepository: FavoritesRepository
) : UpdateAnimeStatusUseCase {

    override suspend fun invoke(
        id: Int,
        status: String
    ): Flow<String> = favoritesRepository.updateAnimeStatus(
        id = id,
        status = status
    )
}
