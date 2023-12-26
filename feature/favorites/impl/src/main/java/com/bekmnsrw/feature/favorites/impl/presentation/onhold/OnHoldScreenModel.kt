package com.bekmnsrw.feature.favorites.impl.presentation.onhold

import androidx.compose.runtime.Immutable
import androidx.paging.PagingData
import androidx.paging.cachedIn
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.bekmnsrw.feature.auth.api.usecase.local.GetUserIdUseCase
import com.bekmnsrw.feature.favorites.api.model.UserRate
import com.bekmnsrw.feature.favorites.api.repository.FavoritesRepository
import com.bekmnsrw.feature.favorites.impl.UserRatesEnum
import com.bekmnsrw.feature.favorites.impl.presentation.onhold.OnHoldScreenModel.OnHoldScreenAction.*
import com.bekmnsrw.feature.favorites.impl.presentation.onhold.OnHoldScreenModel.OnHoldScreenEvent.OnInit
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

    private val _onHold: MutableStateFlow<PagingData<UserRate>> = MutableStateFlow(PagingData.empty())
    val onHold: StateFlow<PagingData<UserRate>> = _onHold.asStateFlow()

    private val _screenAction = MutableSharedFlow<OnHoldScreenAction?>()
    val screenAction: SharedFlow<OnHoldScreenAction?> = _screenAction.asSharedFlow()

    init {
        eventHandler(OnInit)
    }

    @Immutable
    internal sealed interface OnHoldScreenEvent {
        data object OnInit : OnHoldScreenEvent
        data class OnItemClicked(val id: Int) : OnHoldScreenEvent
    }

    @Immutable
    internal sealed interface OnHoldScreenAction {
        data class NavigateDetails(val id: Int) : OnHoldScreenAction
    }

    fun eventHandler(event: OnHoldScreenEvent) {
        when (event) {
             OnInit -> onInit()

            is OnHoldScreenEvent.OnItemClicked -> onItemScreen(event.id)
        }
    }

    private fun onInit() = screenModelScope.launch {
        favoritesRepository.getPlannedPaged(1_379_176, UserRatesEnum.ON_HOLD.status)
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
}
