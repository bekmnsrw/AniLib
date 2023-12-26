package com.bekmnsrw.core.network.interceptor

import com.bekmnsrw.core.network.NetworkConstants.AUTHORIZATION_HEADER_NAME
import com.bekmnsrw.core.network.NetworkConstants.AUTHORIZATION_HEADER_VALUE
import com.bekmnsrw.core.network.NetworkConstants.USER_AGENT_HEADER_NAME
import com.bekmnsrw.core.network.NetworkConstants.USER_AGENT_HEADER_VALUE
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

    override fun intercept(chain: Interceptor.Chain): Response {
        val accessToken = runBlocking {
            getLocalAccessTokenUseCase()
                .flowOn(Dispatchers.IO)
                .first()
        }

        val request = chain.request().newBuilder()
            .header(
                name = USER_AGENT_HEADER_NAME,
                value = USER_AGENT_HEADER_VALUE
            )

        return if (accessToken != null) {
            chain.proceed(
                request.header(
                    name = AUTHORIZATION_HEADER_NAME,
                    value = "$AUTHORIZATION_HEADER_VALUE $accessToken"
                )
                    .build()
            )
        } else {
            chain.proceed(request.build())
        }
    }
}
