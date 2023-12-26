package com.bekmnsrw.feature.favorites.impl.presentation.completed

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
import com.bekmnsrw.feature.favorites.impl.presentation.completed.CompletedScreenModel.CompletedScreenAction
import com.bekmnsrw.feature.favorites.impl.presentation.completed.CompletedScreenModel.CompletedScreenEvent.OnItemClicked
import com.bekmnsrw.feature.favorites.impl.presentation.container.TabAnimeList

internal class CompletedScreen : Screen {

    @Composable
    override fun Content() {
        val screenModel = getScreenModel<CompletedScreenModel>()
        val completedAnimePaged = screenModel.completed.collectAsLazyPagingItems()
        val screenAction by screenModel.screenAction.collectAsStateWithLifecycle(initialValue = null)

        CompletedScreenContent(
            completedAnimePaged = completedAnimePaged,
            onItemClicked = { screenModel.eventHandler(OnItemClicked(id = it)) }
        )

        CompletedScreenActions(screenAction = screenAction)
    }
}

@Composable
private fun CompletedScreenActions(screenAction: CompletedScreenAction?) {
    val navigator = LocalNavigator.currentOrThrow

    LaunchedEffect(screenAction) {
        when (screenAction) {
            null -> Unit

            is CompletedScreenAction.NavigateDetails -> {
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
private fun CompletedScreenContent(
    completedAnimePaged: LazyPagingItems<UserRate>,
    onItemClicked: (Int) -> Unit
) {
    TabAnimeList(
        userRatePaged = completedAnimePaged,
        status = UserRatesEnum.COMPLETED.status,
        onItemClicked = onItemClicked,
        isLoading = completedAnimePaged.loadState.refresh == LoadState.Loading
    )
}
