package com.bekmnsrw.feature.favorites.impl.presentation.favorites

import androidx.compose.runtime.Immutable
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.bekmnsrw.feature.favorites.api.model.FavoriteAnime
import com.bekmnsrw.feature.favorites.api.usecase.GetUserFavoritesUseCase
import com.bekmnsrw.feature.favorites.impl.presentation.favorites.FavoritesScreenModel.FavoritesScreenAction.*
import com.bekmnsrw.feature.favorites.impl.presentation.favorites.FavoritesScreenModel.FavoritesScreenEvent.OnInit
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

internal class FavoritesScreenModel(
    private val getUserFavoritesUseCase: GetUserFavoritesUseCase
) : ScreenModel {

    private val _screenState = MutableStateFlow(FavoritesScreenState())
    val screenState: StateFlow<FavoritesScreenState> = _screenState.asStateFlow()

    private val _screenAction = MutableSharedFlow<FavoritesScreenAction?>()
    val screenAction: SharedFlow<FavoritesScreenAction?> = _screenAction.asSharedFlow()

    @Immutable
    internal data class FavoritesScreenState(
        val isLoading: Boolean = false,
        val favorites: PersistentList<FavoriteAnime> = persistentListOf()
    )

    @Immutable
    internal sealed interface FavoritesScreenEvent {
        data object OnInit : FavoritesScreenEvent
        data class OnItemClicked(val id: Int) : FavoritesScreenEvent
    }

    @Immutable
    internal sealed interface FavoritesScreenAction {
        data class NavigateDetails(val id: Int) : FavoritesScreenAction
    }

    init {
        eventHandler(OnInit)
    }

    fun eventHandler(event: FavoritesScreenEvent) {
        when (event) {
            OnInit -> onInit()

            is FavoritesScreenEvent.OnItemClicked -> onItemClicked(event.id)
        }
    }

    private fun onInit() = screenModelScope.launch {
        getUserFavoritesUseCase(1_379_176)
            .flowOn(Dispatchers.IO)
            .onStart {
                _screenState.emit(
                    _screenState.value.copy(
                        isLoading = true
                    )
                )
            }
            .onCompletion {
                _screenState.emit(
                    _screenState.value.copy(
                        isLoading = false
                    )
                )
            }
            .collect {
                _screenState.emit(
                    _screenState.value.copy(
                        favorites = it.toPersistentList()
                    )
                )
            }
    }

    private fun onItemClicked(id: Int) = screenModelScope.launch {
        _screenAction.emit(
            NavigateDetails(
                id = id
            )
        )
    }
}
