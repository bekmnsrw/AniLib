package com.bekmnsrw.feature.home.api.model.list

data class Anime(
    val airedOn: String,
    val numberOfEpisodes: String,
    val episodesAired: Int,
    val id: Int,
    val image: AnimeImage,
    val kind: String,
    val name: String,
    val releasedOn: String,
    val russianName: String,
    val score: String,
    val status: String
)
