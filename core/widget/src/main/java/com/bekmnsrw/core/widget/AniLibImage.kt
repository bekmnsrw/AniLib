package com.bekmnsrw.core.widget

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import coil.size.Scale

private const val IMAGE_BASE_URL = "https://shikimori.one/"

@Composable
fun AniLibImage(
    modifier: Modifier,
    imageUrl: String,
    alpha: Float = 1.0f,
    isAvatar: Boolean = false
) {
    AsyncImage(
        modifier = modifier,
        contentDescription = null,
        contentScale = ContentScale.Crop,
        alpha = alpha,
        model = ImageRequest.Builder(LocalContext.current)
            .data(
                data = when(isAvatar) {
                    true -> imageUrl
                    false -> "$IMAGE_BASE_URL$imageUrl"
                }
            )
            .error(R.drawable.ic_broken_image)
            .fallback(R.drawable.ic_broken_image)
            .placeholder(R.drawable.loading_animation)
            .crossfade(enable = true)
            .scale(Scale.FILL)
            .diskCachePolicy(policy = CachePolicy.ENABLED)
            .build()
    )
}
