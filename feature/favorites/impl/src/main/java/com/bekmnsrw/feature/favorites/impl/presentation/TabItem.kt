package com.bekmnsrw.feature.favorites.impl.presentation

import androidx.compose.runtime.Composable
import com.bekmnsrw.feature.favorites.impl.presentation.completed.CompletedScreen
import com.bekmnsrw.feature.favorites.impl.presentation.dropped.DroppedScreen
import com.bekmnsrw.feature.favorites.impl.presentation.favorites.FavoritesScreen
import com.bekmnsrw.feature.favorites.impl.presentation.onhold.OnHoldScreen
import com.bekmnsrw.feature.favorites.impl.presentation.planned.PlannedScreen
import com.bekmnsrw.feature.favorites.impl.presentation.watching.WatchingScreen

typealias ScreenContent = @Composable () -> Unit

sealed class TabItem(
    val title: String,
    val screenContent: ScreenContent
) {
    data object Favorites : TabItem(
        title = "Favorites",
        screenContent = { FavoritesScreen().Content() }
    )
    data object PlannedToWatch : TabItem(
        title = "Planned to watch",
        screenContent = { PlannedScreen().Content() }
    )
    data object Completed : TabItem(
        title = "Completed",
        screenContent = { CompletedScreen().Content() }
    )
    data object Watching : TabItem(
        title = "Watching",
        screenContent = { WatchingScreen().Content() }
    )
    data object Dropped : TabItem(
        title = "Dropped",
        screenContent = { DroppedScreen().Content() }
    )
    data object OnHold : TabItem(
        title = "On hold",
        screenContent = { OnHoldScreen().Content() }
    )
}
