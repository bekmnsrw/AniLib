package com.bekmnsrw.feature.home.impl.data.datasource.remote.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateUserRatesBody(
    @SerialName("chapters") val chapters: String = "",
    @SerialName("episodes") val episodes: String = "",
    @SerialName("rewatches") val rewatches: String = "",
    @SerialName("score") val score: String = "",
    @SerialName("status") val status: String = "",
    @SerialName("target_id") val targetId: String,
    @SerialName("target_type") val targetType: String,
    @SerialName("text") val text: String = "",
    @SerialName("user_id") val userId: String,
    @SerialName("volumes") val volumes: String = ""
)
