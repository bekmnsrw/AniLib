package com.bekmnsrw.feature.home.impl.data.datasource.remote

import com.bekmnsrw.feature.home.impl.data.datasource.remote.response.details.AnimeDetailsResponse
import com.bekmnsrw.feature.home.impl.data.datasource.remote.response.list.AnimeResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

internal interface HomeApi {

    @GET(value = "animes")
    suspend fun getAnimePaged(
        @Query("page") page: Int,
        @Query("limit") limit: Int,
        @Query("status") status: String,
        @Query("order") order: String
    ): List<AnimeResponse>

    @GET(value = "animes")
    suspend fun getAnimeList(
        @Query("limit") limit: Int,
        @Query("status") status: String,
        @Query("order") order: String
    ): List<AnimeResponse>

    @GET(value = "animes/{id}")
    suspend fun getAnime(
        @Path("id") id: Int
    ): AnimeDetailsResponse
}
