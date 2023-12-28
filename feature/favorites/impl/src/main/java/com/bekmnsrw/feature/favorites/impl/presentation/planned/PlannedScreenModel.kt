package com.bekmnsrw.feature.favorites.impl.presentation.planned

import androidx.compose.runtime.Immutable
import androidx.paging.PagingData
import androidx.paging.cachedIn
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.bekmnsrw.feature.auth.api.usecase.local.GetUserIdUseCase
import com.bekmnsrw.feature.favorites.api.model.UserRates
import com.bekmnsrw.feature.favorites.api.repository.FavoritesRepository
import com.bekmnsrw.feature.favorites.api.usecase.UpdateAnimeStatusUseCase
import com.bekmnsrw.feature.favorites.impl.UserRatesEnum
import com.bekmnsrw.feature.favorites.impl.presentation.planned.PlannedScreenModel.PlannedScreenAction.*
import com.bekmnsrw.feature.favorites.impl.presentation.planned.PlannedScreenModel.PlannedScreenEvent.*
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
    private val getUserIdUseCase: GetUserIdUseCase,
    private val updateAnimeStatusUseCase: UpdateAnimeStatusUseCase
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

    private val _screenState = MutableStateFlow(PlannedScreenState())
    val screenState: StateFlow<PlannedScreenState> = _screenState.asStateFlow()

    private val _screenAction = MutableSharedFlow<PlannedScreenAction?>()
    val screenAction: SharedFlow<PlannedScreenAction?> = _screenAction.asSharedFlow()

    private val _planned: MutableStateFlow<PagingData<UserRates>> = MutableStateFlow(PagingData.empty())
    val planned: StateFlow<PagingData<UserRates>> = _planned.asStateFlow()

    init {
        eventHandler(OnInit)
    }

    @Immutable
    internal data class PlannedScreenState(
        val shouldShowBottomSheet: Boolean = false,
        val selectedItemIndex: Int = 0,
        val shouldShowDialog: Boolean = false
    )

    @Immutable
    internal sealed interface PlannedScreenEvent {
        data object OnInit : PlannedScreenEvent
        data class OnItemClick(val id: Int) : PlannedScreenEvent
        data object OnBottomSheetDismissRequest : PlannedScreenEvent
        data class OnLongPress(val index: Int) : PlannedScreenEvent
        data object OnChangeCategoryClick : PlannedScreenEvent
        data object OnDialogDismissRequest : PlannedScreenEvent
        data class OnRadioButtonClick(val status: String, val id: Int) : PlannedScreenEvent
    }

    @Immutable
    internal sealed interface PlannedScreenAction {
        data class NavigateDetails(val id: Int) : PlannedScreenAction
        data class ShowSnackbar(val message: String) : PlannedScreenAction
    }

    fun eventHandler(event: PlannedScreenEvent) {
        when (event) {
            OnInit -> onInit()
            is OnItemClick -> onItemClick(event.id)
            is OnLongPress -> onLongPress(event.index)
            OnBottomSheetDismissRequest -> onBottomSheetDismissRequest()
            OnChangeCategoryClick -> onChangeCategoryClick()
            OnDialogDismissRequest -> onDialogDismissRequest()
            is OnRadioButtonClick -> onRadioButtonClick(event.status, event.id)
        }
    }

    private fun onInit() = screenModelScope.launch {
//        userId?.let {
            favoritesRepository.getPlannedPaged(1_379_176, UserRatesEnum.PLANNED.key)
                .flowOn(Dispatchers.IO)
                .cachedIn(screenModelScope)
                .collect { data -> _planned.value = data }
//        }
    }

    private fun onItemClick(id: Int) = screenModelScope.launch {
        _screenAction.emit(NavigateDetails(id = id))
    }

    private fun onBottomSheetDismissRequest() = screenModelScope.launch {
        _screenState.emit(
            _screenState.value.copy(
                shouldShowBottomSheet = false
            )
        )
    }

    private fun onLongPress(index: Int) = screenModelScope.launch {
        _screenState.emit(
            _screenState.value.copy(
                shouldShowBottomSheet = true,
                selectedItemIndex = index
            )
        )
    }

    private fun onChangeCategoryClick() = screenModelScope.launch {
        _screenState.emit(
            _screenState.value.copy(
                shouldShowDialog = true,
                shouldShowBottomSheet = false
            )
        )
    }

    private fun onDialogDismissRequest() = screenModelScope.launch {
        _screenState.emit(
            _screenState.value.copy(
                shouldShowDialog = false
            )
        )
    }

    private fun onRadioButtonClick(status: String, id: Int) = screenModelScope.launch {
        updateAnimeStatusUseCase(id = id, status = status)
            .flowOn(Dispatchers.IO)
            .collect { response ->
                val updatedStatus = response
                    .replace(oldValue = "_", newValue = " ")
                    .replaceFirstChar { it.uppercase() }

                _screenAction.emit(
                    ShowSnackbar(
                        message = "Successfully added to '$updatedStatus' category"
                    )
                )
            }

        _screenState.emit(
            _screenState.value.copy(
                shouldShowDialog = false
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
