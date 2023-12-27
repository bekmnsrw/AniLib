package com.bekmnsrw.feature.favorites.impl.presentation.onhold

import androidx.compose.runtime.Immutable
import androidx.paging.PagingData
import androidx.paging.cachedIn
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.bekmnsrw.feature.auth.api.usecase.local.GetUserIdUseCase
import com.bekmnsrw.feature.favorites.api.model.UserRates
import com.bekmnsrw.feature.favorites.api.repository.FavoritesRepository
import com.bekmnsrw.feature.favorites.impl.UserRatesEnum
import com.bekmnsrw.feature.favorites.impl.presentation.onhold.OnHoldScreenModel.OnHoldScreenAction.*
import com.bekmnsrw.feature.favorites.impl.presentation.onhold.OnHoldScreenModel.OnHoldScreenEvent.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

internal class OnHoldScreenModel(
    private val favoritesRepository: FavoritesRepository,
    private val getUserIdUseCase: GetUserIdUseCase
) : ScreenModel {

    private val _screenState = MutableStateFlow(OnHoldScreenState())
    val screenState: StateFlow<OnHoldScreenState> = _screenState.asStateFlow()

    private val _screenAction = MutableSharedFlow<OnHoldScreenAction?>()
    val screenAction: SharedFlow<OnHoldScreenAction?> = _screenAction.asSharedFlow()

    private val _onHold: MutableStateFlow<PagingData<UserRates>> = MutableStateFlow(PagingData.empty())
    val onHold: StateFlow<PagingData<UserRates>> = _onHold.asStateFlow()

    init {
        eventHandler(OnInit)
    }

    @Immutable
    internal data class OnHoldScreenState(
        val shouldShowModalBottomSheet: Boolean = false,
        val selectedItemIndex: Int = 0
    )

    @Immutable
    internal sealed interface OnHoldScreenEvent {
        data object OnInit : OnHoldScreenEvent
        data class OnItemClicked(val id: Int) : OnHoldScreenEvent
        data object OnModalBottomSheetDismissRequest : OnHoldScreenEvent
        data class OnLongPress(val index: Int) : OnHoldScreenEvent
    }

    @Immutable
    internal sealed interface OnHoldScreenAction {
        data class NavigateDetails(val id: Int) : OnHoldScreenAction
    }

    fun eventHandler(event: OnHoldScreenEvent) {
        when (event) {
             OnInit -> onInit()

            is OnItemClicked -> onItemScreen(event.id)

            is OnLongPress -> onLongPress(event.index)

            OnModalBottomSheetDismissRequest -> onModalBottomSheetDismissRequest()
        }
    }

    private fun onInit() = screenModelScope.launch {
        favoritesRepository.getPlannedPaged(1_379_176, UserRatesEnum.ON_HOLD.key)
            .flowOn(Dispatchers.IO)
            .cachedIn(screenModelScope)
            .collect { data -> _onHold.value = data }
    }

    private fun onItemScreen(id: Int) = screenModelScope.launch {
        _screenAction.emit(
            NavigateDetails(
                id = id
            )
        )
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
