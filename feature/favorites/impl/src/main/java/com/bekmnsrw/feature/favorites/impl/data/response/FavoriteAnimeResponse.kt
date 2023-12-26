package com.bekmnsrw.feature.favorites.impl.data.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class FavoriteAnimeResponse(
    @SerialName("id") val id: Int,
    @SerialName("name") val name: String,
    @SerialName("russian") val russian: String,
    @SerialName("image") val image: String,
    @SerialName("url") val url: String?
)
