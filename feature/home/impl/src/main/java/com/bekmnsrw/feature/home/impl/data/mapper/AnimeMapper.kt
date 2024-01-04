package com.bekmnsrw.feature.home.impl.data.mapper

import com.bekmnsrw.feature.home.api.model.Anime
import com.bekmnsrw.feature.home.api.model.AnimeDetails
import com.bekmnsrw.feature.home.api.model.AnimeImage
import com.bekmnsrw.feature.home.api.model.FavoritesActionResult
import com.bekmnsrw.feature.home.api.model.Genre
import com.bekmnsrw.feature.home.api.model.RatesScoresStat
import com.bekmnsrw.feature.home.api.model.RatesStatusesStat
import com.bekmnsrw.feature.home.api.model.UserRates
import com.bekmnsrw.feature.home.impl.data.datasource.remote.response.AnimeDetailsResponse
import com.bekmnsrw.feature.home.impl.data.datasource.remote.response.AnimeImageResponse
import com.bekmnsrw.feature.home.impl.data.datasource.remote.response.AnimeResponse
import com.bekmnsrw.feature.home.impl.data.datasource.remote.response.CreateUserRatesResponse
import com.bekmnsrw.feature.home.impl.data.datasource.remote.response.FavoritesActionResultResponse
import com.bekmnsrw.feature.home.impl.data.datasource.remote.response.GenreResponse
import com.bekmnsrw.feature.home.impl.data.datasource.remote.response.RatesScoresStatResponse
import com.bekmnsrw.feature.home.impl.data.datasource.remote.response.RatesStatusesStatResponse
import com.bekmnsrw.feature.home.impl.data.datasource.remote.response.UserRatesResponse

internal fun AnimeResponse.toAnime(): Anime = Anime(
    airedOn = airedOn ?: "",
    numberOfEpisodes = if (numberOfEpisodes == 0) "?" else "$numberOfEpisodes",
    episodesAired = episodesAired,
    id = id,
    image = image.toAnimeImage(),
    kind = kind,
    name = name,
    releasedOn = releasedOn ?: "",
    score = score,
    status = status,
    russian = russianName
)

internal fun AnimeImageResponse.toAnimeImage(): AnimeImage = AnimeImage(
    original = original,
    preview = preview,
    x48 = x48,
    x96 = x96
)

internal fun List<AnimeResponse>.toAnimeList(): List<Anime> = this.map { it.toAnime() }

internal fun AnimeDetailsResponse.toAnimeDetails(): AnimeDetails = AnimeDetails(
    airedOn = airedOn,
    description = description,
    duration = duration,
    episodes = episodes,
    episodesAired = episodesAired,
    favoured = favoured,
    genres = genres.toGenreList(),
    id = id,
    image = image.toAnimeImage(),
    japanese = japanese,
    kind = kind,
    name = name,
    nextEpisodeAt = nextEpisodeAt,
    scoresStats = ratesScoresStats.toRatesScoreStatList(),
    statusesStats = ratesStatusesStats.toRatesStatusesStatList(),
    releasedOn = releasedOn,
    russian = russian,
    score = if (score == "0.0") "?" else score,
    status = status,
    synonyms = synonyms + english,
    totalScoresStats = getTotalScoresStats(ratesScoresStats),
    totalStatusesStats = getTotalStatusesStats(ratesStatusesStats),
    rating = rating,
    userRates = userRate?.toUserRate()
)

internal fun getTotalScoresStats(
    ratesScoresStatsResponse: List<RatesScoresStatResponse?>
) = ratesScoresStatsResponse.sumOf { it?.value ?: 0 }

internal fun getTotalStatusesStats(
    ratesStatusesStatResponse: List<RatesStatusesStatResponse?>
) = ratesStatusesStatResponse.sumOf { it?.value ?: 0 }

internal fun GenreResponse.toGenre(): Genre = Genre(
    entryType = entryType,
    id = id,
    kind = kind,
    name = name,
    russian = russian
)

internal fun List<GenreResponse>.toGenreList(): List<Genre> = this.map {
    it.toGenre()
}

internal fun RatesScoresStatResponse?.toRatesScoresStat(): RatesScoresStat = RatesScoresStat(
    name = this?.name ?: 0,
    value = this?.value ?: 0
)

internal fun List<RatesScoresStatResponse?>.toRatesScoreStatList(): List<RatesScoresStat> = this.map {
    it.toRatesScoresStat()
}

internal fun RatesStatusesStatResponse?.toRatesStatusesStat(): RatesStatusesStat =
    RatesStatusesStat(
        name = this?.name ?: "",
        value = this?.value ?: 0
    )

internal fun List<RatesStatusesStatResponse?>.toRatesStatusesStatList(): List<RatesStatusesStat> = this.map {
    it.toRatesStatusesStat()
}

internal fun UserRatesResponse.toUserRate(): UserRates = UserRates(
    chapters = chapters,
    createdAt = createdAt,
    episodes = episodes,
    id = id,
    rewatches = rewatches,
    score = score,
    status = status,
    text = text,
    textHtml = textHtml,
    updatedAt = updatedAt,
    volumes = volumes
)

internal fun FavoritesActionResultResponse.toFavoritesActionResult(): FavoritesActionResult =
    FavoritesActionResult(
        success = success,
        notice = notice
    )

internal fun CreateUserRatesResponse.toUserRates(): UserRates = UserRates(
    chapters = chapters,
    createdAt = createdAt,
    episodes = episodes,
    id = id,
    rewatches = rewatches,
    score = score,
    status = status,
    text = text,
    textHtml = textHtml,
    updatedAt = updatedAt,
    volumes = volumes
)
