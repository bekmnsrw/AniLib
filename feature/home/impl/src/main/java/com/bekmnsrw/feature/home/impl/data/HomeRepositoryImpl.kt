package com.bekmnsrw.feature.home.impl.data

import androidx.paging.PagingData
import com.bekmnsrw.core.db.AppDatabase
import com.bekmnsrw.core.network.GenericPagingSource
import com.bekmnsrw.feature.home.api.model.FavoritesActionResult
import com.bekmnsrw.feature.home.api.model.Anime
import com.bekmnsrw.feature.home.api.model.AnimeDetails
import com.bekmnsrw.feature.home.api.model.SearchRequest
import com.bekmnsrw.feature.home.api.model.UserRates
import com.bekmnsrw.feature.home.api.repository.HomeRepository
import com.bekmnsrw.feature.home.impl.data.datasource.remote.HomeApi
import com.bekmnsrw.feature.home.impl.data.datasource.remote.request.CreateUserRatesBody
import com.bekmnsrw.feature.home.impl.data.datasource.remote.request.CreateUserRatesRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

internal class HomeRepositoryImpl(
    private val homeApi: HomeApi,
    private val appDatabase: AppDatabase
) : HomeRepository, GenericPagingSource<Anime>() {

    private companion object {
        const val TARGET_TYPE = "Anime"
    }

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

    override suspend fun createUserRates(
        userId: Int,
        targetId: Int,
        status: String
    ): Flow<UserRates> = flow {
        emit(
            homeApi.createUserRates(
                CreateUserRatesRequest(
                    CreateUserRatesBody(
                        userId = "$userId",
                        targetId = "$targetId",
                        targetType = TARGET_TYPE,
                        status = status
                    )
                )
            ).toUserRates()
        )
    }

    override suspend fun deleteUserRates(id: Int): Flow<Int> = flow {
        emit(
            homeApi.deleteUserRates(id = id).code()
        )
    }

    override suspend fun searchAnime(
        query: String,
        status: String?
    ): Flow<PagingData<Anime>> = execute { currentPage, limit ->
        flow {
            emit(
                if (status == null) {
                    homeApi.searchAnime(
                        page = currentPage,
                        limit = limit,
                        query = query
                    ).toAnimeList()
                } else {
                    homeApi.searchAnimeWithStatus(
                        page = currentPage,
                        limit = limit,
                        query = query,
                        status = status
                    ).toAnimeList()
                }
            )
        }
    }

    override suspend fun saveSearchRequest(searchRequest: SearchRequest) = appDatabase
        .searchRequestDao()
        .saveSearchRequest(searchRequestEntity = searchRequest.toSearchRequestEntity())

    override suspend fun deleteSearchRequestById(id: Int) = appDatabase
        .searchRequestDao()
        .deleteSearchRequestById(id = id)

    override suspend fun deleteAllSearchRequests() = appDatabase
        .searchRequestDao()
        .deleteAllSearchRequests()

    override suspend fun getAllSearchRequests(): Flow<List<SearchRequest>> = appDatabase
        .searchRequestDao()
        .getAllSearchRequestsOrderedByIdDesc()
        .map { it.toSearchRequestList() }
}
