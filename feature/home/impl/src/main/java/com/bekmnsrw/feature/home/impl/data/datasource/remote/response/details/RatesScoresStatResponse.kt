package com.bekmnsrw.feature.home.impl.data.datasource.remote.response.details

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class RatesScoresStatResponse(
    @SerialName("name") val name: Int,
    @SerialName("value") val value: Int
)
