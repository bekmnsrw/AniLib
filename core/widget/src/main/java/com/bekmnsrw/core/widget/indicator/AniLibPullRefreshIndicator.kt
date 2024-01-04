package com.bekmnsrw.core.widget.indicator

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AniLibPullRefreshIndicator(
    modifier: Modifier,
    refreshing: Boolean,
    pullRefreshState: PullRefreshState
) {
    PullRefreshIndicator(
        refreshing = refreshing,
        state = pullRefreshState,
        modifier = modifier,
        backgroundColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.background
    )
}
