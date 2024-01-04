package com.bekmnsrw.core.widget.topbar

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import com.bekmnsrw.core.designsystem.icon.AniLibIcons

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AniLibTopBarWithNavIconFilterAndSearch(
    scrollBehavior: TopAppBarScrollBehavior,
    title: String,
    onNavigationIconClicked: () -> Unit,
    onSearchIconClicked: () -> Unit,
    onFilterClicked: () -> Unit
) = TopAppBar(
    scrollBehavior = scrollBehavior,
    title = { Text(text = title) },
    navigationIcon = {
        IconButton(onClick = onNavigationIconClicked) {
            Icon(
                imageVector = AniLibIcons.ArrowBack,
                contentDescription = null
            )
        }
    },
    actions = {
        IconButton(onClick = onSearchIconClicked) {
            Icon(
                imageVector = AniLibIcons.Search,
                contentDescription = null
            )
        }
        IconButton(onClick = onFilterClicked) {
            Icon(
                imageVector = AniLibIcons.Filter,
                contentDescription = null
            )
        }
    }
)
