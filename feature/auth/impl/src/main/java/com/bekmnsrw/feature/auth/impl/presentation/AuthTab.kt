package com.bekmnsrw.feature.auth.impl.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import org.koin.androidx.compose.getKoin

object AuthTab : Tab {

    override val options: TabOptions
        @Composable
        get() {
            val title = ""

            return remember {
                TabOptions(
                    index = 3u,
                    title = title
                )
            }
        }

    @Composable
    override fun Content() {
        Navigator(screen = AuthScreen())
    }
}
