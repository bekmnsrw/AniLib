package com.bekmnsrw.feature.favorites.impl.presentation.favorites

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.mutableIntStateOf
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.bekmnsrw.feature.auth.api.usecase.local.GetUserIdUseCase
import com.bekmnsrw.feature.favorites.api.model.FavoriteAnime
import com.bekmnsrw.feature.favorites.api.usecase.GetUserFavoritesUseCase
import com.bekmnsrw.feature.favorites.impl.presentation.favorites.FavoritesScreenModel.FavoritesScreenAction.NavigateDetails
import com.bekmnsrw.feature.favorites.impl.presentation.favorites.FavoritesScreenModel.FavoritesScreenAction.ShowSnackbar
import com.bekmnsrw.feature.favorites.impl.presentation.favorites.FavoritesScreenModel.FavoritesScreenEvent.OnCardPress
import com.bekmnsrw.feature.favorites.impl.presentation.favorites.FavoritesScreenModel.FavoritesScreenEvent.OnIconFavoriteClick
import com.bekmnsrw.feature.favorites.impl.presentation.favorites.FavoritesScreenModel.FavoritesScreenEvent.OnInit
import com.bekmnsrw.feature.favorites.impl.presentation.favorites.FavoritesScreenModel.FavoritesScreenEvent.OnItemClick
import com.bekmnsrw.feature.favorites.impl.presentation.favorites.FavoritesScreenModel.FavoritesScreenEvent.OnRefresh
import com.bekmnsrw.feature.home.api.usecase.RemoveFromFavoritesUseCase
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
    private val getUserFavoritesUseCase: GetUserFavoritesUseCase,
    private val removeFromFavoritesUseCase: RemoveFromFavoritesUseCase,
    private val getUserIdUseCase: GetUserIdUseCase
) : ScreenModel {

    private companion object {
        const val ERROR_MESSAGE = "Oops, something went wrong. Please try again!"
        const val TYPE = "Anime"
    }

    private val userId by lazy { mutableIntStateOf(0) }

    private val _screenState = MutableStateFlow(FavoritesScreenState())
    val screenState: StateFlow<FavoritesScreenState> = _screenState.asStateFlow()

    private val _screenAction = MutableSharedFlow<FavoritesScreenAction?>()
    val screenAction: SharedFlow<FavoritesScreenAction?> = _screenAction.asSharedFlow()

    @Immutable
    internal data class FavoritesScreenState(
        val refreshing: Boolean = false,
        val favorites: PersistentList<FavoriteAnime> = persistentListOf()
    )

    @Immutable
    internal sealed interface FavoritesScreenEvent {
        data object OnInit : FavoritesScreenEvent
        data object OnRefresh : FavoritesScreenEvent
        data class OnItemClick(val id: Int) : FavoritesScreenEvent
        data class OnIconFavoriteClick(val id: Int) : FavoritesScreenEvent
        data class OnCardPress(val id: Int) : FavoritesScreenEvent
    }

    @Immutable
    internal sealed interface FavoritesScreenAction {
        data class NavigateDetails(val id: Int) : FavoritesScreenAction
        data class ShowSnackbar(val message: String) : FavoritesScreenAction
    }

    init {
        eventHandler(OnInit)
    }

    fun eventHandler(event: FavoritesScreenEvent) {
        when (event) {
            OnInit -> onInit()
            OnRefresh -> onRefresh()
            is OnItemClick -> onItemClick(event.id)
            is OnCardPress -> onCardPress(event.id)
            is OnIconFavoriteClick -> onIconFavoriteClick(event.id)
        }
    }

    private fun onRefresh() = screenModelScope.launch {
        getUserFavoritesUseCase(userId.intValue)
            .flowOn(Dispatchers.IO)
            .onStart {
                _screenState.emit(
                    _screenState.value.copy(
                        refreshing = true
                    )
                )
            }
            .onCompletion {
                _screenState.emit(
                    _screenState.value.copy(
                        refreshing = false
                    )
                )
            }
            .collect {
                if (!_screenState.value.favorites.containsAll(it)) {
                    _screenState.emit(
                        _screenState.value.copy(
                            favorites = it.toPersistentList()
                        )
                    )
                }
            }
    }

    private fun onInit() = screenModelScope.launch {
        getUserIdUseCase()
            .flowOn(Dispatchers.IO)
            .collect { id ->
                if (id != null) {
                    userId.intValue = id
                    getUserFavoritesUseCase(id)
                        .flowOn(Dispatchers.IO)
                        .onStart {
                            _screenState.emit(
                                _screenState.value.copy(
                                    refreshing = true
                                )
                            )
                        }
                        .onCompletion {
                            _screenState.emit(
                                _screenState.value.copy(
                                    refreshing = false
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
            }
    }

    private fun onItemClick(id: Int) = screenModelScope.launch {
        _screenAction.emit(NavigateDetails(id = id))
    }

    private fun onCardPress(id: Int) = screenModelScope.launch {}

    private fun deleteFromFavorites(id: Int) = screenModelScope.launch {
        removeFromFavoritesUseCase(type = TYPE, id = id)
            .flowOn(Dispatchers.IO)
            .collect { response ->
                when (response.success) {
                    true -> {
                        _screenState.emit(
                            _screenState.value.copy(
                                favorites = _screenState.value.favorites.removeAll {
                                    it.id == id
                                }
                            )
                        )
                    }

                    false -> _screenAction.emit(ShowSnackbar(message = ERROR_MESSAGE))
                }
            }
    }

    private fun onIconFavoriteClick(id: Int) = screenModelScope.launch {
        deleteFromFavorites(id = id)
    }
}
