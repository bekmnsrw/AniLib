package com.bekmnsrw.feature.favorites.impl.data.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class UserResponse(
    @SerialName("avatar") val avatar: String,
    @SerialName("id") val id: Int,
    @SerialName("image") val userImageResponse: UserImageResponse,
    @SerialName("last_online_at") val lastOnlineAt: String,
    @SerialName("nickname") val nickname: String,
    @SerialName("url") val url: String
)
