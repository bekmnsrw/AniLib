package com.bekmnsrw.core.widget.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowForwardIos
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.offset
import com.bekmnsrw.core.designsystem.theme.AniLibTypography
import com.bekmnsrw.core.widget.AniLibImage
import com.bekmnsrw.feature.home.api.model.Anime
import kotlinx.collections.immutable.PersistentList

@Composable
fun AniLibHorizontalList(
    animeList: PersistentList<Anime>,
    animeListTitle: String,
    onMoreClicked: () -> Unit = {},
    onItemClicked: (Int) -> Unit,
    isMoreEnable: Boolean = true
) {
    val density = LocalDensity.current
    val offsetPx = with(density) { 16.dp.roundToPx() }

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        AniLibHorizontalListHeader(
            title = animeListTitle,
            onMoreClicked = onMoreClicked,
            isMoreEnable = isMoreEnable
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
                AniLibHorizontalListItem(
                    anime = animeItem,
                    onItemClicked = onItemClicked
                )
            }
        }
    }
}

@Composable
private fun AniLibHorizontalListHeader(
    title: String,
    onMoreClicked: () -> Unit,
    isMoreEnable: Boolean
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
            if (isMoreEnable) {
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
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AniLibHorizontalListItem(
    anime: Anime,
    onItemClicked: (Int) -> Unit
) {
    Card(
        modifier = Modifier
            .width(120.dp)
            .wrapContentHeight(),
        shape = RoundedCornerShape(8.dp),
        onClick = { onItemClicked(anime.id) }
    ) {
        Column {
            AniLibImage(
                imageUrl = anime.image.original,
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
            AniLibHorizontalListItemText(
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
private fun AniLibHorizontalListItemText(
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
                        text = if (status == "ongoing")
                            "$episodesAired/$numberOfEpisodes ep"
                        else "$numberOfEpisodes ep",
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
