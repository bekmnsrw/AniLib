package com.bekmnsrw.feature.home.impl.data

import androidx.paging.PagingData
import com.bekmnsrw.core.network.GenericPagingSource
import com.bekmnsrw.feature.home.api.model.FavoritesActionResult
import com.bekmnsrw.feature.home.api.model.Anime
import com.bekmnsrw.feature.home.api.model.AnimeDetails
import com.bekmnsrw.feature.home.api.repository.HomeRepository
import com.bekmnsrw.feature.home.impl.data.datasource.remote.HomeApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

internal class HomeRepositoryImpl(
    private val homeApi: HomeApi
) : HomeRepository, GenericPagingSource<Anime>() {

    override suspend fun getAnimePaged(
        status: String,
        order: String
    ): Flow<PagingData<Anime>> = execute { currentPage, limit ->
        flow {
            emit(
                homeApi.getAnimePaged(
                    page = currentPage,
                    limit = limit,
                    status = status,
                    order = order
                ).toAnimeList()
            )
        }
    }

    override suspend fun getAnimeList(
        limit: Int,
        status: String,
        order: String
    ): List<Anime> = homeApi
        .getAnimeList(
            limit = limit,
            status = status,
            order = order
        ).toAnimeList()

    override suspend fun getAnime(id: Int): Flow<AnimeDetails> = flow {
        emit(
            homeApi
                .getAnime(id = id)
                .toAnimeDetails()
        )
    }

    override suspend fun addToFavorites(
        type: String,
        id: Int
    ): Flow<FavoritesActionResult> = flow {
        emit(
            homeApi
                .addToFavorites(
                    linkedType = type,
                    linkedId = id
                ).toFavoritesActionResult()
        )
    }

    override suspend fun removeFromFavorites(
        type: String,
        id: Int
    ): Flow<FavoritesActionResult> = flow {
        emit(
            homeApi
                .removeFromFavorites(
                    linkedType = type,
                    linkedId = id
                ).toFavoritesActionResult()
        )
    }

    override suspend fun getSimilarAnimeList(
        id: Int,
        limit: Int
    ): Flow<List<Anime>> = flow {
        emit(
            homeApi
                .getSimilarAnimeList(
                    id = id,
                    limit = limit
                ).toAnimeList()
        )
    }
}
