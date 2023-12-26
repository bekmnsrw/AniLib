package com.bekmnsrw.feature.favorites.impl.presentation.container

import androidx.compose.runtime.Immutable
import cafe.adriel.voyager.core.model.ScreenModel
import com.bekmnsrw.feature.favorites.impl.presentation.TabItem
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

internal class FavoritesTabsContainerScreenModel : ScreenModel {

    @Immutable
    internal data class FavoritesScreenState(
        val tabs: PersistentList<TabItem> = persistentListOf(
            TabItem.Favorites,
            TabItem.PlannedToWatch,
            TabItem.Completed,
            TabItem.Watching,
            TabItem.Dropped,
            TabItem.OnHold
        )
    )

    private val _screenState = MutableStateFlow(FavoritesScreenState())
    val screenState: StateFlow<FavoritesScreenState> = _screenState.asStateFlow()
}
