package com.bekmnsrw.feature.home.api.model.details

data class UserRate(
    val chapters: Int,
    val createdAt: String,
    val episodes: Int,
    val id: Int,
    val rewatches: Int,
    val score: Int,
    val status: String,
    val text: String?,
    val textHtml: String,
    val updatedAt: String,
    val volumes: Int
)
