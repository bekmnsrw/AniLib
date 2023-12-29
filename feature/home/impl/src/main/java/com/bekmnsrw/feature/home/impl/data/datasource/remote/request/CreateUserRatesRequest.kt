package com.bekmnsrw.feature.home.impl.data.datasource.remote.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class CreateUserRatesRequest(
    @SerialName("user_rate") val userRate: CreateUserRatesBody
)
