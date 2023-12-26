package com.bekmnsrw.feature.favorites.impl.presentation.planned

import androidx.compose.runtime.Immutable
import androidx.paging.PagingData
import androidx.paging.cachedIn
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.bekmnsrw.feature.auth.api.usecase.local.GetUserIdUseCase
import com.bekmnsrw.feature.favorites.api.model.UserRate
import com.bekmnsrw.feature.favorites.api.repository.FavoritesRepository
import com.bekmnsrw.feature.favorites.impl.UserRatesEnum
import com.bekmnsrw.feature.favorites.impl.presentation.planned.PlannedScreenModel.PlannedScreenAction.NavigateDetails
import com.bekmnsrw.feature.favorites.impl.presentation.planned.PlannedScreenModel.PlannedScreenEvent.OnInit
import com.bekmnsrw.feature.favorites.impl.presentation.planned.PlannedScreenModel.PlannedScreenEvent.OnItemClicked
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

internal class PlannedScreenModel(
    private val favoritesRepository: FavoritesRepository,
    private val getUserIdUseCase: GetUserIdUseCase
) : ScreenModel {

//    private val userId by lazy { getId() }
//
//    private fun getId(): Int {
//        var id: Int? = null
//
//        screenModelScope.launch {
//            id = getUserIdUseCase()
//                .flowOn(Dispatchers.IO)
//                .first()
//        }
//
//        println()
//
//        return id ?: 0
//    }

    private val _planned: MutableStateFlow<PagingData<UserRate>> = MutableStateFlow(PagingData.empty())
    val planned: StateFlow<PagingData<UserRate>> = _planned.asStateFlow()

    private val _screenAction = MutableSharedFlow<PlannedScreenAction?>()
    val screenAction: SharedFlow<PlannedScreenAction?> = _screenAction.asSharedFlow()

    init {
        eventHandler(OnInit)
    }

    @Immutable
    internal sealed interface PlannedScreenEvent {
        data object OnInit : PlannedScreenEvent
        data class OnItemClicked(val id: Int) : PlannedScreenEvent
    }

    @Immutable
    internal sealed interface PlannedScreenAction {
        data class NavigateDetails(val id: Int) : PlannedScreenAction
    }

    fun eventHandler(event: PlannedScreenEvent) {
        when (event) {
            OnInit -> onInit()
            is OnItemClicked -> onItemClicked(event.id)
        }
    }

    private fun onInit() = screenModelScope.launch {
//        userId?.let {
            favoritesRepository.getPlannedPaged(1_379_176, UserRatesEnum.PLANNED.status)
                .flowOn(Dispatchers.IO)
                .cachedIn(screenModelScope)
                .collect { data -> _planned.value = data }
//        }
    }

    private fun onItemClicked(id: Int) = screenModelScope.launch {
        _screenAction.emit(
            NavigateDetails(
                id = id
            )
        )
    }

//    private val params = MutableStateFlow("planned")
//
//    @OptIn(ExperimentalCoroutinesApi::class)
//    val plannedPaged =
//         favoritesRepository.getPlannedPaged(
//            id = 1379176,
//            status = status
//         )
//    .cachedIn(screenModelScope)
}
