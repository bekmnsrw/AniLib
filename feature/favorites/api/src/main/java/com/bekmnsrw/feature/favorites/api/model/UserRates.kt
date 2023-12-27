package com.bekmnsrw.feature.favorites.api.model

import com.bekmnsrw.feature.home.api.model.Anime

data class UserRates(
    val id: Int,
    val anime: Anime,
    val userScore: Int,
    val userStatus: String,
    val episodesWatched: Int,
    val rewatches: Int
)
