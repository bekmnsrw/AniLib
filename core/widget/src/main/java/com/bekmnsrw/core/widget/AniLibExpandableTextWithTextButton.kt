package com.bekmnsrw.core.widget

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import com.bekmnsrw.core.designsystem.theme.AniLibTypography

@Composable
fun AniLibExpandableTextWithTextButton(
    modifier: Modifier = Modifier,
    text: String,
    isExpanded: Boolean,
    onDescriptionButtonClicked: () -> Unit
) {
    Column(modifier = modifier) {
        Text(
            text = text,
            style = AniLibTypography.bodyLarge,
            maxLines = if (isExpanded) Int.MAX_VALUE else 3,
            overflow = TextOverflow.Ellipsis
        )
        Box(modifier = Modifier.fillMaxWidth()) {
            TextButton(
                onClick = onDescriptionButtonClicked,
                modifier = Modifier.align(Alignment.Center)
            ) {
                Text(
                    text = if (isExpanded)
                        stringResource(id = R.string.hide)
                    else
                        stringResource(id = R.string.show_more)
                )
            }
        }
    }
}
