package com.bekmnsrw.feature.profile.impl.presentation.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
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
import com.bekmnsrw.core.designsystem.theme.DarkAndroidColorScheme
import com.bekmnsrw.core.designsystem.theme.DarkDefaultColorScheme
import com.bekmnsrw.core.designsystem.theme.LightAndroidColorScheme
import com.bekmnsrw.core.designsystem.theme.LightDefaultColorScheme
import com.bekmnsrw.core.navigation.SharedScreen
import com.bekmnsrw.core.utils.convertStringToDateTime
import com.bekmnsrw.core.utils.formatStatusString
import com.bekmnsrw.core.widget.indicator.AniLibCircularProgressBar
import com.bekmnsrw.core.widget.button.AniLibIconButton
import com.bekmnsrw.core.widget.AniLibImage
import com.bekmnsrw.core.widget.UserRatesEnum
import com.bekmnsrw.feature.profile.api.model.AnimeRates
import com.bekmnsrw.feature.profile.api.model.WhoAmI
import com.bekmnsrw.feature.profile.impl.presentation.profile.ProfileScreenModel.ProfileScreenAction
import com.bekmnsrw.feature.profile.impl.presentation.profile.ProfileScreenModel.ProfileScreenAction.NavigateAuthScreen
import com.bekmnsrw.feature.profile.impl.presentation.profile.ProfileScreenModel.ProfileScreenAction.NavigateDetailsScreen
import com.bekmnsrw.feature.profile.impl.presentation.profile.ProfileScreenModel.ProfileScreenAction.NavigateFavoritesTab
import com.bekmnsrw.feature.profile.impl.presentation.profile.ProfileScreenModel.ProfileScreenAction.NavigateSettingsScreen
import com.bekmnsrw.feature.profile.impl.presentation.profile.ProfileScreenModel.ProfileScreenEvent.OnItemClick
import com.bekmnsrw.feature.profile.impl.presentation.profile.ProfileScreenModel.ProfileScreenEvent.OnMoreClick
import com.bekmnsrw.feature.profile.impl.presentation.profile.ProfileScreenModel.ProfileScreenEvent.OnSettingsIconClick
import com.bekmnsrw.feature.profile.impl.presentation.settings.SettingsScreen
import com.bekmnsrw.profile.impl.R
import com.github.tehras.charts.piechart.PieChart
import com.github.tehras.charts.piechart.PieChartData
import com.github.tehras.charts.piechart.animation.simpleChartAnimation
import com.github.tehras.charts.piechart.renderer.SimpleSliceDrawer
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.PersistentMap

internal class ProfileScreen : Screen {

