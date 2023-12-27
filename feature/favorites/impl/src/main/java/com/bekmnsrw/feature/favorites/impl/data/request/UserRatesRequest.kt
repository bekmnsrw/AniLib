package com.bekmnsrw.feature.favorites.impl.data.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class UserRatesRequest(
    @SerialName("user_rate") val userRate: UserRatesBody
)
