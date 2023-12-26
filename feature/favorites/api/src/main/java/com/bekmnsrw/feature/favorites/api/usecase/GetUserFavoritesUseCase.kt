package com.bekmnsrw.feature.favorites.api.usecase

import com.bekmnsrw.feature.favorites.api.model.FavoriteAnime
import kotlinx.coroutines.flow.Flow

interface GetUserFavoritesUseCase {

    suspend operator fun invoke(id: Int): Flow<List<FavoriteAnime>>
}
