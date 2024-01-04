package com.bekmnsrw.feature.favorites.api.repository

import androidx.paging.PagingData
import com.bekmnsrw.feature.favorites.api.model.FavoriteAnime
import com.bekmnsrw.feature.favorites.api.model.UserRates
import kotlinx.coroutines.flow.Flow

interface FavoritesRepository {

    suspend fun getAnimePagedByStatus(
        id: Int,
        status: String
    ): Flow<PagingData<UserRates>>

    suspend fun getUserFavorites(id: Int): Flow<List<FavoriteAnime>>

    suspend fun updateAnimeStatus(
        id: Int,
        status: String
    ): Flow<String>
}
