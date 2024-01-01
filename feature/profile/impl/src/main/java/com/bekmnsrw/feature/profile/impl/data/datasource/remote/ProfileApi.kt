package com.bekmnsrw.feature.profile.impl.data.datasource.remote

import com.bekmnsrw.feature.profile.impl.data.datasource.remote.response.AnimeRatesResponse
import com.bekmnsrw.feature.profile.impl.data.datasource.remote.response.WhoAmIResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

internal interface ProfileApi {

    @GET(value = "users/whoami")
    suspend fun getProfile(): WhoAmIResponse

    @GET(value = "users/{id}/anime_rates")
    suspend fun getUserAnimeByStatus(
        @Path("id") id: Int,
        @Query("status") status: String
    ): List<AnimeRatesResponse>

    @GET(value = "users/{id}/anime_rates")
    suspend fun getUserAnimeRates(
        @Path("id") id: Int
    ): List<AnimeRatesResponse>


    @GET(value = "users/sign_out")
    suspend fun signOut(): Response<Unit>
}
