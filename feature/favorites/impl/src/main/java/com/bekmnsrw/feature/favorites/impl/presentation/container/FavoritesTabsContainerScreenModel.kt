package com.bekmnsrw.feature.favorites.impl.presentation.container

import androidx.compose.runtime.Immutable
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.bekmnsrw.feature.auth.api.usecase.local.IsAuthenticatedUseCase
import com.bekmnsrw.feature.auth.api.usecase.local.SaveUserIdUseCase
import com.bekmnsrw.feature.favorites.impl.presentation.container.FavoritesTabsContainerScreenModel.FavoritesTabsContainerScreenAction.*
import com.bekmnsrw.feature.favorites.impl.presentation.container.FavoritesTabsContainerScreenModel.FavoritesTabsContainerScreenEvent.*
import com.bekmnsrw.feature.profile.api.usecase.GetProfileUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

internal class FavoritesTabsContainerScreenModel(
    private val isAuthenticatedUseCase: IsAuthenticatedUseCase,
    private val getProfileUseCase: GetProfileUseCase,
    private val saveUserIdUseCase: SaveUserIdUseCase,
) : ScreenModel {

    @Immutable
    internal data class FavoritesTabsContainerScreenState(
        val isLoading: Boolean = false,
        val isFirstVisit: Boolean = true
    )

    @Immutable
    internal sealed interface FavoritesTabsContainerScreenEvent {
        data object OnInit : FavoritesTabsContainerScreenEvent
        data object OnStart : FavoritesTabsContainerScreenEvent
    }

    @Immutable
    internal sealed interface FavoritesTabsContainerScreenAction {
        data object NavigateAuthScreen : FavoritesTabsContainerScreenAction
        data object NavigateFavoritesScreen : FavoritesTabsContainerScreenAction
    }

    private val _screenState = MutableStateFlow(FavoritesTabsContainerScreenState())
    val screenState: StateFlow<FavoritesTabsContainerScreenState> = _screenState.asStateFlow()

    private val _screenAction = MutableSharedFlow<FavoritesTabsContainerScreenAction?>()
    val screenAction: SharedFlow<FavoritesTabsContainerScreenAction?> = _screenAction.asSharedFlow()

    init {
        eventHandler(OnInit)
    }

    fun eventHandler(event: FavoritesTabsContainerScreenEvent) {
        when (event) {
            OnInit -> onInit()
            OnStart -> onStart()
        }
    }

    private fun onStart() = screenModelScope.launch {
        _screenState.emit(
            _screenState.value.copy(
                isLoading = true
            )
        )

        val isAuthenticated = isAuthenticatedUseCase()
            .flowOn(Dispatchers.IO)
            .first()

        when (isAuthenticated) {
            false, null -> { _screenAction.emit(NavigateAuthScreen) }
            else -> {
                if (_screenState.value.isFirstVisit) {
                    getProfileUseCase()
                        .flowOn(Dispatchers.IO)
                        .collect {
                            saveUserIdUseCase(id = it.id)
                            _screenAction.emit(NavigateFavoritesScreen)
                        }
                    _screenState.emit(
                        _screenState.value.copy(
                            isFirstVisit = false
                        )
                    )
                }
            }
        }

        _screenState.emit(
            _screenState.value.copy(
                isLoading = false
            )
        )
    }

    private fun onInit() = screenModelScope.launch {
        _screenState.emit(
            _screenState.value.copy(
                isLoading = true
            )
        )

        val isAuthenticated = isAuthenticatedUseCase()
            .flowOn(Dispatchers.IO)
            .first()

        when (isAuthenticated) {
            false, null -> { _screenAction.emit(NavigateAuthScreen) }
            else -> {}
        }

        _screenState.emit(
            _screenState.value.copy(
                isLoading = false
            )
        )
    }
}
