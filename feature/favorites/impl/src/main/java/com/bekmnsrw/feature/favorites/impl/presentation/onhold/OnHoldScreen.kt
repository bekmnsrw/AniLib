package com.bekmnsrw.feature.favorites.impl.presentation.onhold

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
import com.bekmnsrw.feature.favorites.impl.presentation.onhold.OnHoldScreenModel.OnHoldScreenAction
import com.bekmnsrw.feature.favorites.impl.presentation.onhold.OnHoldScreenModel.OnHoldScreenEvent.OnItemClicked

internal class OnHoldScreen : Screen {

    @Composable
    override fun Content() {
        val screenModel = getScreenModel<OnHoldScreenModel>()
        val onHoldAnimePaged = screenModel.onHold.collectAsLazyPagingItems()
        val screenAction by screenModel.screenAction.collectAsStateWithLifecycle(initialValue = null)

        OnHoldScreenContent(
            onHoldAnimePaged = onHoldAnimePaged,
            onItemClicked = { screenModel.eventHandler(OnItemClicked(id = it)) }
        )

        OnHoldScreenActions(screenAction = screenAction)
    }
}

@Composable
private fun OnHoldScreenActions(screenAction: OnHoldScreenAction?) {
    val navigator = LocalNavigator.currentOrThrow

    LaunchedEffect(screenAction) {
        when (screenAction) {
            null -> Unit
            is OnHoldScreenAction.NavigateDetails -> {
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
private fun OnHoldScreenContent(
    onHoldAnimePaged: LazyPagingItems<UserRate>,
    onItemClicked: (Int) -> Unit
) {
    TabAnimeList(
        userRatePaged = onHoldAnimePaged,
        status = UserRatesEnum.PLANNED.status,
        onItemClicked = onItemClicked,
        isLoading = onHoldAnimePaged.loadState.refresh == LoadState.Loading
    )
}
