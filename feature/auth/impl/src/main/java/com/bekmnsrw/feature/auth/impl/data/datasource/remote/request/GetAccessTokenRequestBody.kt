package com.bekmnsrw.feature.auth.impl.data.datasource.remote.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class GetAccessTokenRequestBody(
    @SerialName("grant_type") val grantType: String,
    @SerialName("client_id") val clientId: String,
    @SerialName("client_secret") val clientSecret: String,
    @SerialName("code") val authCode: String,
    @SerialName("redirect_uri") val redirectUri: String
)
