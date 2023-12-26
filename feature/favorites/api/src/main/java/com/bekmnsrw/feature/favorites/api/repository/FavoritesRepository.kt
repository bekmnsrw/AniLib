package com.bekmnsrw.feature.favorites.api.repository

import androidx.paging.PagingData
import com.bekmnsrw.feature.favorites.api.model.FavoriteAnime
import com.bekmnsrw.feature.favorites.api.model.UserRate
import kotlinx.coroutines.flow.Flow

interface FavoritesRepository {

    suspend fun getPlannedPaged(
        id: Int,
        status: String
    ): Flow<PagingData<UserRate>>

    suspend fun getUserFavorites(id: Int): Flow<List<FavoriteAnime>>
}
