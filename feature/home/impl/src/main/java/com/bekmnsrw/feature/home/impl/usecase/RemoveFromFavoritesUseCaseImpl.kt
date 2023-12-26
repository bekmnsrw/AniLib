package com.bekmnsrw.feature.home.impl.usecase

import com.bekmnsrw.feature.home.api.model.FavoritesActionResult
import com.bekmnsrw.feature.home.api.repository.HomeRepository
import com.bekmnsrw.feature.home.api.usecase.RemoveFromFavoritesUseCase
import kotlinx.coroutines.flow.Flow

internal class RemoveFromFavoritesUseCaseImpl(
    private val homeRepository: HomeRepository
) : RemoveFromFavoritesUseCase {

    override suspend fun invoke(
        type: String,
        id: Int
    ): Flow<FavoritesActionResult> = homeRepository.removeFromFavorites(
        type = type,
        id = id
    )
}
