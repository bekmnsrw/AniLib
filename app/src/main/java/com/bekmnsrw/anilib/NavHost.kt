package com.bekmnsrw.anilib

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import cafe.adriel.voyager.core.annotation.ExperimentalVoyagerApi
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabDisposable
import cafe.adriel.voyager.navigator.tab.TabNavigator
import com.bekmnsrw.core.designsystem.icon.AniLibIcons
import com.bekmnsrw.core.designsystem.theme.LocalBackgroundTheme
import com.bekmnsrw.feature.auth.impl.presentation.AuthTab
import com.bekmnsrw.feature.favorites.impl.presentation.FavoritesTab
import com.bekmnsrw.feature.home.impl.presentation.HomeTab
import com.bekmnsrw.feature.profile.impl.presentation.ProfileTab

@OptIn(ExperimentalVoyagerApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun NavHost() {
    TabNavigator(
        tab = HomeTab,
        tabDisposable = {
            TabDisposable(
                navigator = it,
                tabs = listOf(
                    HomeTab,
                    FavoritesTab,
                    ProfileTab,
                    AuthTab
                )
            )
        }
    ) {
        Scaffold(
            content = { CurrentTab() },
            bottomBar = {
                BottomNavigation(backgroundColor = LocalBackgroundTheme.current.color) {
                    TabNavigationItem(
                        tab = HomeTab,
                        filledIcon = AniLibIcons.HomeFilled,
                        outlinedIcon = AniLibIcons.HomeOutlined
                    )
                    TabNavigationItem(
                        tab = FavoritesTab,
                        filledIcon = AniLibIcons.FavoritesFilled,
                        outlinedIcon = AniLibIcons.FavoritesOutlined
                    )
                    TabNavigationItem(
                        tab = ProfileTab,
                        filledIcon = AniLibIcons.ProfileFilled,
                        outlinedIcon = AniLibIcons.ProfileOutlined
                    )
                }
            }
        )
    }
}

@Composable
fun RowScope.TabNavigationItem(
    tab: Tab,
    filledIcon: ImageVector,
    outlinedIcon: ImageVector
) {
    val tabNavigator = LocalTabNavigator.current
    val isSelected = tabNavigator.current == tab

    BottomNavigationItem(
        selected = isSelected,
        onClick = { tabNavigator.current = tab },
        icon = {
            Icon(
                imageVector = when (isSelected) {
                    true -> filledIcon
                    false -> outlinedIcon
                },
                contentDescription = tab.options.title
            )
        },
        selectedContentColor = MaterialTheme.colorScheme.primary,
        unselectedContentColor = MaterialTheme.colorScheme.onBackground
    )
}
