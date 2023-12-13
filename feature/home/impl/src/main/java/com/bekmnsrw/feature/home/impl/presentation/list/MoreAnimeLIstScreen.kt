package com.bekmnsrw.feature.home.impl.presentation.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.bekmnsrw.core.designsystem.icon.AniLibIcons
import com.bekmnsrw.core.designsystem.theme.AniLibTypography
import com.bekmnsrw.core.widget.AniLibCircularProgressBar
import com.bekmnsrw.core.widget.AniLibDropDownMenu
import com.bekmnsrw.core.widget.AniLibImage
import com.bekmnsrw.core.widget.topbar.AniLibTopBarWithNavIconFilterAndSearch
import com.bekmnsrw.feature.home.api.model.list.Anime
import com.bekmnsrw.feature.home.impl.presentation.details.DetailsScreen
import com.bekmnsrw.feature.home.impl.presentation.list.MoreAnimeListScreenModel.MoreAnimeListScreenAction
import com.bekmnsrw.feature.home.impl.presentation.list.MoreAnimeListScreenModel.MoreAnimeListScreenEvent
import com.bekmnsrw.feature.home.impl.presentation.list.MoreAnimeListScreenModel.MoreAnimeListScreenState
import org.koin.androidx.compose.getKoin

internal data class MoreAnimeListScreen(val status: String) : Screen {

    @Composable
    override fun Content() {
        getKoin().setProperty("status", status)
        getKoin().setProperty("order", "ranked")

        val navigator = LocalNavigator.currentOrThrow
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
            navigator = navigator
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
                onNavigationIconClicked = {
                    eventHandler(
                        MoreAnimeListScreenEvent.OnArrowBackClicked
                    )
                },
                onSearchIconClicked = { /* TODO: EventHandler invocation */ },
                onFilterClicked = {
                    eventHandler(
                        MoreAnimeListScreenEvent.OnFilterClicked
                    )
                }
            )
        },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { contentPadding ->
        AniLibDropDownMenu(
            offset = DpOffset(x = 16.dp, y = 64.dp),
            isDropDownMenuExpanded = screenState.isDropDownMenuExpanded,
            items = MoreAnimeListScreenModel.dropDownMenuItems,
            selectedItem = screenState.filteredBy,
            onDismissRequest = {
                eventHandler(
                    MoreAnimeListScreenEvent.OnDropDownMenuDismissRequest
                )
            },
            onDropDownMenuItemClicked = {
                eventHandler(
                    MoreAnimeListScreenEvent.OnDropDownMenuItemClicked(
                        order = it
                    )
                )
            }
        )

        AnimeList(
            contentPadding = contentPadding,
            animePaged = animePaged,
            eventHandler = eventHandler
        )

        if (animePaged.loadState.refresh == LoadState.Loading) {
            AniLibCircularProgressBar(shouldShow = true)
        }
    }
}

@Composable
private fun AnimeList(
    contentPadding: PaddingValues,
    animePaged: LazyPagingItems<Anime>,
    eventHandler: (MoreAnimeListScreenEvent) -> Unit
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(16.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding)
    ) {
        items(
            count = animePaged.itemCount,
            key = animePaged.itemKey { it.id },
            contentType = animePaged.itemContentType { "AnimePaged" }
        ) { index ->
            animePaged[index]?.let { animeItem ->
                AnimeListItem(anime = animeItem) {
                    eventHandler(
                        MoreAnimeListScreenEvent.OnAnimeCardClicked(
                            id = animeItem.id
                        )
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AnimeListItem(
    anime: Anime,
    onCardClicked: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        shape = RoundedCornerShape(8.dp),
        onClick = { onCardClicked() }
    ) {
        Row {
            AniLibImage(
                imageUrl = anime.image.original,
                alpha = 1.0f,
                modifier = Modifier
                    .width(120.dp)
                    .height(160.dp)
                    .clip(
                        RoundedCornerShape(
                            topStart = 8.dp,
                            bottomStart = 8.dp
                        )
                    )
            )
            AnimeListItemDescription(
                name = anime.name,
                numberOfEpisodes = anime.numberOfEpisodes,
                episodesAired = anime.episodesAired,
                score = anime.score,
                kind = anime.kind,
                airedOn = anime.airedOn,
                status = anime.status,
                releasedOn = anime.releasedOn
            )
        }
    }
}

@Composable
private fun AnimeListItemDescription(
    name: String,
    numberOfEpisodes: String,
    episodesAired: Int,
    score: String,
    kind: String,
    airedOn: String,
    status: String,
    releasedOn: String
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        DescriptionItem(
            value = name,
            style = AniLibTypography.titleMedium
        )
        if (status != "anons") {
            DescriptionItem(
                key = "Rating: ",
                value = score
            )
        }
        DescriptionItem(
            key = "Kind: ",
            value = kind
        )
        DescriptionItem(
            key = "Status: ",
            value = status
        )
        if (status != "anons") {
            DescriptionItem(
                key = "Aired on: ",
                value = airedOn
            )
        }
        if (status == "released" && releasedOn.isNotEmpty()) {
            DescriptionItem(
                key = "Released on: ",
                value = releasedOn
            )
        }
        if (kind != "movie") {
            DescriptionItem(
                key = "Aired: ",
                value = when (status) {
                    "anons" -> "0/? ep"
                    else -> "$episodesAired/$numberOfEpisodes ep"
                }
            )
        }
    }
}

@Composable
private fun DescriptionItem(
    key: String = "",
    value: String,
    style: TextStyle = AniLibTypography.bodyMedium
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (key.isNotEmpty()) {
            Text(
                text = key,
                style = AniLibTypography.titleSmall
            )
        }
        Text(
            text = value,
            style = style,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
        )
    }
}

@Composable
private fun MoreAnimeListScreenAction(
    screenAction: MoreAnimeListScreenAction?,
    navigator: Navigator
) {
    LaunchedEffect(screenAction) {
        when (screenAction) {
            null -> Unit

            MoreAnimeListScreenAction.NavigateHomeScreen -> navigator.pop()

            is MoreAnimeListScreenAction.NavigateDetailsScreen -> navigator.push(
                item = DetailsScreen(
                    id = screenAction.id
                )
            )
        }
    }
}
