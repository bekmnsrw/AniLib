package com.bekmnsrw.feature.favorites.impl.presentation.completed

import androidx.compose.runtime.Immutable
import androidx.paging.PagingData
import androidx.paging.cachedIn
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.bekmnsrw.feature.auth.api.usecase.local.GetUserIdUseCase
import com.bekmnsrw.feature.favorites.api.model.UserRate
import com.bekmnsrw.feature.favorites.api.repository.FavoritesRepository
import com.bekmnsrw.feature.favorites.impl.UserRatesEnum
import com.bekmnsrw.feature.favorites.impl.presentation.completed.CompletedScreenModel.CompletedScreenAction.*
import com.bekmnsrw.feature.favorites.impl.presentation.completed.CompletedScreenModel.CompletedScreenEvent.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

internal class CompletedScreenModel(
    private val favoritesRepository: FavoritesRepository,
    private val getUserIdUseCase: GetUserIdUseCase
) : ScreenModel {

    private val _completed: MutableStateFlow<PagingData<UserRate>> = MutableStateFlow(PagingData.empty())
    val completed: StateFlow<PagingData<UserRate>> = _completed.asStateFlow()

    private val _screenAction = MutableSharedFlow<CompletedScreenAction?>()
    val screenAction: SharedFlow<CompletedScreenAction?> = _screenAction.asSharedFlow()

    init {
        eventHandler(OnInit)
    }

    @Immutable
    internal sealed interface CompletedScreenEvent {
        data object OnInit : CompletedScreenEvent
        data class OnItemClicked(val id: Int) : CompletedScreenEvent
    }

    @Immutable
    internal sealed interface CompletedScreenAction {
        data class NavigateDetails(val id: Int) : CompletedScreenAction
    }

    fun eventHandler(event: CompletedScreenEvent) {
        when (event) {
            OnInit -> onInit()

            is OnItemClicked -> onItemClicked(event.id)
        }
    }

    private fun onInit() = screenModelScope.launch {
        favoritesRepository.getPlannedPaged(1_379_176, UserRatesEnum.COMPLETED.status)
            .flowOn(Dispatchers.IO)
            .cachedIn(screenModelScope)
            .collect { data -> _completed.value = data }
    }

    private fun onItemClicked(id: Int) = screenModelScope.launch {
        _screenAction.emit(
            NavigateDetails(
                id = id
            )
        )
    }
}
