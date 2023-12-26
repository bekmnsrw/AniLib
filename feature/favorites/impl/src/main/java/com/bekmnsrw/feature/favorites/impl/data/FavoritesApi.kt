package com.bekmnsrw.feature.favorites.impl.data

import com.bekmnsrw.feature.favorites.impl.data.response.FavoritesResponse
import com.bekmnsrw.feature.favorites.impl.data.response.UserRatesResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

internal interface FavoritesApi {

    /* Move adding and removing from favourites here */

    @GET(value = "users/{id}/anime_rates")
    suspend fun getPlannedPaged(
        @Path("id") id: Int,
        @Query("page") page: Int,
        @Query("limit") limit: Int,
        @Query("status") status: String
    ): List<UserRatesResponse>

    @GET(value = "users/{id}/favourites")
    suspend fun getUserFavorites(
        @Path("id") id: Int
    ): FavoritesResponse
}
