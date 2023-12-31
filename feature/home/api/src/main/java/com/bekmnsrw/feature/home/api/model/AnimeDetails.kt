package com.bekmnsrw.feature.home.api.model

data class AnimeDetails(
    val airedOn: String?,
    val description: String?,
    val duration: Int?,
    val episodes: Int?,
    val episodesAired: Int?,
    val favoured: Boolean,
    val genres: List<Genre>,
    val id: Int,
    val image: AnimeImage,
    val japanese: List<String?>,
    val kind: String,
    val name: String,
    val nextEpisodeAt: String?,
    val scoresStats: List<RatesScoresStat?>,
    val statusesStats: List<RatesStatusesStat?>,
    val releasedOn: String?,
    val russian: String?,
    val score: String,
    val status: String,
    val synonyms: List<String?>,
    val totalScoresStats: Int,
    val totalStatusesStats: Int,
    val rating: String,
    val userRates: UserRates?
)
