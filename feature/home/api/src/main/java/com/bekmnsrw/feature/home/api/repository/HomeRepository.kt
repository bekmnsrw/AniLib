package com.bekmnsrw.feature.home.api.repository

import androidx.paging.PagingData
import com.bekmnsrw.feature.home.api.model.details.AnimeDetails
import com.bekmnsrw.feature.home.api.model.list.Anime
import kotlinx.coroutines.flow.Flow

interface HomeRepository {

    suspend fun getAnimePaged(
        status: String,
        order: String
    ): Flow<PagingData<Anime>>

    suspend fun getAnimeList(
        limit: Int,
        status: String,
        order: String
    ): List<Anime>

    suspend fun getAnime(
        id: Int
    ): Flow<AnimeDetails>
}
