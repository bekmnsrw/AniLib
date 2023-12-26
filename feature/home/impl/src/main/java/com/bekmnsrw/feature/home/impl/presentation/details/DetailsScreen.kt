package com.bekmnsrw.feature.home.impl.presentation.details

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.registry.ScreenRegistry
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.bekmnsrw.core.designsystem.icon.AniLibIcons
import com.bekmnsrw.core.designsystem.theme.AniLibTypography
import com.bekmnsrw.core.designsystem.theme.LocalBackgroundTheme
import com.bekmnsrw.core.navigation.SharedScreen
import com.bekmnsrw.core.widget.AniLibAgeRatingBadge
import com.bekmnsrw.core.widget.AniLibCircularProgressBar
import com.bekmnsrw.core.widget.AniLibExpandableTextWithTextButton
import com.bekmnsrw.core.widget.AniLibHorizontalList
import com.bekmnsrw.core.widget.AniLibImage
import com.bekmnsrw.core.widget.AniLibModalBottomSheet
import com.bekmnsrw.core.widget.AniLibRateWidget
import com.bekmnsrw.core.widget.AniLibSnackbar
import com.bekmnsrw.core.widget.AniLibStatusWidget
import com.bekmnsrw.feature.home.api.model.Anime
import com.bekmnsrw.feature.home.api.model.AnimeDetails
import com.bekmnsrw.feature.home.api.model.Genre
import com.bekmnsrw.feature.home.impl.AnimeStatusEnum.ONGOING
import com.bekmnsrw.feature.home.impl.AnimeStatusEnum.RELEASED
import com.bekmnsrw.feature.home.impl.HomeConstants.ANIME_ID_KOIN_PROPERTY
import com.bekmnsrw.feature.home.impl.R
import com.bekmnsrw.feature.home.impl.presentation.details.DetailsScreenModel.DetailsScreenAction
import com.bekmnsrw.feature.home.impl.presentation.details.DetailsScreenModel.DetailsScreenAction.NavigateBack
import com.bekmnsrw.feature.home.impl.presentation.details.DetailsScreenModel.DetailsScreenAction.NavigateDetailsScreen
import com.bekmnsrw.feature.home.impl.presentation.details.DetailsScreenModel.DetailsScreenAction.ShowErrorSnackBar
import com.bekmnsrw.feature.home.impl.presentation.details.DetailsScreenModel.DetailsScreenAction.ShowIsFavouredSnackbar
import com.bekmnsrw.feature.home.impl.presentation.details.DetailsScreenModel.DetailsScreenEvent
import com.bekmnsrw.feature.home.impl.presentation.details.DetailsScreenModel.DetailsScreenEvent.OnArrowBackClicked
import com.bekmnsrw.feature.home.impl.presentation.details.DetailsScreenModel.DetailsScreenEvent.OnDescriptionClicked
import com.bekmnsrw.feature.home.impl.presentation.details.DetailsScreenModel.DetailsScreenEvent.OnFavouredClicked
import com.bekmnsrw.feature.home.impl.presentation.details.DetailsScreenModel.DetailsScreenEvent.OnInfoIconClicked
import com.bekmnsrw.feature.home.impl.presentation.details.DetailsScreenModel.DetailsScreenEvent.OnModalBottomSheetDismiss
import com.bekmnsrw.feature.home.impl.presentation.details.DetailsScreenModel.DetailsScreenEvent.OnSimilarAnimeCardClicked
import com.bekmnsrw.feature.home.impl.presentation.details.DetailsScreenModel.DetailsScreenState
import kotlinx.collections.immutable.PersistentList
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getKoin

private const val WAS_ADDED_TO_FAVORITES = "was added to favorites"
private const val WAS_REMOVED_FROM_FAVORITES = "was removed from favorites"
private const val MOVIE = "movie"

internal data class DetailsScreen(val id: Int) : Screen {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        getKoin().setProperty(ANIME_ID_KOIN_PROPERTY, id)

        val screenModel = getScreenModel<DetailsScreenModel>()
        val screenState by screenModel.screenState.collectAsStateWithLifecycle()
        val screenAction by screenModel.screenAction.collectAsStateWithLifecycle(initialValue = null)

        val snackbarHostState = remember { SnackbarHostState() }
        val modalBottomSheetState = rememberModalBottomSheetState()

        if (screenState.isLoading) {
            AniLibCircularProgressBar(shouldShow = true)
        } else {
            DetailsScreenContent(
                screenState = screenState,
                eventHandler = screenModel::eventHandler,
                animeId = id,
                snackbarHostState = snackbarHostState,
                modalBottomSheetState = modalBottomSheetState
            )
        }

