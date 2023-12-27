package com.bekmnsrw.feature.favorites.impl.data.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class UpdatedUserRatesResponse(
    @SerialName("chapters") val chapters: Int?,
    @SerialName("created_at") val createdAt: String?,
    @SerialName("episodes") val episodes: Int?,
    @SerialName("id") val id: Int?,
    @SerialName("rewatches") val rewatches: Int?,
    @SerialName("score") val score: Int?,
    @SerialName("status") val status: String?,
    @SerialName("target_id") val targetId: Int?,
    @SerialName("target_type") val targetType: String?,
    @SerialName("text") val text: String?,
    @SerialName("text_html") val textHtml: String?,
    @SerialName("updated_at") val updatedAt: String?,
    @SerialName("user_id") val userId: Int?,
    @SerialName("volumes") val volumes: Int?
)
