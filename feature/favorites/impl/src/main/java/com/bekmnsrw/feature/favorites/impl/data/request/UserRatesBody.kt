package com.bekmnsrw.feature.favorites.impl.data.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class UserRatesBody(
    @SerialName("chapters") val chapters: String = "",
    @SerialName("episodes") val episodes: String = "",
    @SerialName("rewatches") val rewatches: String = "",
    @SerialName("score") val score: String = "",
    @SerialName("status") val status: String = "",
    @SerialName("text") val text: String = "",
    @SerialName("volumes") val volumes: String = ""
)
