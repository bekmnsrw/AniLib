package com.bekmnsrw.feature.home.impl.data.datasource.remote.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class FavoritesResponse(
    @SerialName("animes") val animes: List<AnimeResponse>
)
