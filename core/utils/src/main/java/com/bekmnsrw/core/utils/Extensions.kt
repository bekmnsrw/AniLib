package com.bekmnsrw.core.utils

fun formatStatusString(status: String?): String? = status
    ?.replace(oldValue = "_", newValue = " ")
    ?.replaceFirstChar { it.uppercase() }

fun convertNextEpisodeAt(nextEpisodeAt: String): Pair<String, String> {
    val dateTime = nextEpisodeAt.split("T")
    return Pair(dateTime[0], dateTime[1].substring(0, 5))
}
