package com.bekmnsrw.feature.auth.api.model

data class AccessToken(
    val accessToken: String,
    val tokenType: String,
    val expiresIn: Long,
    val refreshToken: String,
    val scope: String,
    val createdAt: Long
)
