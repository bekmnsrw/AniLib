package com.bekmnsrw.feature.favorites.impl.presentation.favorites

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.registry.ScreenRegistry
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.bekmnsrw.core.designsystem.icon.AniLibIcons
import com.bekmnsrw.core.designsystem.theme.AniLibTypography
import com.bekmnsrw.core.navigation.SharedScreen
import com.bekmnsrw.core.widget.AniLibImage
import com.bekmnsrw.core.widget.indicator.AniLibPullRefreshIndicator
import com.bekmnsrw.feature.favorites.api.model.FavoriteAnime
import com.bekmnsrw.feature.favorites.impl.R
import com.bekmnsrw.feature.favorites.impl.presentation.favorites.FavoritesScreenModel.FavoritesScreenAction
import com.bekmnsrw.feature.favorites.impl.presentation.favorites.FavoritesScreenModel.FavoritesScreenAction.NavigateDetails
import com.bekmnsrw.feature.favorites.impl.presentation.favorites.FavoritesScreenModel.FavoritesScreenEvent
import com.bekmnsrw.feature.favorites.impl.presentation.favorites.FavoritesScreenModel.FavoritesScreenEvent.OnIconFavoriteClick
import com.bekmnsrw.feature.favorites.impl.presentation.favorites.FavoritesScreenModel.FavoritesScreenEvent.OnItemClick
import com.bekmnsrw.feature.favorites.impl.presentation.favorites.FavoritesScreenModel.FavoritesScreenEvent.OnRefresh
import kotlinx.coroutines.launch

internal class FavoritesScreen : Screen {

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    override fun Content() {
        val screenModel = getScreenModel<FavoritesScreenModel>()
        val screenState by screenModel.screenState.collectAsStateWithLifecycle()
        val screenAction by screenModel.screenAction.collectAsStateWithLifecycle(initialValue = null)

        val pullRefreshState = rememberPullRefreshState(
            refreshing = screenState.refreshing,
            onRefresh = { screenModel.eventHandler(OnRefresh) }
        )

        val snackbarHostState = remember { SnackbarHostState() }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .pullRefresh(pullRefreshState)
        ) {
            AnimatedVisibility(
                visible = !screenState.refreshing,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                if (screenState.favorites.isEmpty()) {
                    EmptyListText()
                } else {
                    FavoritesScreenContent(
                        favoritesAnime = screenState.favorites,
                        eventHandler = screenModel::eventHandler
                    )
                }
            }
            AniLibPullRefreshIndicator(
                modifier = Modifier.align(Alignment.TopCenter),
                refreshing = screenState.refreshing,
                pullRefreshState = pullRefreshState
            )
        }

        FavoritesScreenActions(
            screenAction = screenAction,
            snackbarHostState = snackbarHostState
        )
    }
}

@Composable
private fun EmptyListText() {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.align(Alignment.Center)
        ) {
            Text(text = stringResource(id = R.string.empty_list_placeholder))
            Text(text = stringResource(id = R.string.favorites))
        }
    }
}

@Composable
private fun FavoritesScreenActions(
    screenAction: FavoritesScreenAction?,
    snackbarHostState: SnackbarHostState
) {
    val navigator = LocalNavigator.currentOrThrow
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(screenAction) {
        when (screenAction) {
            null -> Unit

            is NavigateDetails -> {
                val detailsScreen = ScreenRegistry.get(
                    SharedScreen.DetailsScreen(
                        id = screenAction.id
                    )
                )
                navigator.push(item = detailsScreen)
            }

            is FavoritesScreenAction.ShowSnackbar -> coroutineScope.launch {
                snackbarHostState.showSnackbar(
                    message = screenAction.message,
                    duration = SnackbarDuration.Short
                )
            }
        }
    }
}

@Composable
private fun FavoritesScreenContent(
    favoritesAnime: List<FavoriteAnime>,
    eventHandler: (FavoritesScreenEvent) -> Unit
) {
    Scaffold(modifier = Modifier.fillMaxSize()) { contentPadding ->
        FavoritesAnimeList(
            favoritesAnime = favoritesAnime,
            contentPadding = contentPadding,
            eventHandler = eventHandler
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun FavoritesAnimeList(
    favoritesAnime: List<FavoriteAnime>,
    contentPadding: PaddingValues,
    eventHandler: (FavoritesScreenEvent) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding)
            .padding(bottom = 56.dp)
    ) {
        items(
            items = favoritesAnime,
            key = { it.id },
            contentType = { "FavoriteAnime" }
        ) {
            FavoritesAnimeListItem(
                modifier = Modifier.animateItemPlacement(),
                anime = it,
                onItemClicked = { eventHandler(OnItemClick(id = it.id)) },
                onIconFavoriteClicked = { eventHandler(OnIconFavoriteClick(id = it.id)) }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FavoritesAnimeListItem(
    modifier: Modifier,
    anime: FavoriteAnime,
    onItemClicked: () -> Unit,
    onIconFavoriteClicked: () -> Unit
) {
    Card(
        onClick = onItemClicked,
        shape = RoundedCornerShape(8.dp),
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AniLibImage(
                imageUrl = anime.image,
                modifier = Modifier
                    .weight(0.75f)
                    .height(120.dp)
                    .clip(
                        RoundedCornerShape(
                            topStart = 8.dp,
                            bottomStart = 8.dp
                        )
                    )
            )
            Column(
                modifier = Modifier
                    .weight(2f)
                    .padding(8.dp)
            ) {
                Text(
                    text = anime.name,
                    style = AniLibTypography.titleSmall
                )
                Spacer(modifier = Modifier.padding(vertical = 2.dp))
                Text(
                    text = anime.russian,
                    style = AniLibTypography.bodySmall
                )
            }
            Icon(
                imageVector = AniLibIcons.FavoritesFilled,
                tint = MaterialTheme.colorScheme.primary,
                contentDescription = null,
                modifier = Modifier
                    .padding(end = 16.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { onIconFavoriteClicked() }
            )
        }
    }
}
