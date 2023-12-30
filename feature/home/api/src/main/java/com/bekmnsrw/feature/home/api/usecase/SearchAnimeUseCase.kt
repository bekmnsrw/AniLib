package com.bekmnsrw.feature.home.api.usecase

import androidx.paging.PagingData
import com.bekmnsrw.feature.home.api.model.Anime
import kotlinx.coroutines.flow.Flow

interface SearchAnimeUseCase {

    suspend operator fun invoke(
        query: String,
        status: String?
    ): Flow<PagingData<Anime>>
}
