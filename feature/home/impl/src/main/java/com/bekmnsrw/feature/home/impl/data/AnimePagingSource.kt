package com.bekmnsrw.feature.home.impl.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.bekmnsrw.feature.home.api.model.list.Anime
import com.bekmnsrw.feature.home.api.repository.HomeRepository
import com.bekmnsrw.feature.home.impl.data.datasource.remote.HomeApi

internal class AnimePagingSource(
    private val homeApi: HomeApi,
    private val status: String,
    private val order: String
) : PagingSource<Int, Anime>() {

    internal companion object {
        const val PAGE_SIZE = 15
        private const val STARTING_PAGE_INDEX = 1
    }

    override fun getRefreshKey(
        state: PagingState<Int, Anime>
    ): Int? = state.anchorPosition?.let { anchorPosition ->
        val anchorPage = state.closestPageToPosition(anchorPosition)
        anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Anime> {
        val position = params.key ?: STARTING_PAGE_INDEX

        val animes = homeApi.getAnimePaged(
            page = position,
            limit = PAGE_SIZE,
            status = status,
            order = order
        ).toAnimeList()

        return LoadResult.Page(
            data = animes,
            prevKey = if (position == STARTING_PAGE_INDEX) null else position.minus(1),
            nextKey = if (animes.isEmpty()) null else position.plus(1)
        )
    }
}
