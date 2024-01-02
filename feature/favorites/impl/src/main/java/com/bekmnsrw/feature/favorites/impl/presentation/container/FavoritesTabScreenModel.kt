package com.bekmnsrw.feature.favorites.impl.presentation.container

import androidx.compose.runtime.Immutable
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.bekmnsrw.feature.auth.api.usecase.local.IsAuthenticatedUseCase
import com.bekmnsrw.feature.favorites.impl.presentation.container.FavoritesTabScreenModel.FavoritesTabScreenAction.NavigateAuthScreen
import com.bekmnsrw.feature.favorites.impl.presentation.container.FavoritesTabScreenModel.FavoritesTabScreenEvent.OnInit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

internal class FavoritesTabScreenModel(
    private val isAuthenticatedUseCase: IsAuthenticatedUseCase
) : ScreenModel {

    @Immutable
    internal sealed interface FavoritesTabScreenEvent {
        data object OnInit : FavoritesTabScreenEvent
    }

    @Immutable
    internal sealed interface FavoritesTabScreenAction {
        data object NavigateAuthScreen : FavoritesTabScreenAction
    }

    init {
        eventHandler(OnInit)
    }

    private val _screenAction = MutableSharedFlow<FavoritesTabScreenAction?>()
    val screenAction: SharedFlow<FavoritesTabScreenAction?> = _screenAction.asSharedFlow()

    fun eventHandler(event: FavoritesTabScreenEvent) {
        when (event) {
            OnInit -> onInit()
        }
    }

    private fun onInit() = screenModelScope.launch {
        val isAuthenticated = isAuthenticatedUseCase()
            .flowOn(Dispatchers.IO)
            .first()

        when (isAuthenticated) {
            false, null -> { _screenAction.emit(NavigateAuthScreen) }
            else -> {}
        }
    }
}
