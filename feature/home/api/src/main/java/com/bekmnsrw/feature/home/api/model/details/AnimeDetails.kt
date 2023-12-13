package com.bekmnsrw.feature.home.api.model.details

import com.bekmnsrw.feature.home.api.model.list.AnimeImage

data class AnimeDetails(
    val airedOn: String?,
    val description: String?,
    val duration: Int?,
    val english: List<String?>,
    val episodes: Int?,
    val episodesAired: Int?,
    val favoured: Boolean,
    val genres: List<Genre?>,
    val id: Int,
    val image: AnimeImage,
    val japanese: List<String?>,
    val kind: String?,
    val name: String,
    val nextEpisodeAt: String?,
    val ratesScoresStats: List<RatesScoresStat?>,
    val ratesStatusesStats: List<RatesStatusesStat?>,
    val releasedOn: String?,
    val russian: String?,
    val score: String?,
    val status: String?,
    val userRate: UserRate?,
//    val videos: List<Video?>
//    val screenshots: List<Screenshot?>,
//    val threadId: Int?,
//    val topicId: Int?,
)
