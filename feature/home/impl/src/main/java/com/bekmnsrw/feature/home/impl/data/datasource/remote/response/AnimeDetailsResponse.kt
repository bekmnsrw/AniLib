package com.bekmnsrw.feature.home.impl.data.datasource.remote.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class AnimeDetailsResponse(
    @SerialName("id") val id: Int,
    @SerialName("name") val name: String,
    @SerialName("russian") val russian: String?,
    @SerialName("japanese") val japanese: List<String?>,
    @SerialName("english") val english: List<String?>,
    @SerialName("synonyms") val synonyms: List<String>,
    @SerialName("image") val image: AnimeImageResponse,
    @SerialName("status") val status: String,
    @SerialName("genres") val genres: List<GenreResponse>,
    @SerialName("kind") val kind: String,
    @SerialName("episodes") val episodes: Int,
    @SerialName("aired_on") val airedOn: String?,
    @SerialName("released_on") val releasedOn: String?,
    @SerialName("episodes_aired") val episodesAired: Int?,
    @SerialName("next_episode_at") val nextEpisodeAt: String?,
    @SerialName("duration") val duration: Int?,
    @SerialName("description") val description: String?,
    @SerialName("favoured") val favoured: Boolean,
    @SerialName("rates_scores_stats") val ratesScoresStats: List<RatesScoresStatResponse?>,
    @SerialName("rates_statuses_stats") val ratesStatusesStats: List<RatesStatusesStatResponse?>,
    @SerialName("score") val score: String,
    @SerialName("rating") val rating: String,
    @SerialName("user_rate") val userRate: UserRatesResponse?
)
