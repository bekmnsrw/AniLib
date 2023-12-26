package com.bekmnsrw.feature.favorites.impl.presentation.dropped

import androidx.compose.runtime.Immutable
import androidx.paging.PagingData
import androidx.paging.cachedIn
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.bekmnsrw.feature.auth.api.usecase.local.GetUserIdUseCase
import com.bekmnsrw.feature.favorites.api.model.UserRate
import com.bekmnsrw.feature.favorites.api.repository.FavoritesRepository
import com.bekmnsrw.feature.favorites.impl.UserRatesEnum
import com.bekmnsrw.feature.favorites.impl.presentation.dropped.DroppedScreenModel.DroppedScreenAction.*
import com.bekmnsrw.feature.favorites.impl.presentation.dropped.DroppedScreenModel.DroppedScreenEvent.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

internal class DroppedScreenModel(
    private val favoritesRepository: FavoritesRepository,
    private val getUserIdUseCase: GetUserIdUseCase
) : ScreenModel {

    private val _dropped: MutableStateFlow<PagingData<UserRate>> = MutableStateFlow(PagingData.empty())
    val dropped: StateFlow<PagingData<UserRate>> = _dropped.asStateFlow()

    private val _screenAction = MutableSharedFlow<DroppedScreenAction?>()
    val screenAction: SharedFlow<DroppedScreenAction?> = _screenAction.asSharedFlow()

    init {
        eventHandler(OnInit)
    }

    @Immutable
    internal sealed interface DroppedScreenEvent {
        data object OnInit : DroppedScreenEvent
        data class OnItemClicked(val id: Int) : DroppedScreenEvent
    }

    @Immutable
    internal sealed interface DroppedScreenAction {
        data class NavigateDetails(val id: Int) : DroppedScreenAction
    }

    fun eventHandler(event: DroppedScreenEvent) {
        when (event) {
            OnInit -> onInit()

            is OnItemClicked -> onItemClicked(event.id)
        }
    }

    private fun onInit() = screenModelScope.launch {
        favoritesRepository.getPlannedPaged(1_379_176, UserRatesEnum.DROPPED.status)
            .flowOn(Dispatchers.IO)
            .cachedIn(screenModelScope)
            .collect { data -> _dropped.value = data }
    }

    private fun onItemClicked(id: Int) = screenModelScope.launch {
        _screenAction.emit(
            NavigateDetails(
                id = id
            )
        )
    }
}
