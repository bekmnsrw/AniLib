package com.bekmnsrw.feature.favorites.impl

enum class UserRatesEnum(
    val key: String,
    val value: String
) {
    COMPLETED(key = "completed" ,value = "Completed"),
    DROPPED(key = "dropped", value = "Dropped"),
    ON_HOLD(key = "on_hold", value = "On hold"),
    PLANNED(key = "planned", value = "Planned"),
    WATCHING(key = "watching", value = "Watching")
}
