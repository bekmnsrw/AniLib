package com.bekmnsrw.core.network

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.single
import java.lang.Exception

abstract class GenericPagingSource<Response : Any> : PagingSource<Int, Response>() {
    private companion object {
        const val DEFAULT_PAGE_SIZE = 30
        const val START_PAGE_INDEX = 1
    }

    protected open val pageSize: Int = DEFAULT_PAGE_SIZE
    private lateinit var call: (suspend (currentPage: Int, limit: Int) -> Flow<List<Response>>)

    private fun getDefaultPagingConfig(): PagingConfig = PagingConfig(
        prefetchDistance = DEFAULT_PAGE_SIZE / 2,
        pageSize = pageSize
    )

    protected fun execute(
        call: (suspend (currentPage: Int, limit: Int) -> Flow<List<Response>>)
    ): Flow<PagingData<Response>> {
        this.call = call
        return Pager(
            config = getDefaultPagingConfig(),
            pagingSourceFactory = { this }
        ).flow
    }

    override fun getRefreshKey(
        state: PagingState<Int, Response>
    ): Int? = state.anchorPosition?.let { anchorPosition ->
        val anchorPage = state.closestPageToPosition(anchorPosition)
        anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Response> {
        val currentPage = params.key ?: START_PAGE_INDEX
        return try {
            val result = call(currentPage, DEFAULT_PAGE_SIZE).single()
            val nextKey = if (result.isEmpty()) null else currentPage.plus(1)
            val prevKey = if (currentPage == START_PAGE_INDEX) null else currentPage.minus(1)
            LoadResult.Page(
                data = result,
                prevKey = prevKey,
                nextKey = nextKey
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}
