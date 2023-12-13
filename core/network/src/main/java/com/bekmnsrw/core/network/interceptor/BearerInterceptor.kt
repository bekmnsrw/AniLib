package com.bekmnsrw.core.network.interceptor

import com.bekmnsrw.feature.auth.api.usecase.local.GetLocalAccessTokenUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class BearerInterceptor(
    private val getLocalAccessTokenUseCase: GetLocalAccessTokenUseCase
) : Interceptor {

    companion object {
        const val USER_AGENT_HEADER_NAME = "User-Agent"
        const val USER_AGENT_HEADER_VALUE = "AniLib"
        const val AUTHORIZATION_HEADER_NAME = "Authorization"
        const val AUTHORIZATION_HEADER_VALUE = "Bearer"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val accessToken = runBlocking {
            getLocalAccessTokenUseCase()
                .flowOn(Dispatchers.IO)
                .first()
        }

        return chain.proceed(
            chain.request().newBuilder()
                .header(
                    name = USER_AGENT_HEADER_NAME,
                    value = USER_AGENT_HEADER_VALUE
                )
                .header(
                    name = AUTHORIZATION_HEADER_NAME,
                    value = "$AUTHORIZATION_HEADER_VALUE $accessToken"
                )
                .build()
        )
    }
}
