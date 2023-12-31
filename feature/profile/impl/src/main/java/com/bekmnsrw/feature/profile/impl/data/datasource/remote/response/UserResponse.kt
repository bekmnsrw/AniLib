package com.bekmnsrw.feature.profile.impl.data.datasource.remote.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class UserResponse(
    @SerialName("avatar") val avatar: String,
    @SerialName("id") val id: Int,
    @SerialName("image") val image: UserImageResponse,
    @SerialName("last_online_at") val lastOnlineAt: String,
    @SerialName("nickname") val nickname: String,
    @SerialName("url") val url: String
)
