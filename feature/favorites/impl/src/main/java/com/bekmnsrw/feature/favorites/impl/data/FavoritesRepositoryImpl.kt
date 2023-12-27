package com.bekmnsrw.feature.favorites.impl.data

import androidx.paging.PagingData
import com.bekmnsrw.core.network.GenericPagingSource
import com.bekmnsrw.feature.favorites.api.model.FavoriteAnime
import com.bekmnsrw.feature.favorites.api.model.UserRates
import com.bekmnsrw.feature.favorites.api.repository.FavoritesRepository
import com.bekmnsrw.feature.favorites.impl.data.request.UserRatesBody
import com.bekmnsrw.feature.favorites.impl.data.request.UserRatesRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

internal class FavoritesRepositoryImpl(
    private val favoritesApi: FavoritesApi
) : FavoritesRepository, GenericPagingSource<UserRates>() {

    override suspend fun getPlannedPaged(
        id: Int,
        status: String
    ): Flow<PagingData<UserRates>> {
        return execute { currentPage, limit ->
            flow {
                emit(
                    favoritesApi.getPlannedPaged(
                        id = id,
                        page = currentPage,
                        limit = limit,
                        status = status
                    ).toUserRateList()
                )
            }
        }
    }

    override suspend fun getUserFavorites(id: Int): Flow<List<FavoriteAnime>> = flow {
        emit(
            favoritesApi
                .getUserFavorites(id = id)
                .animes
                .toFavoriteAnimeList()
        )
    }

    override suspend fun updateAnimeStatus(id: Int, status: String): Flow<String> = flow {
        favoritesApi.updateAnimeStatus(
            id = id,
            userRates = UserRatesRequest(
                UserRatesBody().copy(
                    status = status
                )
            )
        ).status?.let { emit(it) }
    }
}
