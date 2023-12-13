package com.bekmnsrw.feature.favorites.impl.presentation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.bekmnsrw.core.designsystem.icon.AniLibIcons

object FavoritesTab : Tab {

    override val options: TabOptions
        @Composable
        get() {
            val title = "Favorites"
            val icon = rememberVectorPainter(AniLibIcons.FavoritesFilled)

            return remember {
                TabOptions(
                    index = 0u,
                    title = title,
                    icon = icon
                )
            }
        }

    @Composable
    override fun Content() {
        Text(text = "FavoritesScreen")
    }
}
