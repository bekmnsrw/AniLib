package com.bekmnsrw.core.network

import androidx.paging.PagingSource
import androidx.paging.PagingState

open class BasePagingSource<V : Any>(
    private val totalPages: Int? = null,
    private val block: suspend (Int, Int) -> List<V>
) : PagingSource<Int, V>() {

    private companion object {
        const val DEFAULT_PAGE_SIZE = 30
        const val START_PAGE_INDEX = 1
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, V> {
        val currentPage = params.key ?: START_PAGE_INDEX
        return try {
            val response = block(currentPage, DEFAULT_PAGE_SIZE)
            LoadResult.Page(
                data = response,
                prevKey = if (currentPage == START_PAGE_INDEX) null else currentPage.minus(1),
                nextKey = if (totalPages != null && currentPage == totalPages) null else currentPage.plus(1)
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(
        state: PagingState<Int, V>
    ): Int? = state.anchorPosition?.let { anchorPosition ->
        val anchorPage = state.closestPageToPosition(anchorPosition)
        anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
    }
}
