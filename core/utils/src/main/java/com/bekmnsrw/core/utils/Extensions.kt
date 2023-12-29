package com.bekmnsrw.core.utils

fun formatStatusString(status: String?): String? = status
    ?.replace(oldValue = "_", newValue = " ")
    ?.replaceFirstChar { it.uppercase() }
