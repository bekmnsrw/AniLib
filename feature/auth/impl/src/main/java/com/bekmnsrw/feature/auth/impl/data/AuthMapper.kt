package com.bekmnsrw.feature.auth.impl.data

import com.bekmnsrw.feature.auth.api.model.AccessToken
import com.bekmnsrw.feature.auth.api.model.AuthCode
import com.bekmnsrw.feature.auth.impl.data.datasource.remote.response.AccessTokenResponse
import com.bekmnsrw.feature.auth.impl.data.datasource.remote.response.AuthCodeResponse

fun AuthCodeResponse.toAuthCode(): AuthCode = AuthCode(
    authCode = authCode
)

fun AccessTokenResponse.toAccessToken(): AccessToken = AccessToken(
    accessToken = accessToken,
    tokenType = tokenType,
    expiresIn = expiresIn,
    refreshToken = refreshToken,
    scope = scope,
    createdAt = createdAt
)
