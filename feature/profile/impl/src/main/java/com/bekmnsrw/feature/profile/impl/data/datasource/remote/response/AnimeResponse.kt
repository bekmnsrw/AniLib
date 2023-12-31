package com.bekmnsrw.feature.profile.impl.data.datasource.remote.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class AnimeResponse(
    @SerialName("aired_on") val airedOn: String?,
    @SerialName("episodes") val episodes: Int?,
    @SerialName("episodes_aired") val episodesAired: Int?,
    @SerialName("id") val id: Int,
    @SerialName("image") val image: Image,
    @SerialName("kind") val kind: String,
    @SerialName("name") val name: String,
    @SerialName("released_on") val releasedOn: String?,
    @SerialName("russian") val russian: String,
    @SerialName("score") val score: String,
    @SerialName("status") val status: String,
    @SerialName("url") val url: String?
)
