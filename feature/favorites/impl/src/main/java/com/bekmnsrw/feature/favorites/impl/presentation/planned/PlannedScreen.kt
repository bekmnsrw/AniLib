package com.bekmnsrw.feature.favorites.impl.presentation.planned

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
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
import com.bekmnsrw.feature.favorites.api.model.UserRates
import com.bekmnsrw.feature.favorites.impl.UserRatesEnum
import com.bekmnsrw.feature.favorites.impl.presentation.container.AnimeBottomSheet
import com.bekmnsrw.feature.favorites.impl.presentation.container.TabAnimeList
import com.bekmnsrw.feature.favorites.impl.presentation.planned.PlannedScreenModel.PlannedScreenAction
import com.bekmnsrw.feature.favorites.impl.presentation.planned.PlannedScreenModel.PlannedScreenEvent.*

internal class PlannedScreen : Screen {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val screenModel = getScreenModel<PlannedScreenModel>()
        val screenState by screenModel.screenState.collectAsStateWithLifecycle()
        val screenAction by screenModel.screenAction.collectAsStateWithLifecycle(initialValue = null)
        val plannedAnimePaged = screenModel.planned.collectAsLazyPagingItems()

        val modalBottomSheetState = rememberModalBottomSheetState()

        PlannedToWatchScreenContent(
            plannedAnimePaged = plannedAnimePaged,
            modalBottomSheetState = modalBottomSheetState,
            shouldShowModalBottomSheet = screenState.shouldShowModalBottomSheet,
            selectedItemIndex = screenState.selectedItemIndex,
            onItemClicked = { screenModel.eventHandler(OnItemClicked(id = it)) },
            onLongClick = { screenModel.eventHandler(OnLongPress(index = it)) },
            onDismissRequest = { screenModel.eventHandler(OnModalBottomSheetDismissRequest) },
            onChangeCategoryClick = {}
        )

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PlannedToWatchScreenContent(
    plannedAnimePaged: LazyPagingItems<UserRates>,
    modalBottomSheetState: SheetState,
    shouldShowModalBottomSheet: Boolean,
    selectedItemIndex: Int,
    onDismissRequest: () -> Unit,
    onItemClicked: (Int) -> Unit,
    onLongClick: (Int) -> Unit,
    onChangeCategoryClick: () -> Unit
) {
    TabAnimeList(
        userRatesPaged = plannedAnimePaged,
        status = UserRatesEnum.PLANNED.key,
        onItemClick = onItemClicked,
        isLoading = plannedAnimePaged.loadState.refresh == LoadState.Loading,
        onLongClick = onLongClick
    )

    if (shouldShowModalBottomSheet) {
        plannedAnimePaged[selectedItemIndex]?.let { userRate ->
            AnimeBottomSheet(
                sheetState = modalBottomSheetState,
                category = userRate.userStatus,
                name = userRate.anime.name,
                onChangeCategoryClick = onChangeCategoryClick,
                onDismissRequest = onDismissRequest
            )
        }
    }
}
