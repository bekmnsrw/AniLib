package com.bekmnsrw.feature.home.impl.data.datasource.remote.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class AnimeImageResponse(
    @SerialName("original") val original: String,
    @SerialName("preview") val preview: String,
    @SerialName("x48") val x48: String,
    @SerialName("x96") val x96: String
)
