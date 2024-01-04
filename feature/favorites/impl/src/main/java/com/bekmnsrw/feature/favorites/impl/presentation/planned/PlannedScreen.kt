package com.bekmnsrw.feature.favorites.impl.presentation.planned

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
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
import com.bekmnsrw.feature.favorites.impl.presentation.planned.PlannedScreenModel.PlannedScreenAction
import com.bekmnsrw.feature.favorites.impl.presentation.planned.PlannedScreenModel.PlannedScreenAction.NavigateDetails
import com.bekmnsrw.feature.favorites.impl.presentation.planned.PlannedScreenModel.PlannedScreenAction.ShowSnackbar
import com.bekmnsrw.feature.favorites.impl.presentation.planned.PlannedScreenModel.PlannedScreenEvent.OnBottomSheetDismissRequest
import com.bekmnsrw.feature.favorites.impl.presentation.planned.PlannedScreenModel.PlannedScreenEvent.OnChangeCategoryClick
import com.bekmnsrw.feature.favorites.impl.presentation.planned.PlannedScreenModel.PlannedScreenEvent.OnDialogDismissRequest
import com.bekmnsrw.feature.favorites.impl.presentation.planned.PlannedScreenModel.PlannedScreenEvent.OnItemClick
import com.bekmnsrw.feature.favorites.impl.presentation.planned.PlannedScreenModel.PlannedScreenEvent.OnLongPress
import com.bekmnsrw.feature.favorites.impl.presentation.planned.PlannedScreenModel.PlannedScreenEvent.OnRadioButtonClick
import kotlinx.coroutines.launch

internal class PlannedScreen : Screen {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val screenModel = getScreenModel<PlannedScreenModel>()
        val screenState by screenModel.screenState.collectAsStateWithLifecycle()
        val screenAction by screenModel.screenAction.collectAsStateWithLifecycle(initialValue = null)
        val plannedAnimePaged = screenModel.planned.collectAsLazyPagingItems()

        val modalBottomSheetState = rememberModalBottomSheetState()
        val snackbarHostState = remember { SnackbarHostState() }

        with(screenModel) {
            PlannedToWatchScreenContent(
                plannedAnimePaged = plannedAnimePaged,
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

        PlannedScreenActions(
            screenAction = screenAction,
            snackbarHostState = snackbarHostState
        )
    }
}

@Composable
private fun PlannedScreenActions(
    screenAction: PlannedScreenAction?,
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PlannedToWatchScreenContent(
    plannedAnimePaged: LazyPagingItems<UserRates>,
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
        userRatesPaged = plannedAnimePaged,
        status = UserRatesEnum.PLANNED.key,
        onItemClick = onItemClick,
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
                onDismissRequest = onBottomSheetDismissRequest
            )
        }
    }

    if (shouldShowDialog) {
        plannedAnimePaged[selectedItemIndex]?.let { userRate ->
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
