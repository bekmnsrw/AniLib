package com.bekmnsrw.feature.home.impl.data.datasource.remote.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class VideoResponse(
    @SerialName("hosting") val hosting: String,
    @SerialName("id") val id: Int,
    @SerialName("image_url") val imageUrl: String,
    @SerialName("kind") val kind: String,
    @SerialName("name") val name: String,
    @SerialName("player_url") val playerUrl: String,
    @SerialName("url") val url: String
)
