package com.bekmnsrw.feature.home.api.model

data class Video(
    val hosting: String,
    val id: Int,
    val imageUrl: String,
    val kind: String,
    val name: String,
    val playerUrl: String,
    val url: String
)
