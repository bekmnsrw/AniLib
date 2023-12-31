package com.bekmnsrw.core.widget

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import com.bekmnsrw.core.designsystem.icon.AniLibIcons

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AniLibTopBarWithSearch(
    scrollBehavior: TopAppBarScrollBehavior,
    title: String,
    onSearchIconClicked: () -> Unit
) = TopAppBar(
    scrollBehavior = scrollBehavior,
    title = { Text(text = title) },
    actions = {
        AniLibIconButton(
            onClick = onSearchIconClicked,
            imageVector = AniLibIcons.Search
        )
    }
)
