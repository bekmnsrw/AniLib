package com.bekmnsrw.core.network.authenticator

import com.bekmnsrw.core.network.interceptor.BearerInterceptor
import com.bekmnsrw.feature.auth.api.AuthConstant
import com.bekmnsrw.feature.auth.api.usecase.local.GetLocalRefreshTokenUseCase
import com.bekmnsrw.feature.auth.api.usecase.local.SaveLocalAccessTokenUseCase
import com.bekmnsrw.feature.auth.api.usecase.local.SaveLocalRefreshTokenUseCase
import com.bekmnsrw.feature.auth.api.usecase.remote.RefreshAccessTokenUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

class AuthAuthenticator(
    private val getLocalRefreshTokenUseCase: GetLocalRefreshTokenUseCase,
    private val refreshAccessTokenUseCase: RefreshAccessTokenUseCase,
    private val saveLocalRefreshTokenUseCase: SaveLocalRefreshTokenUseCase,
    private val saveLocalAccessTokenUseCase: SaveLocalAccessTokenUseCase
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        val refreshToken = runBlocking {
            getLocalRefreshTokenUseCase()
                .flowOn(Dispatchers.IO)
                .first()
        }

        println("REFRESH_TOKEN: $refreshToken")

        val newToken = runBlocking {
            refreshAccessTokenUseCase(
                grantType = AuthConstant.GRANT_TYPE_REFRESH_TOKEN,
                clientId = AuthConstant.CLIENT_ID,
                clientSecret = AuthConstant.CLIENT_SECRET,
                refreshToken = refreshToken!!
            )
                .flowOn(Dispatchers.IO)
                .first()
        }

        CoroutineScope(Dispatchers.IO).launch {
            saveLocalAccessTokenUseCase(accessToken = newToken.accessToken)
            saveLocalRefreshTokenUseCase(refreshToken = newToken.refreshToken)
        }

        return response.request.newBuilder()
            .header(
                name = BearerInterceptor.USER_AGENT_HEADER_NAME,
                value = BearerInterceptor.USER_AGENT_HEADER_VALUE
            )
            .header(
                name = BearerInterceptor.AUTHORIZATION_HEADER_NAME,
                value = "${BearerInterceptor.AUTHORIZATION_HEADER_VALUE} ${newToken.accessToken}"
            )
            .build()
    }
}
