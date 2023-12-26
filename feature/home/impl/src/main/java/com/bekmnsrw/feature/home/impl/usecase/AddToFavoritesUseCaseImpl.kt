package com.bekmnsrw.feature.home.impl.usecase

import com.bekmnsrw.feature.home.api.model.FavoritesActionResult
import com.bekmnsrw.feature.home.api.repository.HomeRepository
import com.bekmnsrw.feature.home.api.usecase.AddToFavoritesUseCase
import kotlinx.coroutines.flow.Flow

internal class AddToFavoritesUseCaseImpl(
    private val homeRepository: HomeRepository
) : AddToFavoritesUseCase {

    override suspend fun invoke(
        type: String,
        id: Int
    ): Flow<FavoritesActionResult> = homeRepository.addToFavorites(
        type = type,
        id = id
    )
}
