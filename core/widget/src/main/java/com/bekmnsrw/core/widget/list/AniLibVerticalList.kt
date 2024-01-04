package com.bekmnsrw.core.widget.list

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.bekmnsrw.core.designsystem.theme.AniLibTypography
import com.bekmnsrw.core.widget.AniLibImage
import com.bekmnsrw.feature.home.api.model.Anime

@Composable
fun AniLibVerticalList(
    contentPadding: PaddingValues,
    animePaged: LazyPagingItems<Anime>,
    onItemClicked: (Int) -> Unit
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(16.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding)
            .padding(bottom = 56.dp)
    ) {
        items(
            count = animePaged.itemCount,
            key = animePaged.itemKey { it.id },
            contentType = animePaged.itemContentType { "AnimePaged" }
        ) { index ->
            animePaged[index]?.let { anime ->
                AniLibVerticalListItem(
                    anime = anime,
                    onItemClicked = onItemClicked
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AniLibVerticalListItem(
    anime: Anime,
    onItemClicked: (Int) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        shape = RoundedCornerShape(8.dp),
        onClick = { onItemClicked(anime.id) }
    ) {
        Row {
            AniLibImage(
                imageUrl = anime.image.original,
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
            AniLibVerticalListItemDescription(anime = anime)
        }
    }
}

@Composable
private fun AniLibVerticalListItemDescription(anime: Anime) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        with(anime) {
            AniLibDescriptionItem(
                value = name,
                style = AniLibTypography.titleMedium,
                maxLines = if (status == "anons") 2 else 1
            )
            AniLibDescriptionItem(
                key = "Kind: ",
                value = kind
            )
            when (status) {
                "ongoing", "released" -> {
                    AniLibDescriptionItem(
                        key = "Rating: ",
                        value = score
                    )
                    AniLibDescriptionItem(
                        key = "Aired on: ",
                        value = airedOn
                    )
                    if (status == "released" && releasedOn.isNotEmpty()) {
                        AniLibDescriptionItem(
                            key = "Released on: ",
                            value = releasedOn
                        )
                    }
                    if (kind != "movie") {
                        AniLibDescriptionItem(
                            key = "Episodes: ",
                            value = when (status) {
                                "ongoing" -> "$episodesAired/$numberOfEpisodes ep"
                                else -> "$numberOfEpisodes ep"
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AniLibDescriptionItem(
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
