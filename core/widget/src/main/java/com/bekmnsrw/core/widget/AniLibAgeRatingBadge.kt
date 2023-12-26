package com.bekmnsrw.core.widget

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

private const val NO_RATING = "none"
private const val ALL_AGES = "g"
private const val CHILDREN = "pg"
private const val TWEEN_OR_OLDER = "pg_13"
private const val VIOLENCE_AND_PROFANITY = "r"
private const val MILD_NUDITY = "r_plus"
private const val HENTAI = "rx"

@Composable
fun AniLibAgeRatingBadge(
    rating: String
) {
    Card {
        Text(
            modifier = Modifier.padding(4.dp),
            text = when (rating) {
                NO_RATING, ALL_AGES, CHILDREN -> stringResource(id = R.string.all_ages_rating)
                TWEEN_OR_OLDER -> stringResource(id = R.string.tween_rating)
                VIOLENCE_AND_PROFANITY, MILD_NUDITY -> stringResource(id = R.string.teens_rating)
                HENTAI -> stringResource(id = R.string.adult_rating)
                else -> ""
            }
        )
    }
}
