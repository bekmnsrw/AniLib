package com.bekmnsrw.feature.home.impl.usecase

import androidx.paging.PagingData
import com.bekmnsrw.feature.home.api.model.Anime
import com.bekmnsrw.feature.home.api.repository.HomeRepository
import com.bekmnsrw.feature.home.api.usecase.SearchAnimeUseCase
import kotlinx.coroutines.flow.Flow

class SearchAnimeUseCaseImpl(
    private val homeRepository: HomeRepository
) : SearchAnimeUseCase {

    override suspend fun invoke(
        query: String,
        status: String?
    ): Flow<PagingData<Anime>> = homeRepository.searchAnime(
        query = query,
        status = status
    )
}