        DetailsScreenActions(
            screenAction = screenAction,
            snackbarHostState = snackbarHostState
        )
    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun DetailsScreenContent(
    screenState: DetailsScreenState,
    eventHandler: (DetailsScreenEvent) -> Unit,
    animeId: Int,
    snackbarHostState: SnackbarHostState,
    modalBottomSheetState: SheetState
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
                    AnimeDetails(
                        animeDetails = screenState.animeDetails,
                        onIconClicked = { eventHandler(OnInfoIconClicked) },
                        isDescriptionExpanded = screenState.isDescriptionExpanded,
                        onDescriptionButtonClicked = { eventHandler(OnDescriptionClicked) },
                        similarAnimeList = screenState.similarAnimeList,
                        onSimilarAnimeCardClicked = { eventHandler(OnSimilarAnimeCardClicked(id = it)) }
                    )
                }
            }

            AniLibSnackbar(
                snackbarHostState = snackbarHostState,
                modifier = Modifier.align(Alignment.BottomCenter)
            )

            AnimatedVisibility(visible = screenState.shouldShowModalBottomSheet) {
                AniLibModalBottomSheet(
                    sheetState = modalBottomSheetState,
                    onDismissRequest = { eventHandler(OnModalBottomSheetDismiss) }
                ) {
                    screenState.animeDetails?.let { animeDetails ->
                        ModalBottomSheetContent(anime = animeDetails)
                    }
                }
            }
        }
    }
}

@Composable
private fun ModalBottomSheetContent(anime: AnimeDetails) {
    ModalBottomSheetItem(
        title = stringResource(id = R.string.original_name),
        supportingTextList = listOf(anime.name)
    )
    ModalBottomSheetItem(
        title = stringResource(id = R.string.russian_name),
        supportingTextList = listOf(anime.russian)
    )
    ModalBottomSheetItem(
        title = stringResource(id = R.string.japanese_name),
        supportingTextList = anime.japanese
    )
    ModalBottomSheetItem(
        title = stringResource(id = R.string.synonyms),
        supportingTextList = anime.synonyms
    )
}

@Composable
private fun ModalBottomSheetItem(
    title: String,
    supportingTextList: List<String>
) {
    Text(
        text = title,
        style = AniLibTypography.titleMedium
    )
    supportingTextList.forEach { supportingText ->
        Text(
            text = supportingText,
            style = AniLibTypography.bodyMedium
        )
    }
    Divider(modifier = Modifier.padding(vertical = 8.dp))
}