    @Composable
    override fun Content() {
        val screenModel = getScreenModel<ProfileScreenModel>()
        val screenState by screenModel.screenState.collectAsStateWithLifecycle()
        val screenAction by screenModel.screenAction.collectAsStateWithLifecycle(initialValue = null)

        if (screenState.isLoading) {
            AniLibCircularProgressBar(shouldShow = true)
        } else {
            ProfileScreenContent(
                whoAmI = screenState.profile,
                userAnimeRates = screenState.userAnimeRates,
                userAnimeStatuses = screenState.userAnimeStatuses,
                onSettingsIconClick = { screenModel.eventHandler(OnSettingsIconClick) },
                onItemClick = { screenModel.eventHandler(OnItemClick(id = it)) },
                onMoreClick = { screenModel.eventHandler(OnMoreClick) }
            )
        }

        ProfileScreenActions(screenAction = screenAction)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProfileScreenContent(
    whoAmI: WhoAmI?,
    userAnimeRates: PersistentList<AnimeRates>,
    userAnimeStatuses: PersistentMap<String, Int>,
    onSettingsIconClick: () -> Unit,
    onItemClick: (Int) -> Unit,
    onMoreClick: () -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(state = rememberTopAppBarState())

    Scaffold(
        modifier = Modifier.nestedScroll(connection = scrollBehavior.nestedScrollConnection),
        topBar = { ProfileTopAppBar(scrollBehavior = scrollBehavior, onClick = onSettingsIconClick) }
    ) { paddingValues ->
        whoAmI?.let { profile ->
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(32.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(bottom = 56.dp)
            ) {
                item {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        AniLibImage(
                            modifier = Modifier.clip(CircleShape),
                            imageUrl = profile.image.x160,
                            isAvatar = true
                        )
                        Text(
                            text = profile.nickname,
                            style = AniLibTypography.titleMedium
                        )
                    }
                }
                item {
                    UserAnimeStatuses(
                        userAnimeStatuses = userAnimeStatuses,
                        onMoreClick = onMoreClick
                    )
                }
                item {
                    UserAnimeRates(
                        userAnimeRates = userAnimeRates,
                        onItemClick = onItemClick
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProfileTopAppBar(
    scrollBehavior: TopAppBarScrollBehavior,
    onClick: () -> Unit
) = TopAppBar(
    title = {},
    scrollBehavior = scrollBehavior,
    actions = {
        AniLibIconButton(
            onClick = onClick,
            imageVector = AniLibIcons.Settings
        )
    }
)

@Composable
private fun UserAnimeStatuses(
    userAnimeStatuses: PersistentMap<String, Int>,
    onMoreClick: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        ProfileItemHeader(
            text = stringResource(id = R.string.statistics),
            onClick = onMoreClick
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                userAnimeStatuses.forEach { status ->
                    AnimeStatusPieChartPlotItem(key = status.key, value = status.value)
                }
            }
            AnimeStatusPieChart(userAnimeStatuses = userAnimeStatuses)
        }
    }
}

@Composable
private fun AnimeStatusPieChartPlotItem(
    key: String,
    value: Int
) {
    val isDarkMode = isSystemInDarkTheme()

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Card(
            modifier = Modifier.size(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = when (key) {
                    UserRatesEnum.WATCHING.key -> when (isDarkMode) {
                        true -> DarkDefaultColorScheme.tertiaryContainer
                        false -> LightDefaultColorScheme.tertiaryContainer
                    }
                    UserRatesEnum.DROPPED.key -> when (isDarkMode) {
                        true -> DarkDefaultColorScheme.error
                        false -> LightDefaultColorScheme.error
                    }
                    UserRatesEnum.PLANNED.key -> when (isDarkMode) {
                        true -> DarkDefaultColorScheme.primary
                        false -> LightDefaultColorScheme.primary
                    }
                    UserRatesEnum.ON_HOLD.key -> when (isDarkMode) {
                        true -> DarkAndroidColorScheme.tertiary
                        false -> LightAndroidColorScheme.tertiary
                    }
                    else -> when (isDarkMode) {
                        true -> DarkAndroidColorScheme.primary
                        false -> LightAndroidColorScheme.primary
                    }
                }
            )
        ) {}
        Text(text = "${formatStatusString(key)}: $value")
    }
}

@Composable
private fun AnimeStatusPieChart(
    userAnimeStatuses: PersistentMap<String, Int>
) {
    val totalCountOfStatuses = userAnimeStatuses.values.sum()
    val isDarkMode = isSystemInDarkTheme()

    PieChart(
        pieChartData = PieChartData(
            listOf(
                PieChartData.Slice(
                    value = userAnimeStatuses[UserRatesEnum.WATCHING.key]!!.toFloat()/totalCountOfStatuses,
                    color = when (isDarkMode) {
                        true -> DarkDefaultColorScheme.tertiaryContainer
                        false -> LightDefaultColorScheme.tertiaryContainer
                    }
                ),
                PieChartData.Slice(
                    value = userAnimeStatuses[UserRatesEnum.DROPPED.key]!!.toFloat()/totalCountOfStatuses,
                    color = when (isDarkMode) {
                        true -> DarkDefaultColorScheme.error
                        false -> LightDefaultColorScheme.error
                    }
                ),
                PieChartData.Slice(
                    value = userAnimeStatuses[UserRatesEnum.PLANNED.key]!!.toFloat()/totalCountOfStatuses,
                    color = when (isDarkMode) {
                        true -> DarkDefaultColorScheme.primary
                        false -> LightDefaultColorScheme.primary
                    }
                ),
                PieChartData.Slice(
                    value = userAnimeStatuses[UserRatesEnum.ON_HOLD.key]!!.toFloat()/totalCountOfStatuses,
                    color = when (isDarkMode) {
                        true -> DarkAndroidColorScheme.tertiary
                        false -> LightAndroidColorScheme.tertiary
                    }
                ),
                PieChartData.Slice(
                    value = userAnimeStatuses[UserRatesEnum.COMPLETED.key]!!.toFloat()/totalCountOfStatuses,
                    color = when (isDarkMode) {
                        true -> DarkAndroidColorScheme.primary
                        false -> LightAndroidColorScheme.primary
                    }
                )
            )
        ),
        modifier = Modifier.size(120.dp),
        animation = simpleChartAnimation(),
        sliceDrawer = SimpleSliceDrawer()
    )
}

@Composable
private fun UserAnimeRates(
    userAnimeRates: PersistentList<AnimeRates>,
    onMoreClick: () -> Unit = {},
    onItemClick: (Int) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        ProfileItemHeader(
            text = stringResource(id = R.string.your_rates),
            onClick = onMoreClick
        )
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            userAnimeRates.forEach {
                Row(
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onItemClick(it.animeId) }
                ) {
                    AniLibImage(
                        modifier = Modifier
                            .height(120.dp)
                            .width(80.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        imageUrl = it.image
                    )
                    Column {
                        Text(
                            text = it.name,
                            style = AniLibTypography.titleMedium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = it.russian,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            val dateTime = convertStringToDateTime(it.updatedAt)

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = "${it.score}")
                                Icon(imageVector = AniLibIcons.StarRate, contentDescription = null)
                            }
                            Text(text = "${dateTime.first} ${dateTime.second}")
                        }
                        Text(text = "${formatStatusString(it.status)}")
                    }
                }
            }
        }
    }
}

@Composable
private fun ProfileItemHeader(
    text: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = text,
            style = AniLibTypography.titleMedium
        )
        Text(
            text = stringResource(id = R.string.more),
            style = AniLibTypography.titleMedium,
            modifier = Modifier.clickable { onClick() }
        )
    }
}

@Composable
private fun ProfileScreenActions(screenAction: ProfileScreenAction?) {
    val navigator = LocalNavigator.currentOrThrow

    LaunchedEffect(screenAction) {
        when (screenAction) {
            null -> Unit

            NavigateAuthScreen -> {
                val authScreen = ScreenRegistry.get(
                    provider = SharedScreen.AuthScreen
                )
                navigator.replaceAll(item = authScreen)
            }

            is NavigateDetailsScreen -> {
                val detailsScreen = ScreenRegistry.get(
                    provider = SharedScreen.DetailsScreen(
                        id = screenAction.id
                    )
                )
                navigator.push(item = detailsScreen)
            }

            NavigateFavoritesTab -> {
                val favoritesTab = ScreenRegistry.get(
                    provider = SharedScreen.FavoritesScreen
                )
                navigator.push(item = favoritesTab)
            }

            NavigateSettingsScreen -> navigator.push(
                item = SettingsScreen()
            )
        }
    }
}
