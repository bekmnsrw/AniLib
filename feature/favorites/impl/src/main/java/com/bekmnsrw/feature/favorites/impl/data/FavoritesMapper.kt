package com.bekmnsrw.feature.favorites.impl.data

import com.bekmnsrw.feature.favorites.api.model.FavoriteAnime
import com.bekmnsrw.feature.favorites.api.model.UserRate
import com.bekmnsrw.feature.favorites.impl.data.response.AnimeImageResponse
import com.bekmnsrw.feature.favorites.impl.data.response.AnimeResponse
import com.bekmnsrw.feature.favorites.impl.data.response.FavoriteAnimeResponse
import com.bekmnsrw.feature.favorites.impl.data.response.UserRatesResponse
import com.bekmnsrw.feature.home.api.model.Anime
import com.bekmnsrw.feature.home.api.model.AnimeImage

internal fun UserRatesResponse.toUserRate(): UserRate = UserRate(
    anime = animeResponse.toAnime(),
    userScore = score,
    userStatus = status,
    episodesWatched = episodes,
    rewatches = rewatches
)

internal fun FavoriteAnimeResponse.toFavoriteAnime(): FavoriteAnime = FavoriteAnime(
    id = id,
    name = name,
    russian = russian,
    image = image
)

internal fun List<FavoriteAnimeResponse>.toFavoriteAnimeList(): List<FavoriteAnime> = this.map { it.toFavoriteAnime() }

internal fun List<UserRatesResponse>.toUserRateList(): List<UserRate> = this.map {
    it.toUserRate()
}

internal fun AnimeResponse.toAnime(): Anime = Anime(
    airedOn = airedOn ?: "",
    numberOfEpisodes = if (episodes == 0) "?" else "$episodes",
    episodesAired = episodesAired,
    id = id,
    image = animeImageResponse.toAnimeImage(),
    kind = kind,
    name = name,
    releasedOn = releasedOn ?: "",
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