@Composable
private fun DetailsScreenActions(
    screenAction: DetailsScreenAction?,
    snackbarHostState: SnackbarHostState
) {
    val navigator = LocalNavigator.currentOrThrow
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(screenAction) {
        when (screenAction) {
            null -> Unit

            NavigateBack -> navigator.pop()

            is ShowIsFavouredSnackbar -> coroutineScope.launch {
                snackbarHostState.showSnackbar(
                    message = when (screenAction.isFavoured) {
                        true -> "'${screenAction.name}' $WAS_ADDED_TO_FAVORITES"
                        false -> "'${screenAction.name}' $WAS_REMOVED_FROM_FAVORITES"
                    },
                    duration = SnackbarDuration.Short
                )
            }

            is ShowErrorSnackBar -> coroutineScope.launch {
                snackbarHostState.showSnackbar(
                    message = screenAction.message,
                    duration = SnackbarDuration.Short
                )
            }

            is NavigateDetailsScreen -> {
                val detailsScreen = ScreenRegistry.get(
                    provider = SharedScreen.DetailsScreen(
                        id = screenAction.id
                    )
                )
                navigator.push(item = detailsScreen)
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
        StickyHeaderItem(
            imageVector = AniLibIcons.ArrowBack,
            onClick = { eventHandler(OnArrowBackClicked) }
        )
        StickyHeaderItem(
            imageVector = when (isFavoured) {
                true -> AniLibIcons.FavoritesFilled
                false -> AniLibIcons.FavoritesOutlined
            },
            onClick = {
                eventHandler(
                    OnFavouredClicked(
                        animeId = animeId
                    )
                )
            }
        )
    }
}

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
                modifier = Modifier.align(Alignment.Center),
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun AnimeDetails(
    animeDetails: AnimeDetails?,
    isDescriptionExpanded: Boolean,
    similarAnimeList: PersistentList<Anime>,
    onIconClicked: () -> Unit,
    onDescriptionButtonClicked: () -> Unit,
    onSimilarAnimeCardClicked: (Int) -> Unit
) {
    animeDetails?.let { anime ->
        AnimeImage(imageUrl = anime.image.original)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(horizontal = 16.dp)
                .offset(y = (-58).dp)
        ) {
            AnimeName(
                name = anime.name,
                ageRating = anime.rating,
                onIconClicked = onIconClicked
            )
            Spacer(modifier = Modifier.padding(vertical = 8.dp))
            AnimeInfo(anime = anime)
            Divider(modifier = Modifier.padding(vertical = 8.dp))
            AnimeGenre(genres = anime.genres)
            Divider(modifier = Modifier.padding(vertical = 8.dp))
            anime.description?.let {
                AniLibExpandableTextWithTextButton(
                    text = it,
                    isExpanded = isDescriptionExpanded,
                    onDescriptionButtonClicked = onDescriptionButtonClicked
                )
            }
            Divider(modifier = Modifier.padding(vertical = 8.dp))
            AniLibRateWidget(
                rate = anime.score,
                rateStats = anime.scoresStats,
                totalRateStats = anime.totalScoresStats
            )
            Divider(modifier = Modifier.padding(vertical = 8.dp))
            AniLibStatusWidget(
                statusStats = anime.statusesStats,
                totalStatusStats = anime.totalStatusesStats
            )
            if (similarAnimeList.isNotEmpty()) {
                Divider(modifier = Modifier.padding(vertical = 8.dp))
                AniLibHorizontalList(
                    animeList = similarAnimeList,
                    animeListTitle = stringResource(id = R.string.you_also_may_like),
                    onItemClicked = onSimilarAnimeCardClicked,
                    isMoreEnable = false
                )
                Spacer(modifier = Modifier.padding(vertical = 8.dp))
            }
        }
    }
}

@Composable
private fun AnimeImage(
    imageUrl: String
) {
    val backgroundColor = LocalBackgroundTheme.current.color

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .offset(y = (-64).dp)
    ) {
        AniLibImage(
            imageUrl = imageUrl,
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
            imageUrl = imageUrl,
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth()
                .heightIn(min = 0.dp, max = 368.dp)
                .padding(horizontal = 80.dp)
                .shadow(elevation = 16.dp, shape = RoundedCornerShape(8.dp))
                .clip(RoundedCornerShape(8.dp))
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun AnimeName(
    name: String,
    ageRating: String,
    onIconClicked: () -> Unit
) {
    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = name,
            style = AniLibTypography.titleLarge,
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .clickable { onIconClicked() }
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AniLibAgeRatingBadge(rating = ageRating)
            Icon(
                imageVector = AniLibIcons.Info,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { onIconClicked() }
            )
        }
    }
}

@Composable
private fun AnimeInfo(anime: AnimeDetails) {
    with(anime) {
        AnimeInfoItem(
            key = stringResource(id = R.string.status),
            value = status
        )
        AnimeInfoItem(
            key = stringResource(id = R.string.kind),
            value = kind
        )
        when (anime.status) {
            ONGOING.status, RELEASED.status -> {
                AnimeInfoItem(
                    key = stringResource(id = R.string.aired_on),
                    value = airedOn
                )
                if (status == RELEASED.status && releasedOn?.isNotEmpty() == true) {
                    AnimeInfoItem(
                        key = stringResource(id = R.string.released_on),
                        value = releasedOn!!
                    )
                }
                if (kind != MOVIE) {
                    AnimeInfoItem(
                        key = stringResource(id = R.string.episodes),
                        value = when (status) {
                            ONGOING.status -> "$episodesAired/$episodes ep ~ $duration min"
                            else -> "$episodes ep ~ $duration min"
                        }
                    )
                }
                if (status == ONGOING.status) {
                    nextEpisodeAt?.let {
                        val nextEpisodeAt = convertNextEpisodeAt(it)
                        AnimeInfoItem(
                            key = stringResource(id = R.string.next_episode_at),
                            value = "${nextEpisodeAt.first} ${nextEpisodeAt.second}"
                        )
                    }
                }
            }
        }
    }
}

/* Move to utils */
private fun convertNextEpisodeAt(nextEpisodeAt: String): Pair<String, String> {
    val dateTime = nextEpisodeAt.split("T")
    return Pair(dateTime[0], dateTime[1].substring(0, 5))
}

@Composable
private fun AnimeInfoItem(
    key: String = "",
    value: String,
    style: TextStyle = AniLibTypography.bodyLarge
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (key.isNotEmpty()) {
            Text(
                text = key,
                style = AniLibTypography.titleMedium
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

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun AnimeGenre(genres: List<Genre>) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        genres.forEach { genre ->
            AnimeGenreItem(
                genre = genre.name,
                onClick = {}
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AnimeGenreItem(
    genre: String,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick
    ) {
        Text(
            text = genre,
            style = AniLibTypography.bodyLarge,
            modifier = Modifier.padding(8.dp)
        )
    }
}