package com.bekmnsrw.feature.home.impl.data

import com.bekmnsrw.feature.home.api.model.details.AnimeDetails
import com.bekmnsrw.feature.home.api.model.details.Genre
import com.bekmnsrw.feature.home.api.model.details.RatesScoresStat
import com.bekmnsrw.feature.home.api.model.details.RatesStatusesStat
import com.bekmnsrw.feature.home.api.model.details.Screenshot
import com.bekmnsrw.feature.home.api.model.details.UserRate
import com.bekmnsrw.feature.home.api.model.details.Video
import com.bekmnsrw.feature.home.api.model.list.Anime
import com.bekmnsrw.feature.home.api.model.list.AnimeImage
import com.bekmnsrw.feature.home.impl.data.datasource.remote.response.details.AnimeDetailsResponse
import com.bekmnsrw.feature.home.impl.data.datasource.remote.response.details.GenreResponse
import com.bekmnsrw.feature.home.impl.data.datasource.remote.response.details.RatesScoresStatResponse
import com.bekmnsrw.feature.home.impl.data.datasource.remote.response.details.RatesStatusesStatResponse
import com.bekmnsrw.feature.home.impl.data.datasource.remote.response.details.ScreenshotResponse
import com.bekmnsrw.feature.home.impl.data.datasource.remote.response.details.UserRateResponse
import com.bekmnsrw.feature.home.impl.data.datasource.remote.response.details.VideoResponse
import com.bekmnsrw.feature.home.impl.data.datasource.remote.response.list.AnimeImageResponse
import com.bekmnsrw.feature.home.impl.data.datasource.remote.response.list.AnimeResponse

internal fun AnimeResponse.toAnime(): Anime = Anime(
    airedOn = airedOn ?: "",
    numberOfEpisodes = if (numberOfEpisodes == 0) "?" else "$numberOfEpisodes",
    episodesAired = episodesAired,
    id = id,
    image = image.toAnimeImage(),
    kind = kind,
    name = name,
    releasedOn = releasedOn ?: "",
    russianName = russianName,
    score = score,
    status = status
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
    english = english,
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
    ratesScoresStats = ratesScoresStats.toRatesScoreStatList(),
    ratesStatusesStats = ratesStatusesStats.toRatesStatusesStatList(),
    releasedOn = releasedOn,
    russian = russian,
    score = score,
    status = status,
    userRate = userRate?.toUserRate(),
//    videos = videos.toVideoList()
//    screenshots = screenshots.toScreenshotList(),
//    threadId = threadId,
//    topicId = topicId,
)

internal fun GenreResponse.toGenre(): Genre = Genre(
    entryType = entryType,
    id = id,
    kind = kind,
    name = name,
    russian = russian
)

internal fun List<GenreResponse?>.toGenreList(): List<Genre?> = this.map { it?.toGenre() }

internal fun RatesScoresStatResponse.toRatesScoresStat(): RatesScoresStat = RatesScoresStat(
    name = name,
    value = value
)

internal fun List<RatesScoresStatResponse?>.toRatesScoreStatList(): List<RatesScoresStat?> = this.map {
    it?.toRatesScoresStat()
}

internal fun RatesStatusesStatResponse.toRatesStatusesStat(): RatesStatusesStat = RatesStatusesStat(
    name = name,
    value = value
)

internal fun List<RatesStatusesStatResponse?>.toRatesStatusesStatList(): List<RatesStatusesStat?> = this.map {
    it?.toRatesStatusesStat()
}

internal fun UserRateResponse.toUserRate(): UserRate = UserRate(
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

internal fun VideoResponse.toVideo(): Video = Video(
    hosting = hosting,
    id = id,
    imageUrl = imageUrl,
    kind = kind,
    name = name,
    playerUrl = playerUrl,
    url = url
)

internal fun List<VideoResponse?>.toVideoList(): List<Video?> = this.map { it?.toVideo() }

internal fun ScreenshotResponse.toScreenshot(): Screenshot = Screenshot(
    original = original,
    preview = preview
)

internal fun List<ScreenshotResponse?>.toScreenshotList(): List<Screenshot?> = this.map { it?.toScreenshot() }
