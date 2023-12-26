package com.bekmnsrw.feature.favorites.impl.presentation.planned

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import cafe.adriel.voyager.core.registry.ScreenRegistry
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.bekmnsrw.core.navigation.SharedScreen
import com.bekmnsrw.core.widget.AniLibCircularProgressBar
import com.bekmnsrw.feature.favorites.api.model.UserRate
import com.bekmnsrw.feature.favorites.impl.UserRatesEnum
import com.bekmnsrw.feature.favorites.impl.presentation.container.TabAnimeList
import com.bekmnsrw.feature.favorites.impl.presentation.planned.PlannedScreenModel.*
import com.bekmnsrw.feature.favorites.impl.presentation.planned.PlannedScreenModel.PlannedScreenEvent.*

internal class PlannedScreen : Screen {

    @Composable
    override fun Content() {
        val screenModel = getScreenModel<PlannedScreenModel>()
        val plannedAnimePaged = screenModel.planned.collectAsLazyPagingItems()
        val screenAction by screenModel.screenAction.collectAsStateWithLifecycle(initialValue = null)

        if (plannedAnimePaged.loadState.refresh == LoadState.Loading) {
            AniLibCircularProgressBar(shouldShow = true)
        } else {
            PlannedToWatchScreenContent(
                plannedAnimePaged = plannedAnimePaged,
                onItemClicked = {
                    screenModel.eventHandler(
                        OnItemClicked(
                            id = it
                        )
                    )
                }
            )
        }

        PlannedScreenActions(screenAction = screenAction)
    }
}

@Composable
private fun PlannedScreenActions(screenAction: PlannedScreenAction?) {
    val navigator = LocalNavigator.currentOrThrow

    LaunchedEffect(screenAction) {
        when (screenAction) {
            null -> Unit

            is PlannedScreenAction.NavigateDetails -> {
                val detailsScreen = ScreenRegistry.get(
                    provider = SharedScreen.DetailsScreen(
                        id = screenAction.id
                    )
                )
                navigator.push(item = detailsScreen)
            }
        }
    }
}

@Composable
private fun PlannedToWatchScreenContent(
    plannedAnimePaged: LazyPagingItems<UserRate>,
    onItemClicked: (Int) -> Unit
) {
    TabAnimeList(
        userRatePaged = plannedAnimePaged,
        status = UserRatesEnum.PLANNED.status,
        onItemClicked = onItemClicked
    )
}
