package com.bekmnsrw.feature.profile.impl.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import cafe.adriel.voyager.transitions.SlideTransition
import com.bekmnsrw.feature.profile.impl.presentation.profile.ProfileScreen

object ProfileTab : Tab {

    override val options: TabOptions
        @Composable
        get() = remember {
            TabOptions(
                index = 2u,
                title = "Profile",
                icon = null
            )
        }

    @Composable
    override fun Content() {
        Navigator(screen = ProfileScreen())
//        { navigator ->
//            SlideTransition(navigator = navigator)
//        }
    }
}
