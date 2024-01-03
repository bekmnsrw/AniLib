package com.bekmnsrw.feature.favorites.impl.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.bekmnsrw.feature.favorites.impl.presentation.container.FavoritesTabsContainer

object FavoritesTab : Tab {

    override val options: TabOptions
        @Composable
        get() = remember {
            TabOptions(
                index = 0u,
                title = "Favorites",
                icon = null
            )
        }

    @Composable
    override fun Content() {
        Navigator(screen = FavoritesTabsContainer())
//        { navigator ->
//            SlideTransition(navigator = navigator)
//        }
    }
}
