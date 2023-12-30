package com.bekmnsrw.feature.home.impl.presentation.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import cafe.adriel.voyager.core.registry.ScreenRegistry
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.bekmnsrw.core.designsystem.icon.AniLibIcons
import com.bekmnsrw.core.designsystem.theme.AniLibTypography
import com.bekmnsrw.core.navigation.SharedScreen
import com.bekmnsrw.core.widget.AniLibIconButton
import com.bekmnsrw.core.widget.AniLibImage
import com.bekmnsrw.feature.home.api.model.Anime
import com.bekmnsrw.feature.home.api.model.SearchRequest
import com.bekmnsrw.feature.home.impl.HomeConstants
import com.bekmnsrw.feature.home.impl.R
import com.bekmnsrw.feature.home.impl.presentation.search.SearchScreenModel.SearchScreenAction
import com.bekmnsrw.feature.home.impl.presentation.search.SearchScreenModel.SearchScreenAction.NavigateAnimeDetailsScreen
import com.bekmnsrw.feature.home.impl.presentation.search.SearchScreenModel.SearchScreenAction.NavigateBack
import com.bekmnsrw.feature.home.impl.presentation.search.SearchScreenModel.SearchScreenEvent
import com.bekmnsrw.feature.home.impl.presentation.search.SearchScreenModel.SearchScreenEvent.OnActiveChange
import com.bekmnsrw.feature.home.impl.presentation.search.SearchScreenModel.SearchScreenEvent.OnAnimeClick
import com.bekmnsrw.feature.home.impl.presentation.search.SearchScreenModel.SearchScreenEvent.OnArrowBackClick
import com.bekmnsrw.feature.home.impl.presentation.search.SearchScreenModel.SearchScreenEvent.OnClearQueryClick
import com.bekmnsrw.feature.home.impl.presentation.search.SearchScreenModel.SearchScreenEvent.OnImeActionSearchClick
import com.bekmnsrw.feature.home.impl.presentation.search.SearchScreenModel.SearchScreenEvent.OnQueryChange
import com.bekmnsrw.feature.home.impl.presentation.search.SearchScreenModel.SearchScreenEvent.OnSearchHistoryItemClick
import kotlinx.collections.immutable.PersistentList
import org.koin.androidx.compose.getKoin

internal data class SearchScreen(val status: String) : Screen {

    @Composable
    override fun Content() {
        getKoin().setProperty(HomeConstants.STATUS_KOIN_PROPERTY, status)

        val screenModel = getScreenModel<SearchScreenModel>()
        val screenState by screenModel.screenState.collectAsStateWithLifecycle()
        val screenAction by screenModel.screenAction.collectAsStateWithLifecycle(initialValue = null)
        val searchResult = screenModel.searchResult.collectAsLazyPagingItems()
        val searchInput by screenModel.searchInput.collectAsStateWithLifecycle()

        SearchScreenContent(
            query = searchInput,
            isActive = screenState.shouldShowSearch,
            eventHandler = screenModel::eventHandler,
            searchResult = searchResult,
            searchHistory = screenState.searchHistory
        )

        SearchScreenActions(searchScreenAction = screenAction,)
    }
}

