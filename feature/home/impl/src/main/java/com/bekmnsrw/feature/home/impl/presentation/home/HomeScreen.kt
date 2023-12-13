package com.bekmnsrw.feature.home.impl.presentation.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowForwardIos
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.offset
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.bekmnsrw.core.designsystem.theme.AniLibTypography
import com.bekmnsrw.core.widget.AniLibCircularProgressBar
import com.bekmnsrw.core.widget.AniLibImage
import com.bekmnsrw.core.widget.topbar.AniLibTopBarWithSearch
import com.bekmnsrw.feature.home.api.model.list.Anime
import com.bekmnsrw.feature.home.impl.presentation.details.DetailsScreen
import com.bekmnsrw.feature.home.impl.presentation.home.HomeScreenModel.HomeScreenAction
import com.bekmnsrw.feature.home.impl.presentation.home.HomeScreenModel.HomeScreenAction.*
import com.bekmnsrw.feature.home.impl.presentation.home.HomeScreenModel.HomeScreenEvent
import com.bekmnsrw.feature.home.impl.presentation.home.HomeScreenModel.HomeScreenEvent.*
import com.bekmnsrw.feature.home.impl.presentation.home.HomeScreenModel.HomeScreenState
import com.bekmnsrw.feature.home.impl.presentation.list.MoreAnimeListScreen
import kotlinx.collections.immutable.PersistentList

internal class HomeScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val screenModel = getScreenModel<HomeScreenModel>()
        val screenState by screenModel.screenState.collectAsStateWithLifecycle()
        val screenAction by screenModel.screenAction.collectAsStateWithLifecycle(initialValue = null)

        HomeScreenContent(
            screenState = screenState,
            eventHandler = screenModel::eventHandler
        )

        HomeScreenActions(
            screenAction = screenAction,
            navigator = navigator
        )
    }
}

@Composable
private fun HomeScreenActions(
    screenAction: HomeScreenAction?,
    navigator: Navigator
) {
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
                onSearchIconClicked = { /* TODO: EventHandler invocation */ }
            )
        }
    ) { paddingValues ->
        AnimeList(
            paddingValues = paddingValues,
            screenState = screenState,
            eventHandler = eventHandler
        )

        AniLibCircularProgressBar(shouldShow = screenState.isLoading)
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
            .padding(bottom = 56.dp)
    ) {
        item {
            AnimeList(
                animeList = screenState.ongoingAnimeList,
                animeListTitle = "Ongoing Anime",
                animeStatus = "ongoing",
                eventHandler = eventHandler
            )
        }

        item {
            AnimeList(
                animeList = screenState.anonsAnimeList,
                animeListTitle = "Anons Anime",
                animeStatus = "anons",
                eventHandler = eventHandler
            )
        }

        item {
            AnimeList(
                animeList = screenState.releasedAnimeList,
                animeListTitle = "Released Anime",
                animeStatus = "released",
                eventHandler = eventHandler
            )
        }
    }
}

@Composable
private fun AnimeList(
    animeList: PersistentList<Anime>,
    animeListTitle: String,
    animeStatus: String,
    eventHandler: (HomeScreenEvent) -> Unit
) {
    val density = LocalDensity.current
    val offsetPx = with(density) { 16.dp.roundToPx() }

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(
            bottom = if (animeStatus != "released") 16.dp else 0.dp
        )
    ) {
        AnimeListHeader(
            title = animeListTitle,
            onMoreClicked = {
                eventHandler(
                    OnMoreClicked(status = animeStatus)
                )
            }
        )
        LazyRow(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(horizontal = 16.dp),
            modifier = Modifier
                .layout { measurable, constraints ->
                    val looseConstraints = constraints.offset(offsetPx * 2, 0)
                    val placeable = measurable.measure(looseConstraints)
                    layout(placeable.width, placeable.height) { placeable.placeRelative(0, 0) }
                }
                .fillMaxWidth()
        ) {
            items(
                items = animeList,
                key = { it.id }
            ) { animeItem ->
                AnimeCard(
                    anime = animeItem,
                    onCardClicked = {
                        eventHandler(
                            OnAnimeCardClicked(id = animeItem.id)
                        )
                    }
                )
            }
        }
    }
}

@Composable
private fun AnimeListHeader(
    title: String,
    onMoreClicked: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = title,
            style = AniLibTypography.titleMedium
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clip(RoundedCornerShape(4.dp))
                .clickable { onMoreClicked() }
                .padding(start = 4.dp)
        ) {
            Text(
                text = "More",
                style = AniLibTypography.titleMedium
            )
            Icon(
                imageVector = Icons.Rounded.ArrowForwardIos,
                contentDescription = null,
                modifier = Modifier
                    .padding(top = 2.dp)
                    .size(16.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AnimeCard(
    anime: Anime,
    onCardClicked: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(120.dp)
            .wrapContentHeight(),
        shape = RoundedCornerShape(8.dp),
        onClick = { onCardClicked() }
    ) {
        Column {
            AniLibImage(
                imageUrl = anime.image.original,
                alpha = 1.0f,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .clip(
                        RoundedCornerShape(
                            topStart = 8.dp,
                            topEnd = 8.dp
                        )
                    )
            )
            AnimeCardText(
                title = anime.name,
                score = anime.score,
                numberOfEpisodes = anime.numberOfEpisodes,
                episodesAired = anime.episodesAired,
                status = anime.status
            )
        }
    }
}

@Composable
private fun AnimeCardText(
    title: String,
    score: String,
    numberOfEpisodes: String,
    episodesAired: Int,
    status: String
) {
    Column(modifier = Modifier.padding(4.dp)) {
        Text(
            text = title,
            style = AniLibTypography.titleSmall,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1
        )

        when (status) {
            "ongoing", "released" -> {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "$episodesAired/$numberOfEpisodes ep",
                        style = AniLibTypography.bodySmall,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                    )
                    Text(
                        text = " ∙ ",
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                    )
                    Text(
                        text = score,
                        style = AniLibTypography.bodySmall,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                    )
                }
            }
        }
    }
}
