package com.bekmnsrw.feature.favorites.impl.presentation.container

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.material.ScrollableTabRow
import androidx.compose.material.Tab
import androidx.compose.material.TabRowDefaults
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import cafe.adriel.voyager.core.screen.Screen
import com.bekmnsrw.core.designsystem.icon.AniLibIcons
import com.bekmnsrw.core.designsystem.theme.AniLibTypography
import com.bekmnsrw.core.widget.AniLibCircularProgressBar
import com.bekmnsrw.core.widget.AniLibImage
import com.bekmnsrw.core.widget.AniLibModalBottomSheet
import com.bekmnsrw.core.widget.UserRatesEnum
import com.bekmnsrw.feature.favorites.api.model.UserRates
import com.bekmnsrw.feature.favorites.impl.R
import com.bekmnsrw.feature.favorites.impl.presentation.TabItem
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.launch

internal class FavoritesTabsContainer : Screen {

    private companion object {
        val tabs = persistentListOf(
            TabItem.Favorites,
            TabItem.PlannedToWatch,
            TabItem.Completed,
            TabItem.Watching,
            TabItem.Dropped,
            TabItem.OnHold
        )
    }

    @OptIn(ExperimentalPagerApi::class)
    @Composable
    override fun Content() {
        val pagerState = rememberPagerState()

        FavoritesScreenContent(
            tabs = tabs,
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
        divider = {},
        edgePadding = 16.dp,
        selectedTabIndex = pagerState.currentPage,
        backgroundColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground,
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                color = MaterialTheme.colorScheme.primary,
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
                        pagerState.scrollToPage(index)
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
    userRatesPaged: LazyPagingItems<UserRates>,
    status: String,
    isLoading: Boolean,
    onItemClick: (Int) -> Unit,
    onLongClick: (Int) -> Unit
) {
    if (isLoading) {
        AniLibCircularProgressBar(shouldShow = true)
    } else {
        if (userRatesPaged.itemCount == 0) {
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
                    count = userRatesPaged.itemCount,
                    key = userRatesPaged.itemKey { it.anime.id },
                    contentType = userRatesPaged.itemContentType { "AnimePaged" }
                ) { index ->
                    ListItem(
                        index = index,
                        userRates = userRatesPaged[index],
                        onItemClick = onItemClick,
                        onLongClick = onLongClick
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ListItem(
    index: Int,
    userRates: UserRates?,
    onItemClick: (Int) -> Unit,
    onLongClick: (Int) -> Unit
) {
    userRates?.let {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .combinedClickable(
                    onClick = { onItemClick(userRates.anime.id) },
                    onLongClick = { onLongClick(index) }
                ),
            shape = RoundedCornerShape(8.dp)
        ) {
            Row {
                AniLibImage(
                    imageUrl = userRates.anime.image.original,
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
                ListItemDescription(
                    userRates = userRates,
                    onClick = { onLongClick(index) }
                )
            }
        }
    }
}

@Composable
private fun ListItemDescription(
    userRates: UserRates,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        with(userRates) {
            Row(
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = anime.name,
                    style = AniLibTypography.titleMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = AniLibIcons.MoreVert,
                    contentDescription = null,
                    modifier = Modifier.clickable { onClick() }
                )
            }
            Spacer(modifier = Modifier.padding(vertical = 2.dp))
            Text(text = anime.status.replaceFirstChar { it.uppercase() })
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if (anime.kind != "movie" && anime.status != "anons") {
                    Text(
                        text = when (anime.status) {
                            "released" -> "${anime.numberOfEpisodes} ep"
                            "ongoing" -> "${anime.episodesAired}/${anime.numberOfEpisodes} ep"
                            else -> ""
                        }
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = anime.score
                        )
                        Icon(
                            imageVector = AniLibIcons.StarRate,
                            contentDescription = null
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimeBottomSheet(
    sheetState: SheetState,
    category: String,
    name: String,
    onChangeCategoryClick: () -> Unit,
    onDismissRequest: () -> Unit
) {
    AniLibModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = onDismissRequest
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Text(
                text = name,
                style = AniLibTypography.titleMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Divider()
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onChangeCategoryClick() },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = AniLibIcons.AddToList,
                    contentDescription = null
                )
                Text(text = stringResource(id = R.string.in_category, category))
            }
        }
    }
}

@Composable
private fun EmptyListText(status: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.align(Alignment.Center),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = stringResource(id = R.string.empty_list_placeholder),
                style = AniLibTypography.titleMedium
            )
            Text(
                text = when (status) {
                    UserRatesEnum.PLANNED.key -> stringResource(id = R.string.planned)
                    UserRatesEnum.WATCHING.key -> stringResource(id = R.string.watching)
                    UserRatesEnum.COMPLETED.key -> stringResource(id = R.string.completed)
                    UserRatesEnum.ON_HOLD.key -> stringResource(id = R.string.on_hold)
                    UserRatesEnum.DROPPED.key -> stringResource(id = R.string.dropped)
                    else -> ""
                },
               textAlign = TextAlign.Center
            )
        }
    }
}
