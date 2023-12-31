package com.bekmnsrw.core.widget

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.bekmnsrw.core.designsystem.theme.AniLibTypography
import com.bekmnsrw.core.designsystem.theme.DarkAndroidColorScheme
import com.bekmnsrw.core.designsystem.theme.DarkDefaultColorScheme
import com.bekmnsrw.core.designsystem.theme.LightAndroidColorScheme
import com.bekmnsrw.core.designsystem.theme.LightDefaultColorScheme
import com.bekmnsrw.feature.home.api.model.RatesStatusesStat

private const val PLANNED_ENG = "Planned to Watch"
private const val PLANNED_RU = "Запланировано"
private const val COMPLETED_ENG = "Completed"
private const val COMPLETED_RU = "Просмотрено"
private const val WATCHING_ENG = "Watching"
private const val WATCHING_RU = "Смотрю"
private const val DROPPED_ENG = "Dropped"
private const val DROPPED_RU = "Брошено"
private const val ON_HOLD_ENG = "On Hold"
private const val ON_HOLD_RU = "Отложено"

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AniLibStatusWidget(
    statusStats: List<RatesStatusesStat?>,
    totalStatusStats: Int
) {
    val localDensity = LocalDensity.current
    var rowWidthDp by remember { mutableStateOf(0.dp) }

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = stringResource(id = R.string.in_lists_of_users),
            style = AniLibTypography.titleMedium
        )
        Spacer(modifier = Modifier.padding(4.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { coordinates ->
                    rowWidthDp = with(localDensity) {
                        coordinates.size.width.toDp()
                    }
                },
        ) {
            statusStats.forEach { statusStat ->
                statusStat?.let {
                    AniLibStatusBarItem(
                        statusName = it.name,
                        statusValue = it.value,
                        statusBarWidthDp = rowWidthDp,
                        totalStatusStats = totalStatusStats
                    )
                }
            }
        }
        Spacer(modifier = Modifier.padding(4.dp))
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            statusStats.forEach { statusStat ->
                statusStat?.let {
                    AniLibStatusBarLegendItem(
                        statusName = it.name,
                        statusValue = it.value
                    )
                }
            }
        }
    }
}

@Composable
private fun AniLibStatusBarItem(
    statusName: String,
    statusValue: Int,
    statusBarWidthDp: Dp,
    totalStatusStats: Int
) {
    val isDarkMode = isSystemInDarkTheme()

    Card(
        modifier = Modifier
            .height(32.dp)
            .width(statusBarWidthDp * statusValue / totalStatusStats),
        shape = when (statusName) {
            PLANNED_ENG, PLANNED_RU -> RoundedCornerShape(
                topStart = 8.dp,
                bottomStart = 8.dp
            )

            ON_HOLD_ENG, ON_HOLD_RU -> RoundedCornerShape(
                topEnd = 8.dp,
                bottomEnd = 8.dp
            )

            else -> RoundedCornerShape(0.dp)
        },
        colors = CardDefaults.cardColors(
            containerColor = when (statusName) {
                PLANNED_ENG, PLANNED_RU -> when (isDarkMode) {
                    true -> DarkDefaultColorScheme.primary
                    false -> LightDefaultColorScheme.primary
                }

                COMPLETED_ENG, COMPLETED_RU -> when (isDarkMode) {
                    true -> DarkAndroidColorScheme.primary
                    false -> LightAndroidColorScheme.primary
                }

                WATCHING_ENG, WATCHING_RU -> when (isDarkMode) {
                    true -> DarkDefaultColorScheme.tertiaryContainer
                    false -> LightDefaultColorScheme.tertiaryContainer
                }

                DROPPED_ENG, DROPPED_RU -> when (isDarkMode) {
                    true -> DarkDefaultColorScheme.error
                    false -> LightDefaultColorScheme.error
                }

                ON_HOLD_ENG, ON_HOLD_RU -> when (isDarkMode) {
                    true -> DarkAndroidColorScheme.tertiary
                    false -> LightAndroidColorScheme.tertiary
                }

                else -> when (isDarkMode) {
                    true -> DarkAndroidColorScheme.tertiary
                    false -> LightAndroidColorScheme.tertiary
                }
            }
        )
    ) {}
}

@Composable
private fun AniLibStatusBarLegendItem(
    statusName: String,
    statusValue: Int
) {
    val isDarkMode = isSystemInDarkTheme()

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Card(
            modifier = Modifier.size(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = when (statusName) {
                    PLANNED_ENG, PLANNED_RU -> when (isDarkMode) {
                        true -> DarkDefaultColorScheme.primary
                        false -> LightDefaultColorScheme.primary
                    }

                    COMPLETED_ENG, COMPLETED_RU -> when (isDarkMode) {
                        true -> DarkAndroidColorScheme.primary
                        false -> LightAndroidColorScheme.primary
                    }

                    WATCHING_ENG, WATCHING_RU -> when (isDarkMode) {
                        true -> DarkDefaultColorScheme.tertiaryContainer
                        false -> LightDefaultColorScheme.tertiaryContainer
                    }

                    DROPPED_ENG, DROPPED_RU -> when (isDarkMode) {
                        true -> DarkDefaultColorScheme.error
                        false -> LightDefaultColorScheme.error
                    }

                    ON_HOLD_ENG, ON_HOLD_RU -> when (isDarkMode) {
                        true -> DarkAndroidColorScheme.tertiary
                        false -> LightAndroidColorScheme.tertiary
                    }

                    else -> when (isDarkMode) {
                        true -> DarkAndroidColorScheme.tertiary
                        false -> LightAndroidColorScheme.tertiary
                    }
                }
            )
        ) {}
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = stringResource(id = R.string.status_legend_name, statusName),
                style = AniLibTypography.titleSmall
            )
            Text(
                text = "$statusValue",
                style = AniLibTypography.bodyMedium
            )
        }
    }
}
