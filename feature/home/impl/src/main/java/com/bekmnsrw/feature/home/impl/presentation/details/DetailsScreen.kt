package com.bekmnsrw.feature.home.impl.presentation.details

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.bekmnsrw.core.designsystem.icon.AniLibIcons
import com.bekmnsrw.core.designsystem.theme.LocalBackgroundTheme
import com.bekmnsrw.core.widget.AniLibCircularProgressBar
import com.bekmnsrw.core.widget.AniLibImage
import com.bekmnsrw.core.widget.AniLibSnackbar
import com.bekmnsrw.feature.home.api.model.details.AnimeDetails
import com.bekmnsrw.feature.home.impl.presentation.details.DetailsScreenModel.DetailsScreenAction
import com.bekmnsrw.feature.home.impl.presentation.details.DetailsScreenModel.DetailsScreenAction.NavigateBack
import com.bekmnsrw.feature.home.impl.presentation.details.DetailsScreenModel.DetailsScreenEvent
import com.bekmnsrw.feature.home.impl.presentation.details.DetailsScreenModel.DetailsScreenEvent.OnArrowBackClicked
import com.bekmnsrw.feature.home.impl.presentation.details.DetailsScreenModel.DetailsScreenEvent.OnFavouredClicked
import com.bekmnsrw.feature.home.impl.presentation.details.DetailsScreenModel.DetailsScreenState
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getKoin

internal data class DetailsScreen(val id: Int) : Screen {

    @Composable
    override fun Content() {
        getKoin().setProperty("animeId", id)

        val navigator = LocalNavigator.currentOrThrow
        val screenModel = getScreenModel<DetailsScreenModel>()
        val screenState by screenModel.screenState.collectAsStateWithLifecycle()
        val screenAction by screenModel.screenAction.collectAsStateWithLifecycle(initialValue = null)

        val snackbarHostState = remember { SnackbarHostState() }

        DetailsScreenContent(
            screenState = screenState,
            eventHandler = screenModel::eventHandler,
            animeId = id,
            snackbarHostState = snackbarHostState
        )

        DetailsScreenActions(
            screenAction = screenAction,
            navigator = navigator,
            snackbarHostState = snackbarHostState
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun DetailsScreenContent(
    screenState: DetailsScreenState,
    eventHandler: (DetailsScreenEvent) -> Unit,
    animeId: Int,
    snackbarHostState: SnackbarHostState
) {
    Scaffold { contentPadding ->
        Box(modifier = Modifier.padding(contentPadding)) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                stickyHeader {
                    StickyHeader(
                        eventHandler = eventHandler,
                        animeId = animeId,
                        isFavoured = screenState.isFavoured
                    )
                }
                item {
                    DetailsContent(
                        animeDetails = screenState.animeDetails
                    )
                }
            }
            AniLibSnackbar(
                snackbarHostState = snackbarHostState,
                modifier = Modifier.align(Alignment.BottomCenter)
            )

            AniLibCircularProgressBar(shouldShow = screenState.isLoading)
        }
    }
}

@Composable
private fun DetailsScreenActions(
    screenAction: DetailsScreenAction?,
    navigator: Navigator,
    snackbarHostState: SnackbarHostState
) {
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(screenAction) {
        when (screenAction) {
            null -> Unit

            NavigateBack -> navigator.pop()

            is DetailsScreenAction.ShowIsFavouredSnackbar -> coroutineScope.launch {
                snackbarHostState.showSnackbar(
                    message = when (screenAction.isFavoured) {
                        true -> "'${screenAction.name}' was added to favorites"
                        false -> "'${screenAction.name}' was removed from favorites"
                    },
                    duration = SnackbarDuration.Short
                )
            }
        }
    }
}

@Composable
private fun StickyHeader(
    eventHandler: (DetailsScreenEvent) -> Unit,
    animeId: Int,
    isFavoured: Boolean
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = 16.dp,
                end = 16.dp,
                top = 16.dp
            )
    ) {
        StickyHeaderItem(imageVector = AniLibIcons.ArrowBack) {
            eventHandler(OnArrowBackClicked)
        }
        StickyHeaderItem(
            imageVector = if (isFavoured)
                AniLibIcons.FavoritesFilled
            else
                AniLibIcons.FavoritesBorder
        ) {
            eventHandler(
                OnFavouredClicked(
                    animeId = animeId
                )
            )
        }
    }
}

/* TODO: Add favorite full and outlined */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun StickyHeaderItem(
    imageVector: ImageVector,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.size(48.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        onClick = onClick
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Icon(
                imageVector = imageVector,
                contentDescription = null,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

@Composable
private fun DetailsContent(animeDetails: AnimeDetails?) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .offset(y = (-64).dp)
    ) {
        val backgroundColor = LocalBackgroundTheme.current.color

        animeDetails?.let { anime ->
            AniLibImage(
                imageUrl = anime.image.original,
                alpha = 0.4f,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 0.dp, max = 400.dp)
                    .drawWithCache {
                        val gradientBrush = Brush.verticalGradient(
                            colors = listOf(Color.Transparent, backgroundColor),
                            startY = size.height / 3,
                            endY = size.height
                        )
                        onDrawWithContent {
                            drawContent()
                            drawRect(brush = gradientBrush, blendMode = BlendMode.SrcOver)
                        }
                    }
                    .drawWithCache {
                        val gradientBrush = Brush.verticalGradient(
                            colors = listOf(backgroundColor, Color.Transparent),
                            startY = 0F,
                            endY = size.height / 3
                        )
                        onDrawWithContent {
                            drawContent()
                            drawRect(brush = gradientBrush, blendMode = BlendMode.SrcOver)
                        }
                    }
            )
            AniLibImage(
                imageUrl = anime.image.original,
                alpha = 1.0f,
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxWidth()
                    .heightIn(min = 0.dp, max = 368.dp)
                    .padding(horizontal = 80.dp)
                    .shadow(
                        elevation = 16.dp,
                        shape = RoundedCornerShape(8.dp),
                    )
                    .clip(RoundedCornerShape(8.dp))
            )
        }
    }
}
