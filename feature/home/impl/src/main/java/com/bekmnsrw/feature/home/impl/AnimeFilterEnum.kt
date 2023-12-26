package com.bekmnsrw.feature.home.impl

enum class AnimeFilterEnum(
    val key: String,
    val value: String
) {
    BY_RANK("Rank", "ranked"),
    BY_POPULARITY("Popularity", "popularity"),
    BY_NAME("Name", "name"),
    BY_RANDOM("Random", "random")
}
