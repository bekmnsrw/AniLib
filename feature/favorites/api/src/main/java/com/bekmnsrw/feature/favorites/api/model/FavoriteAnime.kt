package com.bekmnsrw.feature.favorites.api.model

data class FavoriteAnime(
    val id: Int,
    val name: String,
    val russian: String,
    val image: String,
    val isFavourite: Boolean = true
)
