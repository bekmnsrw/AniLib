package com.bekmnsrw.anilib

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.annotation.ExperimentalVoyagerApi
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabDisposable
import cafe.adriel.voyager.navigator.tab.TabNavigator
import com.bekmnsrw.core.designsystem.theme.BackgroundTheme
import com.bekmnsrw.core.designsystem.theme.LocalBackgroundTheme
import com.bekmnsrw.core.designsystem.theme.LocalTintTheme
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
                BottomNavigation(
                    backgroundColor = LocalBackgroundTheme.current.color,
                    contentColor = LocalTintTheme.current.iconTint!!
                ) {
                    TabNavigationItem(HomeTab)
                    TabNavigationItem(FavoritesTab)
                    TabNavigationItem(ProfileTab)
                }
            }
        )
    }
}

@Composable
fun RowScope.TabNavigationItem(tab: Tab) {
    val tabNavigator = LocalTabNavigator.current

    BottomNavigationItem(
        selected = tabNavigator.current == tab,
        onClick = { tabNavigator.current = tab },
        icon = { Icon(painter = tab.options.icon!!, contentDescription = tab.options.title) }
    )
}
