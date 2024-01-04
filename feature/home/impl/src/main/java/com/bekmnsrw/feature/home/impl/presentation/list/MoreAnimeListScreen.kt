package com.bekmnsrw.feature.home.impl.presentation.list

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.bekmnsrw.core.widget.indicator.AniLibCircularProgressBar
import com.bekmnsrw.core.widget.AniLibDropDownMenu
import com.bekmnsrw.core.widget.topbar.AniLibTopBarWithNavIconFilterAndSearch
import com.bekmnsrw.core.widget.list.AniLibVerticalList
import com.bekmnsrw.feature.home.api.model.Anime
import com.bekmnsrw.feature.home.impl.HomeConstants.STATUS_KOIN_PROPERTY
import com.bekmnsrw.feature.home.impl.presentation.details.DetailsScreen
import com.bekmnsrw.feature.home.impl.presentation.list.MoreAnimeListScreenModel.MoreAnimeListScreenAction
import com.bekmnsrw.feature.home.impl.presentation.list.MoreAnimeListScreenModel.MoreAnimeListScreenAction.*
import com.bekmnsrw.feature.home.impl.presentation.list.MoreAnimeListScreenModel.MoreAnimeListScreenEvent
import com.bekmnsrw.feature.home.impl.presentation.list.MoreAnimeListScreenModel.MoreAnimeListScreenEvent.*
import com.bekmnsrw.feature.home.impl.presentation.list.MoreAnimeListScreenModel.MoreAnimeListScreenState
import com.bekmnsrw.feature.home.impl.presentation.search.SearchScreen
import org.koin.androidx.compose.getKoin

internal data class MoreAnimeListScreen(val status: String) : Screen {

    @Composable
    override fun Content() {
        getKoin().setProperty(STATUS_KOIN_PROPERTY, status)

        val screenModel = getScreenModel<MoreAnimeListScreenModel>()
        val animePaged = screenModel.animePaged.collectAsLazyPagingItems()
        val screenState by screenModel.screenState.collectAsStateWithLifecycle()
        val screenAction by screenModel.screenAction.collectAsStateWithLifecycle(initialValue = null)

        MoreAnimeListScreenContent(
            screenState = screenState,
            status = status,
            animePaged = animePaged,
            eventHandler = screenModel::eventHandler
        )

        MoreAnimeListScreenAction(
            screenAction = screenAction,
            status = status
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MoreAnimeListScreenContent(
    screenState: MoreAnimeListScreenState,
    status: String,
    animePaged: LazyPagingItems<Anime>,
    eventHandler: (MoreAnimeListScreenEvent) -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        topBar = {
            AniLibTopBarWithNavIconFilterAndSearch(
                scrollBehavior = scrollBehavior,
                title = status.replaceFirstChar { it.uppercase() },
                onNavigationIconClicked = { eventHandler(OnArrowBackClick) },
                onSearchIconClicked = { eventHandler(OnSearchIconClick(status = status)) },
                onFilterClicked = { eventHandler(OnFilterClick) }
            )
        },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { contentPadding ->
        AniLibDropDownMenu(
            offset = DpOffset(x = 16.dp, y = 64.dp),
            isDropDownMenuExpanded = screenState.isDropDownMenuExpanded,
            items = MoreAnimeListScreenModel.dropDownMenuItems,
            selectedItem = screenState.filteredBy,
            onDismissRequest = { eventHandler(OnDropDownMenuDismissRequest) },
            onDropDownMenuItemClicked = {
                eventHandler(
                    OnDropDownMenuItemClick(
                        order = it
                    )
                )
            }
        )

        if (animePaged.loadState.refresh == LoadState.Loading) {
            AniLibCircularProgressBar(shouldShow = true)
        } else {
            AniLibVerticalList(
                contentPadding = contentPadding,
                animePaged = animePaged,
                onItemClicked = { eventHandler(OnAnimeCardClick(id = it)) }
            )
        }
    }
}

@Composable
private fun MoreAnimeListScreenAction(
    screenAction: MoreAnimeListScreenAction?,
    status: String
) {
    val navigator = LocalNavigator.currentOrThrow

    LaunchedEffect(screenAction) {
        when (screenAction) {
            null -> Unit

            NavigateHomeScreen -> navigator.pop()

            is NavigateDetailsScreen -> navigator.push(
                item = DetailsScreen(
                    id = screenAction.id
                )
            )

            is NavigateSearchScreen -> navigator.push(
                SearchScreen(
                    status = status
                )
            )
        }
    }
}
