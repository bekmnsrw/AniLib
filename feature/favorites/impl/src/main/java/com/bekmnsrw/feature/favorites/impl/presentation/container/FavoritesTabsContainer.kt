package com.bekmnsrw.feature.favorites.impl.presentation.container

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
import androidx.compose.material.ScrollableTabRow
import androidx.compose.material.Tab
import androidx.compose.material.TabRowDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import com.bekmnsrw.core.designsystem.theme.AniLibTypography
import com.bekmnsrw.core.widget.AniLibCircularProgressBar
import com.bekmnsrw.core.widget.AniLibImage
import com.bekmnsrw.feature.favorites.api.model.UserRate
import com.bekmnsrw.feature.favorites.impl.R
import com.bekmnsrw.feature.favorites.impl.UserRatesEnum
import com.bekmnsrw.feature.favorites.impl.presentation.TabItem
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import kotlinx.collections.immutable.PersistentList
import kotlinx.coroutines.launch

internal class FavoritesTabsContainer : Screen {

    @OptIn(ExperimentalPagerApi::class)
    @Composable
    override fun Content() {
        val screenModel = getScreenModel<FavoritesTabsContainerScreenModel>()
        val screenState by screenModel.screenState.collectAsStateWithLifecycle()
        val pagerState = rememberPagerState()

        FavoritesScreenContent(
            tabs = screenState.tabs,
            pagerState = pagerState
        )
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun FavoritesScreenContent(
    tabs: PersistentList<TabItem>,
    pagerState: PagerState
) {
    Scaffold { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            Tabs(tabs = tabs, pagerState = pagerState)
            TabsContent(tabs = tabs, pagerState = pagerState)
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun Tabs(
    tabs: PersistentList<TabItem>,
    pagerState: PagerState
) {
    val coroutineScope = rememberCoroutineScope()

    ScrollableTabRow(
        selectedTabIndex = pagerState.currentPage,
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                modifier = Modifier.pagerTabIndicatorOffset(
                    pagerState = pagerState,
                    tabPositions = tabPositions
                )
            )
        }
    ) {
        tabs.forEachIndexed { index, tabItem ->
            Tab(
                selected = pagerState.currentPage == index,
                text = { Text(tabItem.title) },
                onClick = {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                }
            )
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun TabsContent(
    tabs: PersistentList<TabItem>,
    pagerState: PagerState
) {
    HorizontalPager(
        count = tabs.size,
        state = pagerState
    ) { page ->
        tabs[page].screenContent()
    }
}

@Composable
fun TabAnimeList(
    userRatePaged: LazyPagingItems<UserRate>,
    status: String,
    isLoading: Boolean,
    onItemClicked: (Int) -> Unit
) {
    if (isLoading) {
        AniLibCircularProgressBar(shouldShow = true)
    } else {
        if (userRatePaged.itemCount == 0) {
            EmptyListText(status = status)
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(16.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 56.dp)
            ) {
                items(
                    count = userRatePaged.itemCount,
                    key = userRatePaged.itemKey { it.anime.id },
                    contentType = userRatePaged.itemContentType { "AnimePaged" }
                ) { index ->
                    ListItem(
                        userRate = userRatePaged[index],
                        onItemClicked = onItemClicked
                    )
                }
            }
        }
    }
}

@Composable
private fun EmptyListText(status: String) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.align(Alignment.Center)
        ) {
            Text(
                text = stringResource(id = R.string.empty_list_placeholder)
            )
            Text(
                text = when (status) {
                    UserRatesEnum.PLANNED.status -> stringResource(id = R.string.planned)
                    UserRatesEnum.WATCHING.status ->  stringResource(id = R.string.watching)
                    UserRatesEnum.COMPLETED.status ->  stringResource(id = R.string.completed)
                    UserRatesEnum.ON_HOLD.status ->  stringResource(id = R.string.on_hold)
                    UserRatesEnum.DROPPED.status ->  stringResource(id = R.string.dropped)
                    else -> ""
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ListItem(
    userRate: UserRate?,
    onItemClicked: (Int) -> Unit
) {
    userRate?.let {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            shape = RoundedCornerShape(8.dp),
            onClick = { onItemClicked(userRate.anime.id) }
        ) {
            Row {
                AniLibImage(
                    imageUrl = userRate.anime.image.original,
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
                ListItemDescription(userRate = userRate)
            }
        }
    }
}

@Composable
private fun ListItemDescription(userRate: UserRate) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        with(userRate) {
            DescriptionItem(
                value = anime.name,
                style = AniLibTypography.titleMedium,
                maxLines = 2
            )
            DescriptionItem(
                key = "Status: ",
                value = anime.status
            )
            DescriptionItem(
                key = "Kind: ",
                value = anime.kind
            )
            when (anime.status) {
                "ongoing", "released" -> {
                    DescriptionItem(
                        key = "Rating: ",
                        value = anime.score
                    )
                    DescriptionItem(
                        key = "Your rating: ",
                        value = if (userScore == 0) "No rating" else "$userScore"
                    )
                    if (anime.kind != "movie") {
                        DescriptionItem(
                            key = "Episodes: ",
                            value = when (anime.status) {
                                "ongoing" -> "${anime.episodesAired}/${anime.numberOfEpisodes} ep"
                                else -> "${anime.numberOfEpisodes} ep"
                            }
                        )
                        DescriptionItem(
                            key = "Watched: ",
                            value = "$episodesWatched ep"
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DescriptionItem(
    key: String = "",
    value: String,
    style: TextStyle = AniLibTypography.bodyMedium,
    maxLines: Int = 1
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
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
            maxLines = maxLines,
        )
    }
}
