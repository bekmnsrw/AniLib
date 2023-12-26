package com.bekmnsrw.feature.home.impl.data.datasource.remote

import com.bekmnsrw.feature.home.impl.data.datasource.remote.response.AnimeDetailsResponse
import com.bekmnsrw.feature.home.impl.data.datasource.remote.response.AnimeResponse
import com.bekmnsrw.feature.home.impl.data.datasource.remote.response.FavoritesActionResultResponse
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
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

    @POST(value = "favorites/{linked_type}/{linked_id}")
    suspend fun addToFavorites(
        @Path("linked_type") linkedType: String,
        @Path("linked_id") linkedId: Int
    ): FavoritesActionResultResponse

    @DELETE(value = "favorites/{linked_type}/{linked_id}")
    suspend fun removeFromFavorites(
        @Path("linked_type") linkedType: String,
        @Path("linked_id") linkedId: Int
    ): FavoritesActionResultResponse

    @GET(value = "animes/{id}/similar")
    suspend fun getSimilarAnimeList(
        @Path("id") id: Int,
        @Query("limit") limit: Int
    ): List<AnimeResponse>
}
