package com.bekmnsrw.feature.home.impl.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.bekmnsrw.feature.home.impl.presentation.home.HomeScreen

object HomeTab : Tab {

    override val options: TabOptions
        @Composable
        get() = remember {
            TabOptions(
                index = 1u,
                title = "Home",
                icon = null
            )
        }

    @Composable
    override fun Content() {
        Navigator(screen = HomeScreen())
//        { navigator ->
//            SlideTransition(navigator = navigator)
//        }
    }
}
