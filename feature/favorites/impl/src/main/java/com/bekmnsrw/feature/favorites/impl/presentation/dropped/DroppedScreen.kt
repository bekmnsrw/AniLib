package com.bekmnsrw.feature.favorites.impl.presentation.dropped

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.bekmnsrw.core.widget.dialog.AniLibAnimeStatusDialog
import com.bekmnsrw.core.widget.AniLibSnackbar
import com.bekmnsrw.core.widget.UserRatesEnum
import com.bekmnsrw.feature.favorites.api.model.UserRates
import com.bekmnsrw.feature.favorites.impl.presentation.container.AnimeBottomSheet
import com.bekmnsrw.feature.favorites.impl.presentation.container.TabAnimeList
import com.bekmnsrw.feature.favorites.impl.presentation.dropped.DroppedScreenModel.DroppedScreenAction
import com.bekmnsrw.feature.favorites.impl.presentation.dropped.DroppedScreenModel.DroppedScreenAction.NavigateDetails
import com.bekmnsrw.feature.favorites.impl.presentation.dropped.DroppedScreenModel.DroppedScreenAction.ShowSnackbar
import com.bekmnsrw.feature.favorites.impl.presentation.dropped.DroppedScreenModel.DroppedScreenEvent.OnBottomSheetDismissRequest
import com.bekmnsrw.feature.favorites.impl.presentation.dropped.DroppedScreenModel.DroppedScreenEvent.OnChangeCategoryClick
import com.bekmnsrw.feature.favorites.impl.presentation.dropped.DroppedScreenModel.DroppedScreenEvent.OnDialogDismissRequest
import com.bekmnsrw.feature.favorites.impl.presentation.dropped.DroppedScreenModel.DroppedScreenEvent.OnItemClick
import com.bekmnsrw.feature.favorites.impl.presentation.dropped.DroppedScreenModel.DroppedScreenEvent.OnLongPress
import com.bekmnsrw.feature.favorites.impl.presentation.dropped.DroppedScreenModel.DroppedScreenEvent.OnRadioButtonClick
import kotlinx.coroutines.launch

internal class DroppedScreen : Screen {

    @OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
    @Composable
    override fun Content() {
        val screenModel = getScreenModel<DroppedScreenModel>()
        val screenState by screenModel.screenState.collectAsStateWithLifecycle()
        val screenAction by screenModel.screenAction.collectAsStateWithLifecycle(initialValue = null)
        val droppedAnimePaged = screenModel.dropped.collectAsLazyPagingItems()

        val pullRefreshState = rememberPullRefreshState(
            refreshing = droppedAnimePaged.loadState.refresh == LoadState.Loading,
            onRefresh = { droppedAnimePaged.refresh() }
        )

        val modalBottomSheetState = rememberModalBottomSheetState()
        val snackbarHostState = remember { SnackbarHostState() }

        with(screenModel) {
            DroppedScreenContent(
                pullRefreshState = pullRefreshState,
                droppedAnimePaged = droppedAnimePaged,
                snackbarHostState = snackbarHostState,
                modalBottomSheetState = modalBottomSheetState,
                shouldShowModalBottomSheet = screenState.shouldShowBottomSheet,
                shouldShowDialog = screenState.shouldShowDialog,
                selectedItemIndex = screenState.selectedItemIndex,
                onItemClick = { eventHandler(OnItemClick(id = it)) },
                onLongClick = { eventHandler(OnLongPress(index = it)) },
                onDialogDismissRequest = { eventHandler(OnDialogDismissRequest) },
                onBottomSheetDismissRequest = { eventHandler(OnBottomSheetDismissRequest) },
                onChangeCategoryClick = { eventHandler(OnChangeCategoryClick) },
                onRadioButtonClick = { status, id ->
                    eventHandler(OnRadioButtonClick(status = status, id = id))
                }
            )
        }

        DroppedScreenActions(
            screenAction = screenAction,
            snackbarHostState = snackbarHostState
        )
    }
}

@Composable
private fun DroppedScreenActions(
    screenAction: DroppedScreenAction?,
    snackbarHostState: SnackbarHostState
) {
    val navigator = LocalNavigator.currentOrThrow
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(screenAction) {
        when (screenAction) {
            null -> Unit

            is NavigateDetails -> {
                val detailsScreen = ScreenRegistry.get(
                    provider = SharedScreen.DetailsScreen(
                        id = screenAction.id
                    )
                )
                navigator.push(item = detailsScreen)
            }

            is ShowSnackbar -> coroutineScope.launch {
                snackbarHostState.showSnackbar(
                    message = screenAction.message,
                    duration = SnackbarDuration.Short
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
private fun DroppedScreenContent(
    pullRefreshState: PullRefreshState,
    droppedAnimePaged: LazyPagingItems<UserRates>,
    snackbarHostState: SnackbarHostState,
    modalBottomSheetState: SheetState,
    shouldShowModalBottomSheet: Boolean,
    shouldShowDialog: Boolean,
    selectedItemIndex: Int,
    onBottomSheetDismissRequest: () -> Unit,
    onDialogDismissRequest: () -> Unit,
    onItemClick: (Int) -> Unit,
    onLongClick: (Int) -> Unit,
    onChangeCategoryClick: () -> Unit,
    onRadioButtonClick: (String, Int?) -> Unit
) {
    TabAnimeList(
        pullRefreshState = pullRefreshState,
        userRatesPaged = droppedAnimePaged,
        status = UserRatesEnum.DROPPED.key,
        onItemClick = onItemClick,
        onLongClick = onLongClick,
        refreshing = droppedAnimePaged.loadState.refresh == LoadState.Loading
    )

    if (shouldShowModalBottomSheet) {
        droppedAnimePaged[selectedItemIndex]?.let { userRate ->
            AnimeBottomSheet(
                sheetState = modalBottomSheetState,
                category = userRate.userStatus,
                name = userRate.anime.name,
                onChangeCategoryClick = onChangeCategoryClick,
                onDismissRequest = onBottomSheetDismissRequest
            )
        }
    }

    if (shouldShowDialog) {
        droppedAnimePaged[selectedItemIndex]?.let { userRate ->
            AniLibAnimeStatusDialog(
                id = userRate.id,
                currentStatus = userRate.userStatus,
                onDismissRequest = onDialogDismissRequest,
                onRadioButtonClick = onRadioButtonClick
            )
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        AniLibSnackbar(
            modifier = Modifier.align(Alignment.BottomCenter),
            snackbarHostState = snackbarHostState
        )
    }
}
