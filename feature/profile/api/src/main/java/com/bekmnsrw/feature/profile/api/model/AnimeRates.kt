package com.bekmnsrw.feature.profile.api.model

data class AnimeRates(
    val image: String,
    val name: String,
    val russian: String,
    val updatedAt: String,
    val score: Int?,
    val status: String?,
    val animeId: Int
)
