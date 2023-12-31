package com.bekmnsrw.core.utils

fun formatStatusString(status: String?): String? = status
    ?.replace(oldValue = "_", newValue = " ")
    ?.replaceFirstChar { it.uppercase() }

fun convertStringToDateTime(string: String): Pair<String, String> {
    val dateTime = string.split("T")
    return Pair(dateTime[0], dateTime[1].substring(0, 5))
}
