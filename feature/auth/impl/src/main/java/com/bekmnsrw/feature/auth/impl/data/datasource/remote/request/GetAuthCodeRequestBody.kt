package com.bekmnsrw.feature.auth.impl.data.datasource.remote.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class GetAuthCodeRequestBody(
    @SerialName("client_id") val clientId: String,
    @SerialName("redirect_uri") val redirectUri: String,
    @SerialName("response_type") val responseType: String,
    @SerialName("scope") val scope: String
)
