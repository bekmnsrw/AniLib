package com.bekmnsrw.feature.home.impl.data.datasource.remote.response.details

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class StudioResponse(
    @SerialName("filtered_name") val filteredName: String,
    @SerialName("id") val id: Int,
    @SerialName("image") val image: String,
    @SerialName("name") val name: String,
    @SerialName("real") val real: Boolean
)
