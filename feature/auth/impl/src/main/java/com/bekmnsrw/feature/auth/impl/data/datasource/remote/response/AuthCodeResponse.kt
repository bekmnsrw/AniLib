package com.bekmnsrw.feature.auth.impl.data.datasource.remote.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AuthCodeResponse internal constructor(
    @SerialName("auth_code") val authCode: String
)
