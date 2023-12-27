package com.bekmnsrw.feature.favorites.impl.presentation.watching

import androidx.compose.runtime.Immutable
import androidx.paging.PagingData
import androidx.paging.cachedIn
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.bekmnsrw.feature.auth.api.usecase.local.GetUserIdUseCase
import com.bekmnsrw.feature.favorites.api.model.UserRates
import com.bekmnsrw.feature.favorites.api.repository.FavoritesRepository
import com.bekmnsrw.feature.favorites.impl.UserRatesEnum
import com.bekmnsrw.feature.favorites.impl.presentation.watching.WatchingScreenModel.WatchingScreenAction.*
import com.bekmnsrw.feature.favorites.impl.presentation.watching.WatchingScreenModel.WatchingScreenEvent.OnInit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

internal class WatchingScreenModel(
    private val favoritesRepository: FavoritesRepository,
    private val getUserIdUseCase: GetUserIdUseCase
) : ScreenModel {

    private val _screenState = MutableStateFlow(WatchingScreenState())
    val screenState: StateFlow<WatchingScreenState> = _screenState.asStateFlow()

    private val _screenAction = MutableSharedFlow<WatchingScreenAction?>()
    val screenAction: SharedFlow<WatchingScreenAction?> = _screenAction.asSharedFlow()

    private val _watching = MutableStateFlow<PagingData<UserRates>>(PagingData.empty())
    val watching: StateFlow<PagingData<UserRates>> = _watching.asStateFlow()

    init {
        eventHandler(OnInit)
    }

    @Immutable
    internal data class WatchingScreenState(
        val shouldShowModalBottomSheet: Boolean = false,
        val selectedItemIndex: Int = 0
    )

    @Immutable
    internal sealed interface WatchingScreenEvent {
        data object OnInit : WatchingScreenEvent
        data class OnItemClicked(val id: Int) : WatchingScreenEvent
        data object OnModalBottomSheetDismissRequest : WatchingScreenEvent
        data class OnLongPress(val index: Int) : WatchingScreenEvent
    }

    @Immutable
    internal sealed interface WatchingScreenAction {
        data class NavigateDetails(val id: Int) : WatchingScreenAction
    }

    fun eventHandler(event: WatchingScreenEvent) {
        when (event) {
            OnInit -> onInit()

            is WatchingScreenEvent.OnItemClicked -> onItemClicked(event.id)

            is WatchingScreenEvent.OnLongPress -> onLongPress(event.index)

            WatchingScreenEvent.OnModalBottomSheetDismissRequest -> onModalBottomSheetDismissRequest()
        }
    }

    private fun onInit() = screenModelScope.launch {
        favoritesRepository.getPlannedPaged(1_379_176, UserRatesEnum.WATCHING.key)
            .flowOn(Dispatchers.IO)
            .cachedIn(screenModelScope)
            .collect { data -> _watching.value = data }
    }

    private fun onItemClicked(id: Int) = screenModelScope.launch {
        _screenAction.emit(NavigateDetails(id = id))
    }

    private fun onModalBottomSheetDismissRequest() = screenModelScope.launch {
        _screenState.emit(
            _screenState.value.copy(
                shouldShowModalBottomSheet = false
            )
        )
    }

    private fun onLongPress(index: Int) = screenModelScope.launch {
        _screenState.emit(
            _screenState.value.copy(
                shouldShowModalBottomSheet = true,
                selectedItemIndex = index
            )
        )
    }
}
