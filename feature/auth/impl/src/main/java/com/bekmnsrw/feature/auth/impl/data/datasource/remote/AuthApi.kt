package com.bekmnsrw.feature.auth.impl.data.datasource.remote

import com.bekmnsrw.feature.auth.impl.data.datasource.remote.request.GetAccessTokenRequestBody
import com.bekmnsrw.feature.auth.impl.data.datasource.remote.request.GetAuthCodeRequestBody
import com.bekmnsrw.feature.auth.impl.data.datasource.remote.request.RefreshAccessTokenRequestBody
import com.bekmnsrw.feature.auth.impl.data.datasource.remote.response.AccessTokenResponse
import com.bekmnsrw.feature.auth.impl.data.datasource.remote.response.AuthCodeResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

internal interface AuthApi {

    companion object {
        private const val USER_AGENT_HEADER = "User-Agent: AniLib"
    }

    @GET(value = "authorize")
    suspend fun getAuthCode(
        @Body getAuthCodeRequestBody: GetAuthCodeRequestBody
    ): AuthCodeResponse

    @POST(value = "token")
    @Headers(value = [USER_AGENT_HEADER])
    suspend fun getAccessToken(
        @Body getAccessTokenRequestBody: GetAccessTokenRequestBody
    ): AccessTokenResponse

    @POST(value = "token")
    @Headers(value = [USER_AGENT_HEADER])
    suspend fun refreshAccessToken(
        @Body refreshAccessTokenRequestBody: RefreshAccessTokenRequestBody
    ): AccessTokenResponse
}
