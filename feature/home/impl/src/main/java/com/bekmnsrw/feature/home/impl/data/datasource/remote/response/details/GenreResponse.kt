package com.bekmnsrw.feature.home.impl.data.datasource.remote.response.details

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class GenreResponse(
    @SerialName("entry_type") val entryType: String,
    @SerialName("id") val id: Int,
    @SerialName("kind") val kind: String,
    @SerialName("name") val name: String,
    @SerialName("russian") val russian: String
)
