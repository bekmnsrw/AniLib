package com.bekmnsrw.feature.profile.impl.data.datasource.remote

import com.bekmnsrw.feature.profile.impl.data.datasource.remote.response.WhoAmIResponse
import retrofit2.http.GET

internal interface ProfileApi {

    @GET(value = "users/whoami")
    suspend fun getProfile(): WhoAmIResponse
}
