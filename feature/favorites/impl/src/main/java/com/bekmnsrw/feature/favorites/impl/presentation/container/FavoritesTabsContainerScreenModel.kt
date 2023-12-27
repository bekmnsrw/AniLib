package com.bekmnsrw.feature.favorites.impl.presentation.container

import androidx.compose.runtime.Immutable
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.bekmnsrw.feature.favorites.impl.presentation.container.FavoritesTabsContainerScreenModel.FavoritesTabContainerEvent.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

internal class FavoritesTabsContainerScreenModel : ScreenModel {

    private val _screenState = MutableStateFlow(FavoritesTabContainerState())
    val screenState: StateFlow<FavoritesTabContainerState> = _screenState.asStateFlow()

    @Immutable
    internal data class FavoritesTabContainerState(
        val shouldShowModalBottomSheet: Boolean = false,
    )

    @Immutable
    internal sealed interface FavoritesTabContainerEvent {
        data object OnLongPress : FavoritesTabContainerEvent
        data object OnDismiss : FavoritesTabContainerEvent
    }

    fun eventHandler(event: FavoritesTabContainerEvent) {
        when (event) {
            OnLongPress -> onLongPress()
            OnDismiss -> onDismiss()
        }
    }

    private fun onLongPress() = screenModelScope.launch {
        _screenState.emit(
            _screenState.value.copy(
                shouldShowModalBottomSheet = true
            )
        )
    }

    private fun onDismiss() = screenModelScope.launch {
        _screenState.emit(
            _screenState.value.copy(
                shouldShowModalBottomSheet = false
            )
        )
    }
}
