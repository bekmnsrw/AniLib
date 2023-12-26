package com.bekmnsrw.feature.home.impl.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.bekmnsrw.core.widget.AniLibCircularProgressBar
import com.bekmnsrw.core.widget.AniLibHorizontalList
import com.bekmnsrw.core.widget.AniLibTopBarWithSearch
import com.bekmnsrw.feature.home.impl.AnimeStatusEnum.ANONS
import com.bekmnsrw.feature.home.impl.AnimeStatusEnum.ONGOING
import com.bekmnsrw.feature.home.impl.AnimeStatusEnum.RELEASED
import com.bekmnsrw.feature.home.impl.R
import com.bekmnsrw.feature.home.impl.presentation.details.DetailsScreen
import com.bekmnsrw.feature.home.impl.presentation.home.HomeScreenModel.HomeScreenAction
import com.bekmnsrw.feature.home.impl.presentation.home.HomeScreenModel.HomeScreenAction.NavigateAnimeDetailsScreen
import com.bekmnsrw.feature.home.impl.presentation.home.HomeScreenModel.HomeScreenAction.NavigateMoreAnimeList
import com.bekmnsrw.feature.home.impl.presentation.home.HomeScreenModel.HomeScreenEvent
import com.bekmnsrw.feature.home.impl.presentation.home.HomeScreenModel.HomeScreenEvent.OnAnimeCardClicked
import com.bekmnsrw.feature.home.impl.presentation.home.HomeScreenModel.HomeScreenEvent.OnMoreClicked
import com.bekmnsrw.feature.home.impl.presentation.home.HomeScreenModel.HomeScreenState
import com.bekmnsrw.feature.home.impl.presentation.list.MoreAnimeListScreen

internal class HomeScreen : Screen {

    @Composable
    override fun Content() {
        val screenModel = getScreenModel<HomeScreenModel>()
        val screenState by screenModel.screenState.collectAsStateWithLifecycle()
        val screenAction by screenModel.screenAction.collectAsStateWithLifecycle(initialValue = null)

        HomeScreenContent(
            screenState = screenState,
            eventHandler = screenModel::eventHandler
        )

        HomeScreenActions(screenAction = screenAction)
    }
}

@Composable
private fun HomeScreenActions(
    screenAction: HomeScreenAction?
) {
    val navigator = LocalNavigator.currentOrThrow

    LaunchedEffect(screenAction) {
        when (screenAction) {
            null -> Unit

            is NavigateMoreAnimeList -> navigator.push(
                item = MoreAnimeListScreen(
                    status = screenAction.status
                )
            )

            is NavigateAnimeDetailsScreen -> navigator.push(
                item = DetailsScreen(
                    id = screenAction.id
                )
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeScreenContent(
    screenState: HomeScreenState,
    eventHandler: (HomeScreenEvent) -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(
        state = rememberTopAppBarState()
    )

    Scaffold(
        modifier = Modifier.nestedScroll(
            connection = scrollBehavior.nestedScrollConnection
        ),
        topBar = {
            AniLibTopBarWithSearch(
                scrollBehavior = scrollBehavior,
                title = "AniLib",
                onSearchIconClicked = { /* EventHandler invocation */ }
            )
        }
    ) { paddingValues ->
        if (screenState.isLoading) {
            AniLibCircularProgressBar(shouldShow = true)
        } else {
            AnimeList(
                paddingValues = paddingValues,
                screenState = screenState,
                eventHandler = eventHandler
            )
        }
    }
}

@Composable
private fun AnimeList(
    paddingValues: PaddingValues,
    screenState: HomeScreenState,
    eventHandler: (HomeScreenEvent) -> Unit
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(16.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(bottom = 72.dp)
    ) {
        item {
            AniLibHorizontalList(
                animeList = screenState.ongoingAnimeListBrief,
                animeListTitle = stringResource(id = R.string.ongoing),
                onMoreClicked = { eventHandler(OnMoreClicked(status = ONGOING.status)) },
                onItemClicked = { eventHandler(OnAnimeCardClicked(id = it)) }
            )
        }

        item {
            AniLibHorizontalList(
                animeList = screenState.anonsAnimeListBrief,
                animeListTitle = stringResource(id = R.string.anons),
                onMoreClicked = { eventHandler(OnMoreClicked(status = ANONS.status)) },
                onItemClicked = { eventHandler(OnAnimeCardClicked(id = it)) }
            )
        }

        item {
            AniLibHorizontalList(
                animeList = screenState.releasedAnimeListBrief,
                animeListTitle = stringResource(id = R.string.released),
                onMoreClicked = { eventHandler(OnMoreClicked(status = RELEASED.status)) },
                onItemClicked = { eventHandler(OnAnimeCardClicked(id = it)) }
            )
        }
    }
}
