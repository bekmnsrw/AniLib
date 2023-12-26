package com.bekmnsrw.feature.home.api.usecase

import com.bekmnsrw.feature.home.api.model.FavoritesActionResult
import kotlinx.coroutines.flow.Flow

interface AddToFavoritesUseCase {

    suspend operator fun invoke(
        type: String,
        id: Int
    ): Flow<FavoritesActionResult>
}
