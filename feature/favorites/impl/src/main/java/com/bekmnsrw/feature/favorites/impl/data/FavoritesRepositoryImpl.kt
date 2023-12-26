package com.bekmnsrw.feature.favorites.impl.data

import androidx.paging.PagingData
import com.bekmnsrw.core.network.GenericPagingSource
import com.bekmnsrw.feature.favorites.api.model.FavoriteAnime
import com.bekmnsrw.feature.favorites.api.model.UserRate
import com.bekmnsrw.feature.favorites.api.repository.FavoritesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

internal class FavoritesRepositoryImpl(
    private val favoritesApi: FavoritesApi
) : FavoritesRepository, GenericPagingSource<UserRate>() {

    override suspend fun getPlannedPaged(
        id: Int,
        status: String
    ): Flow<PagingData<UserRate>> {
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
                .animes.toFavoriteAnimeList()
        )
    }
}
