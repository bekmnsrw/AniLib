package com.bekmnsrw.feature.favorites.impl.presentation.completed

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
import com.bekmnsrw.core.widget.AniLibDialog
import com.bekmnsrw.core.widget.AniLibSnackbar
import com.bekmnsrw.core.widget.UserRatesEnum
import com.bekmnsrw.feature.favorites.api.model.UserRates
import com.bekmnsrw.feature.favorites.impl.presentation.completed.CompletedScreenModel.CompletedScreenAction
import com.bekmnsrw.feature.favorites.impl.presentation.completed.CompletedScreenModel.CompletedScreenAction.NavigateDetails
import com.bekmnsrw.feature.favorites.impl.presentation.completed.CompletedScreenModel.CompletedScreenAction.ShowSnackbar
import com.bekmnsrw.feature.favorites.impl.presentation.completed.CompletedScreenModel.CompletedScreenEvent.OnBottomSheetDismissRequest
import com.bekmnsrw.feature.favorites.impl.presentation.completed.CompletedScreenModel.CompletedScreenEvent.OnChangeCategoryClick
import com.bekmnsrw.feature.favorites.impl.presentation.completed.CompletedScreenModel.CompletedScreenEvent.OnDialogDismissRequest
import com.bekmnsrw.feature.favorites.impl.presentation.completed.CompletedScreenModel.CompletedScreenEvent.OnItemClick
import com.bekmnsrw.feature.favorites.impl.presentation.completed.CompletedScreenModel.CompletedScreenEvent.OnLongPress
import com.bekmnsrw.feature.favorites.impl.presentation.completed.CompletedScreenModel.CompletedScreenEvent.OnRadioButtonClick
import com.bekmnsrw.feature.favorites.impl.presentation.container.AnimeBottomSheet
import com.bekmnsrw.feature.favorites.impl.presentation.container.TabAnimeList
import kotlinx.coroutines.launch

internal class CompletedScreen : Screen {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val screenModel = getScreenModel<CompletedScreenModel>()
        val screenState by screenModel.screenState.collectAsStateWithLifecycle()
        val screenAction by screenModel.screenAction.collectAsStateWithLifecycle(initialValue = null)
        val completedAnimePaged = screenModel.completed.collectAsLazyPagingItems()

        val modalBottomSheetState = rememberModalBottomSheetState()
        val snackbarHostState = remember { SnackbarHostState() }

        with(screenModel) {
            CompletedScreenContent(
                completedAnimePaged = completedAnimePaged,
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

        CompletedScreenActions(
            screenAction = screenAction,
            snackbarHostState = snackbarHostState
        )
    }
}

@Composable
private fun CompletedScreenActions(
    screenAction: CompletedScreenAction?,
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
private fun CompletedScreenContent(
    completedAnimePaged: LazyPagingItems<UserRates>,
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
        userRatesPaged = completedAnimePaged,
        status = UserRatesEnum.COMPLETED.key,
        isLoading = completedAnimePaged.loadState.refresh == LoadState.Loading,
        onItemClick = onItemClick,
        onLongClick = onLongClick
    )

    if (shouldShowModalBottomSheet) {
        completedAnimePaged[selectedItemIndex]?.let { userRate ->
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
        completedAnimePaged[selectedItemIndex]?.let { userRate ->
            AniLibDialog(
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
