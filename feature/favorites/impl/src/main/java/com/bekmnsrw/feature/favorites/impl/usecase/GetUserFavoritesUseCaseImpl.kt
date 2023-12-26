package com.bekmnsrw.feature.favorites.impl.usecase

import com.bekmnsrw.feature.favorites.api.model.FavoriteAnime
import com.bekmnsrw.feature.favorites.api.repository.FavoritesRepository
import com.bekmnsrw.feature.favorites.api.usecase.GetUserFavoritesUseCase
import kotlinx.coroutines.flow.Flow

internal class GetUserFavoritesUseCaseImpl(
    private val favoritesRepository: FavoritesRepository
) : GetUserFavoritesUseCase {

    override suspend fun invoke(
        id: Int
    ): Flow<List<FavoriteAnime>> = favoritesRepository.getUserFavorites(id = id)
}
