package com.bekmnsrw.core.widget

enum class UserRatesEnum(
    val key: String,
    val value: String
) {
    NOT_IN_MY_LIST(key = "not_in_my_list", value = "Not in my list"),
    COMPLETED(key = "completed" ,value = "Completed"),
    DROPPED(key = "dropped", value = "Dropped"),
    ON_HOLD(key = "on_hold", value = "On hold"),
    PLANNED(key = "planned", value = "Planned"),
    WATCHING(key = "watching", value = "Watching")
}
