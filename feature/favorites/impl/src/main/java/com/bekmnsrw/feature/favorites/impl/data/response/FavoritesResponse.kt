package com.bekmnsrw.feature.favorites.impl.data.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class FavoritesResponse(
    @SerialName("animes") val animes: List<FavoriteAnimeResponse>
)
