package com.bekmnsrw.feature.favorites.impl.presentation.watching

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
import com.bekmnsrw.feature.favorites.api.model.UserRate
import com.bekmnsrw.feature.favorites.impl.UserRatesEnum
import com.bekmnsrw.feature.favorites.impl.presentation.container.TabAnimeList
import com.bekmnsrw.feature.favorites.impl.presentation.watching.WatchingScreenModel.WatchingScreenAction
import com.bekmnsrw.feature.favorites.impl.presentation.watching.WatchingScreenModel.WatchingScreenEvent.OnItemClicked

internal class WatchingScreen : Screen {

    @Composable
    override fun Content() {
        val screenModel = getScreenModel<WatchingScreenModel>()
        val watchingAnimePaged = screenModel.watching.collectAsLazyPagingItems()
        val screenAction by screenModel.screenAction.collectAsStateWithLifecycle(initialValue = null)

        WatchingScreenContent(
            watchingAnimePaged = watchingAnimePaged,
            onItemClicked = { screenModel.eventHandler(OnItemClicked(id = it)) }
        )

        WatchingScreenActions(screenAction = screenAction)
    }
}

@Composable
private fun WatchingScreenActions(screenAction: WatchingScreenAction?) {
    val navigator = LocalNavigator.currentOrThrow

    LaunchedEffect(screenAction) {
        when (screenAction) {
            null -> Unit

            is WatchingScreenAction.NavigateDetails -> {
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
private fun WatchingScreenContent(
    watchingAnimePaged: LazyPagingItems<UserRate>,
    onItemClicked: (Int) -> Unit
) {
    TabAnimeList(
        userRatePaged = watchingAnimePaged,
        status = UserRatesEnum.WATCHING.status,
        onItemClicked = onItemClicked,
        isLoading = watchingAnimePaged.loadState.refresh == LoadState.Loading
    )
}
