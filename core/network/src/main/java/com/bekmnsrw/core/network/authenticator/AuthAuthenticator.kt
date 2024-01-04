package com.bekmnsrw.core.network.authenticator

import android.util.Log
import com.bekmnsrw.core.network.NetworkConstants.AUTHORIZATION_HEADER_NAME
import com.bekmnsrw.core.network.NetworkConstants.AUTHORIZATION_HEADER_VALUE
import com.bekmnsrw.core.network.NetworkConstants.USER_AGENT_HEADER_NAME
import com.bekmnsrw.core.network.NetworkConstants.USER_AGENT_HEADER_VALUE
import com.bekmnsrw.feature.auth.api.AuthConstant
import com.bekmnsrw.feature.auth.api.usecase.local.GetLocalRefreshTokenUseCase
import com.bekmnsrw.feature.auth.api.usecase.local.IsAuthenticatedUseCase
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
import java.net.HttpURLConnection

class AuthAuthenticator(
    private val getLocalRefreshTokenUseCase: GetLocalRefreshTokenUseCase,
    private val refreshAccessTokenUseCase: RefreshAccessTokenUseCase,
    private val saveLocalRefreshTokenUseCase: SaveLocalRefreshTokenUseCase,
    private val saveLocalAccessTokenUseCase: SaveLocalAccessTokenUseCase,
    private val isAuthenticatedUseCase: IsAuthenticatedUseCase
) : Authenticator {

    @Synchronized
    override fun authenticate(route: Route?, response: Response): Request? {
        val isAuthenticated = runBlocking(Dispatchers.IO) {
            isAuthenticatedUseCase()
                .flowOn(Dispatchers.IO)
                .first()
        }

        Log.e("IS_AUTH", "$isAuthenticated")

        if (response.code == HttpURLConnection.HTTP_UNAUTHORIZED && isAuthenticated == true) {
            synchronized(this) {
                val refreshToken = runBlocking(Dispatchers.IO) {
                    getLocalRefreshTokenUseCase()
                        .flowOn(Dispatchers.IO)
                        .first()
                }

                if (!refreshToken.isNullOrEmpty()) {
                    val newToken = runBlocking(Dispatchers.IO) {
                        refreshAccessTokenUseCase(
                            grantType = AuthConstant.GRANT_TYPE_REFRESH_TOKEN,
                            clientId = AuthConstant.CLIENT_ID,
                            clientSecret = AuthConstant.CLIENT_SECRET,
                            refreshToken = refreshToken
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
                            name = USER_AGENT_HEADER_NAME,
                            value = USER_AGENT_HEADER_VALUE
                        )
                        .header(
                            name = AUTHORIZATION_HEADER_NAME,
                            value = "$AUTHORIZATION_HEADER_VALUE ${newToken.accessToken}"
                        )
                        .build()
                } else {
                    return null
                }
            }
        } else {
            return null
        }
    }
}
