package com.bekmnsrw.feature.favorites.impl.presentation.dropped

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
import com.bekmnsrw.feature.favorites.impl.presentation.dropped.DroppedScreenModel.DroppedScreenAction
import com.bekmnsrw.feature.favorites.impl.presentation.dropped.DroppedScreenModel.DroppedScreenEvent.OnItemClicked

internal class DroppedScreen : Screen {

    @Composable
    override fun Content() {
        val screenModel = getScreenModel<DroppedScreenModel>()
        val droppedAnimePaged = screenModel.dropped.collectAsLazyPagingItems()
        val screenAction by screenModel.screenAction.collectAsStateWithLifecycle(initialValue = null)

        if (droppedAnimePaged.loadState.refresh == LoadState.Loading) {
            AniLibCircularProgressBar(shouldShow = true)
        } else {
            DroppedScreenContent(
                droppedAnimePaged = droppedAnimePaged,
                onItemClicked = {
                    screenModel.eventHandler(
                        OnItemClicked(
                            id = it
                        )
                    )
                }
            )
        }

        DroppedScreenActions(screenAction = screenAction)
    }
}

@Composable
private fun DroppedScreenActions(screenAction: DroppedScreenAction?) {
    val navigator = LocalNavigator.currentOrThrow

    LaunchedEffect(screenAction) {
        when (screenAction) {
            null -> Unit

            is DroppedScreenAction.NavigateDetails -> {
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
private fun DroppedScreenContent(
    droppedAnimePaged: LazyPagingItems<UserRate>,
    onItemClicked: (Int) -> Unit
) {
    TabAnimeList(
        userRatePaged = droppedAnimePaged,
        status = UserRatesEnum.DROPPED.status,
        onItemClicked = onItemClicked
    )
}
