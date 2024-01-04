package com.bekmnsrw.feature.favorites.impl.presentation.onhold

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
import com.bekmnsrw.feature.favorites.impl.presentation.onhold.OnHoldScreenModel.OnHoldScreenAction
import com.bekmnsrw.feature.favorites.impl.presentation.onhold.OnHoldScreenModel.OnHoldScreenEvent.OnBottomSheetDismissRequest
import com.bekmnsrw.feature.favorites.impl.presentation.onhold.OnHoldScreenModel.OnHoldScreenEvent.OnChangeCategoryClick
import com.bekmnsrw.feature.favorites.impl.presentation.onhold.OnHoldScreenModel.OnHoldScreenEvent.OnDialogDismissRequest
import com.bekmnsrw.feature.favorites.impl.presentation.onhold.OnHoldScreenModel.OnHoldScreenEvent.OnItemClick
import com.bekmnsrw.feature.favorites.impl.presentation.onhold.OnHoldScreenModel.OnHoldScreenEvent.OnLongPress
import com.bekmnsrw.feature.favorites.impl.presentation.onhold.OnHoldScreenModel.OnHoldScreenEvent.OnRadioButtonClick
import kotlinx.coroutines.launch

internal class OnHoldScreen : Screen {

    @OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
    @Composable
    override fun Content() {
        val screenModel = getScreenModel<OnHoldScreenModel>()
        val screenState by screenModel.screenState.collectAsStateWithLifecycle()
        val screenAction by screenModel.screenAction.collectAsStateWithLifecycle(initialValue = null)
        val onHoldAnimePaged = screenModel.onHold.collectAsLazyPagingItems()

        val pullRefreshState = rememberPullRefreshState(
            refreshing = onHoldAnimePaged.loadState.refresh == LoadState.Loading,
            onRefresh = { onHoldAnimePaged.refresh() }
        )

        val modalBottomSheetState = rememberModalBottomSheetState()
        val snackbarHostState = remember { SnackbarHostState() }

        with(screenModel) {
            OnHoldScreenContent(
                pullRefreshState = pullRefreshState,
                onHoldAnimePaged = onHoldAnimePaged,
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

        OnHoldScreenActions(
            screenAction = screenAction,
            snackbarHostState = snackbarHostState
        )
    }
}

@Composable
private fun OnHoldScreenActions(
    screenAction: OnHoldScreenAction?,
    snackbarHostState: SnackbarHostState
) {
    val navigator = LocalNavigator.currentOrThrow
    val coroutineScope = rememberCoroutineScope()

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

            is OnHoldScreenAction.ShowSnackbar -> coroutineScope.launch {
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
private fun OnHoldScreenContent(
    pullRefreshState: PullRefreshState,
    onHoldAnimePaged: LazyPagingItems<UserRates>,
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
        userRatesPaged = onHoldAnimePaged,
        status = UserRatesEnum.PLANNED.key,
        onItemClick = onItemClick,
        onLongClick = onLongClick,
        refreshing = onHoldAnimePaged.loadState.refresh == LoadState.Loading
    )

    if (shouldShowModalBottomSheet) {
        onHoldAnimePaged[selectedItemIndex]?.let { userRate ->
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
        onHoldAnimePaged[selectedItemIndex]?.let { userRate ->
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
