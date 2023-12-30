package com.bekmnsrw.feature.home.api.repository

import androidx.paging.PagingData
import com.bekmnsrw.feature.home.api.model.FavoritesActionResult
import com.bekmnsrw.feature.home.api.model.Anime
import com.bekmnsrw.feature.home.api.model.AnimeDetails
import com.bekmnsrw.feature.home.api.model.SearchRequest
import com.bekmnsrw.feature.home.api.model.UserRates
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

    suspend fun getAnime(id: Int): Flow<AnimeDetails>

    suspend fun addToFavorites(
        type: String,
        id: Int
    ): Flow<FavoritesActionResult>

    suspend fun removeFromFavorites(
        type: String,
        id: Int
    ): Flow<FavoritesActionResult>

    suspend fun getSimilarAnimeList(
        id: Int,
        limit: Int
    ): Flow<List<Anime>>

    suspend fun createUserRates(
        userId: Int,
        targetId: Int,
        status: String
    ): Flow<UserRates>

    suspend fun deleteUserRates(id: Int): Flow<Int>

    suspend fun searchAnime(
        query: String,
        status: String?
    ): Flow<PagingData<Anime>>

    suspend fun saveSearchRequest(searchRequest: SearchRequest)

    suspend fun deleteSearchRequestById(id: Int)

    suspend fun deleteAllSearchRequests()

    suspend fun getAllSearchRequests(): Flow<List<SearchRequest>>
}
