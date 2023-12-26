package com.bekmnsrw.core.widget

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest

private const val IMAGE_BASE_URL = "https://shikimori.one/"

@Composable
fun AniLibImage(
    modifier: Modifier,
    imageUrl: String,
    alpha: Float = 1.0f
) {
    AsyncImage(
        modifier = modifier,
        contentDescription = null,
        contentScale = ContentScale.Crop,
        alpha = alpha,
        model = ImageRequest.Builder(LocalContext.current)
            .data(data = "$IMAGE_BASE_URL$imageUrl")
            .crossfade(enable = true)
            .diskCachePolicy(policy = CachePolicy.ENABLED)
            .build()
    )
}
