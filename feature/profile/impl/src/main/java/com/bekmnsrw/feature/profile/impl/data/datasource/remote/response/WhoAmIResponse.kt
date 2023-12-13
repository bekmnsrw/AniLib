package com.bekmnsrw.feature.profile.impl.data.datasource.remote.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class WhoAmIResponse(
    @SerialName("avatar") val avatar: String,
    @SerialName("birth_on") val birthOn: String?,
    @SerialName("full_years") val fullYears: Int?,
    @SerialName("id") val id: Int,
    @SerialName("image") val image: ImageResponse,
    @SerialName("last_online_at") val lastOnlineAt: String,
    @SerialName("locale") val locale: String,
    @SerialName("name") val name: String?,
    @SerialName("nickname") val nickname: String,
    @SerialName("sex") val sex: String?,
    @SerialName("url") val url: String,
    @SerialName("website") val website: String?
)
