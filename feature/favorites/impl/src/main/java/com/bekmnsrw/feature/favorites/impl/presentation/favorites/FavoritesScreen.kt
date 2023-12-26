package com.bekmnsrw.feature.favorites.impl.presentation.favorites

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.registry.ScreenRegistry
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.bekmnsrw.core.designsystem.theme.AniLibTypography
import com.bekmnsrw.core.navigation.SharedScreen
import com.bekmnsrw.core.widget.AniLibCircularProgressBar
import com.bekmnsrw.core.widget.AniLibImage
import com.bekmnsrw.feature.favorites.api.model.FavoriteAnime
import com.bekmnsrw.feature.favorites.impl.presentation.favorites.FavoritesScreenModel.FavoritesScreenAction
import com.bekmnsrw.feature.favorites.impl.presentation.favorites.FavoritesScreenModel.FavoritesScreenAction.NavigateDetails
import com.bekmnsrw.feature.favorites.impl.presentation.favorites.FavoritesScreenModel.FavoritesScreenEvent
import com.bekmnsrw.feature.favorites.impl.presentation.favorites.FavoritesScreenModel.FavoritesScreenEvent.OnItemClicked

internal class FavoritesScreen : Screen {

    @Composable
    override fun Content() {
        val screenModel = getScreenModel<FavoritesScreenModel>()
        val screenState by screenModel.screenState.collectAsStateWithLifecycle()
        val screenAction by screenModel.screenAction.collectAsStateWithLifecycle(initialValue = null)

        if (screenState.isLoading) {
            AniLibCircularProgressBar(shouldShow = true)
        } else {
            FavoritesScreenContent(
                favoritesAnime = screenState.favorites,
                eventHandler = screenModel::eventHandler
            )
        }

        FavoritesScreenActions(
            screenAction = screenAction
        )
    }
}

@Composable
private fun FavoritesScreenActions(screenAction: FavoritesScreenAction?) {
    val navigator = LocalNavigator.currentOrThrow

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

@Composable
private fun FavoritesAnimeList(
    favoritesAnime: List<FavoriteAnime>,
    contentPadding: PaddingValues,
    eventHandler: (FavoritesScreenEvent) -> Unit
) {
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2),
        verticalItemSpacing = 10.dp,
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding)
    ) {
        items(
            items = favoritesAnime,
            key = { it.id },
            contentType = { "FavoriteAnime" }
        ) {
            FavoritesAnimeListItem(
                anime = it,
                onItemClicked = {
                    eventHandler(
                        OnItemClicked(
                            id = it.id
                        )
                    )
                }
            )
        }
    }
}

/* Add like icon */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FavoritesAnimeListItem(
    anime: FavoriteAnime,
    onItemClicked: () -> Unit
) {
    Card(
        onClick = onItemClicked,
        shape = RoundedCornerShape(8.dp)
    ) {
        Column {
            AniLibImage(
                imageUrl = anime.image,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .clip(
                        RoundedCornerShape(
                            topStart = 8.dp,
                            topEnd = 8.dp
                        )
                    )
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
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
        }
    }
}
