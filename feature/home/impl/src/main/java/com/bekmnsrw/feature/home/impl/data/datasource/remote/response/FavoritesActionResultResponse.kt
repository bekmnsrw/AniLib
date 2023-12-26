package com.bekmnsrw.feature.home.impl.data.datasource.remote.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class FavoritesActionResultResponse(
    @SerialName("success") val success: Boolean,
    @SerialName("notice") val notice: String
)
