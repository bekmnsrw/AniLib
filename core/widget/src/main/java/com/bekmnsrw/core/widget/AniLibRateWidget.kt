package com.bekmnsrw.core.widget

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.unit.dp
import com.bekmnsrw.core.designsystem.theme.AniLibTypography
import com.bekmnsrw.feature.home.api.model.RatesScoresStat

@Composable
fun AniLibRateWidget(
    rate: String,
    rateStats: List<RatesScoresStat?>,
    totalRateStats: Int
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = stringResource(id = R.string.rating),
            style = AniLibTypography.titleMedium
        )
        Spacer(modifier = Modifier.padding(4.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = rate,
                    style = AniLibTypography.displaySmall
                )
                Text(
                    text = stringResource(id = R.string.number_of_rates, totalRateStats),
                    style = AniLibTypography.bodyLarge
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                rateStats.forEach { rateStat ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        rateStat?.let {
                            Text(text = if (it.name == 10) "${it.name} " else " ${it.name}  ")
                            AniLibRateBar(
                                totalRateStats = totalRateStats,
                                currentRateStat = it.value
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AniLibRateBar(
    totalRateStats: Int,
    currentRateStat: Int
) {
    val localDensity = LocalDensity.current
    var cardWidthDp by remember { mutableStateOf(0.dp) }

    Box(modifier = Modifier.fillMaxWidth()) {
        Row {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(12.dp)
                    .onGloballyPositioned { coordinates ->
                        cardWidthDp = with(localDensity) { coordinates.size.width.toDp() }
                    }
            ) {}
        }
        Row {
            Card(
                modifier = Modifier
                    .width(cardWidthDp * currentRateStat / totalRateStats + 8.dp)
                    .height(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {}
        }
    }
}
