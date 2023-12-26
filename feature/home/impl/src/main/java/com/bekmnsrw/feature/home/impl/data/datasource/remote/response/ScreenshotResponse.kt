package com.bekmnsrw.feature.home.impl.data.datasource.remote.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class ScreenshotResponse(
    @SerialName("original") val original: String,
    @SerialName("preview") val preview: String
)