@Composable
private fun SearchScreenActions(searchScreenAction: SearchScreenAction?, ) {
    val navigator = LocalNavigator.currentOrThrow

    LaunchedEffect(searchScreenAction) {
        when (searchScreenAction) {
            null -> Unit

            is NavigateAnimeDetailsScreen -> {
                val detailsScreen = ScreenRegistry.get(
                    provider = SharedScreen.DetailsScreen(id = searchScreenAction.id)
                )
                navigator.push(item = detailsScreen)
            }

            NavigateBack -> navigator.pop()
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun SearchScreenContent(
    query: String,
    isActive: Boolean,
    eventHandler: (SearchScreenEvent) -> Unit,
    searchResult: LazyPagingItems<Anime>,
    searchHistory: PersistentList<SearchRequest>
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    Scaffold { contentPadding ->
        AnimeSearchBar(
            contentPadding = contentPadding,
            query = query,
            isActive = isActive,
            onQueryChange = { eventHandler(OnQueryChange(query = it)) },
            onSearch = {
                keyboardController?.hide()
                focusManager.clearFocus(true)
                eventHandler(OnImeActionSearchClick)
            },
            onActiveChange = { eventHandler(OnActiveChange(isActive = it)) },
            onArrowBackClick = { eventHandler(OnArrowBackClick) },
            onClearQueryClick = { eventHandler(OnClearQueryClick) },
            searchResult = searchResult,
            onSearchResultItemClick = { eventHandler(OnAnimeClick(id = it)) },
            searchHistory = searchHistory,
            onSearchHistoryItemClick = { eventHandler(OnSearchHistoryItemClick(id = it)) }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AnimeSearchBar(
    contentPadding: PaddingValues,
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    isActive: Boolean,
    onActiveChange: (Boolean) -> Unit,
    onArrowBackClick: () -> Unit,
    onClearQueryClick: () -> Unit,
    searchResult: LazyPagingItems<Anime>,
    onSearchResultItemClick: (Int) -> Unit,
    searchHistory: PersistentList<SearchRequest>,
    onSearchHistoryItemClick: (Int) -> Unit
) {
    SearchBar(
        modifier = Modifier.fillMaxWidth(),
        query = query,
        onQueryChange = onQueryChange,
        onSearch = onSearch,
        active = isActive,
        onActiveChange = onActiveChange,
        placeholder = { Text(text = stringResource(id = R.string.search_anime)) },
        leadingIcon = {
            AniLibIconButton(
                onClick = {
                    if (isActive) {
                        onActiveChange(false)
                        onClearQueryClick()
                    } else {
                     onArrowBackClick()
                    }
                },
                imageVector = AniLibIcons.ArrowBack
            )
        },
        trailingIcon = {
            if (isActive && query.isNotEmpty()) {
                AniLibIconButton(
                    onClick = onClearQueryClick,
                    imageVector = AniLibIcons.Close
                )
            }
        }
    ) {
        SearchBarContent(
            contentPadding = contentPadding,
            searchResult = searchResult,
            onSearchResultItemClick = onSearchResultItemClick,
            searchHistory = searchHistory,
            onSearchHistoryItemClick = onSearchHistoryItemClick
        )
    }
}

@Composable
private fun SearchBarContent(
    contentPadding: PaddingValues,
    searchResult: LazyPagingItems<Anime>,
    onSearchResultItemClick: (Int) -> Unit,
    searchHistory: PersistentList<SearchRequest>,
    onSearchHistoryItemClick: (Int) -> Unit
) {
    if (searchResult.itemCount == 0) {
        SearchHistory(
            searchHistory = searchHistory,
            onSearchHistoryItemClick = onSearchHistoryItemClick
        )
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
                .padding(bottom = 56.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(16.dp)
        ) {
            items(
                count = searchResult.itemCount,
                key = searchResult.itemKey { it.id },
                contentType = searchResult.itemContentType { "SearchResult" }
            ) { index ->
                searchResult[index]?.let { result ->
                    SearchResultItem(
                        anime = result,
                        onClick = onSearchResultItemClick
                    )
                }
            }
        }
    }
}

@Composable
private fun SearchResultItem(
    anime: Anime,
    onClick: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(anime.id) },
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        AniLibImage(
            imageUrl = anime.image.original,
            modifier = Modifier
                .height(120.dp)
                .width(80.dp)
        )
        Column {
            Text(
                text = anime.name,
                style = AniLibTypography.titleMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = anime.russian,
                style = AniLibTypography.bodyMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun SearchHistory(
    searchHistory: PersistentList<SearchRequest>,
    onSearchHistoryItemClick: (Int) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 56.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(
            items = searchHistory,
            key = { it.id },
            contentType = { "SearchHistory" }
        ) {
            SearchHistoryItem(
                searchRequest = it,
                onClick = onSearchHistoryItemClick
            )
        }
    }
}

@Composable
private fun SearchHistoryItem(
    searchRequest: SearchRequest,
    onClick: (Int) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(searchRequest.id) }
    ) {
        Icon(
            imageVector = AniLibIcons.History,
            contentDescription = null
        )
        Text(text = searchRequest.query)
    }
}
