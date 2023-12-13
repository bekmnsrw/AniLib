package com.bekmnsrw.feature.profile.api.model

data class WhoAmI(
    val avatar: String,
    val birthOn: String?,
    val fullYears: Int?,
    val id: Int,
    val image: Image,
    val lastOnlineAt: String,
    val locale: String,
    val name: String?,
    val nickname: String,
    val sex: String?,
    val url: String,
    val website: String?
)
