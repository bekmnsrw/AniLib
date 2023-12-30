package com.bekmnsrw.feature.home.api.model

data class Anime(
    val id: Int,
    val name: String,
    val score: String,
    val status: String,
    val airedOn: String,
    val releasedOn: String,
    val numberOfEpisodes: String,
    val episodesAired: Int,
    val kind: String,
    val image: AnimeImage,
    val russian: String
)
