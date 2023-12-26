package com.bekmnsrw.feature.home.impl.data.datasource.remote.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class RatesStatusesStatResponse(
    @SerialName("name") val name: String,
    @SerialName("value") val value: Int
)
