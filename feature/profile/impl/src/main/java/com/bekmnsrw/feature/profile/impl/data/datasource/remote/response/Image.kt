package com.bekmnsrw.feature.profile.impl.data.datasource.remote.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class Image(
    @SerialName("original") val original: String,
    @SerialName("preview") val preview: String,
    @SerialName("x48") val x48: String,
    @SerialName("x96") val x96: String
)
