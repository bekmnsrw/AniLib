package com.bekmnsrw.feature.home.impl.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.bekmnsrw.feature.home.api.model.details.AnimeDetails
import com.bekmnsrw.feature.home.api.model.list.Anime
import com.bekmnsrw.feature.home.api.repository.HomeRepository
import com.bekmnsrw.feature.home.impl.data.datasource.remote.HomeApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

internal class HomeRepositoryImpl(
    private val homeApi: HomeApi
) : HomeRepository {

    override suspend fun getAnimePaged(
        status: String,
        order: String
    ): Flow<PagingData<Anime>> = Pager(
        config = PagingConfig(
            pageSize = AnimePagingSource.PAGE_SIZE
        ),
        pagingSourceFactory = {
            AnimePagingSource(
                homeApi = homeApi,
                status = status,
                order = order
            )
        }
    ).flow

    override suspend fun getAnimeList(
        limit: Int,
        status: String,
        order: String
    ): List<Anime> = homeApi.getAnimeList(
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
}
