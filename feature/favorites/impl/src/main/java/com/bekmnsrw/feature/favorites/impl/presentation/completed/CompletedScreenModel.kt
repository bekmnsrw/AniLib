package com.bekmnsrw.feature.favorites.impl.presentation.completed

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
    private val getUserIdUseCase: GetUserIdUseCase,
    private val updateAnimeStatusUseCase: UpdateAnimeStatusUseCase
) : ScreenModel {

    private val _screenState = MutableStateFlow(CompletedScreenState())
    val screenState: StateFlow<CompletedScreenState> = _screenState.asStateFlow()

    private val _screenAction = MutableSharedFlow<CompletedScreenAction?>()
    val screenAction: SharedFlow<CompletedScreenAction?> = _screenAction.asSharedFlow()

    private val _completed: MutableStateFlow<PagingData<UserRates>> = MutableStateFlow(PagingData.empty())
    val completed: StateFlow<PagingData<UserRates>> = _completed.asStateFlow()

    init {
        eventHandler(OnInit)
    }

    @Immutable
    internal data class CompletedScreenState(
        val shouldShowModalBottomSheet: Boolean = false,
        val selectedItemIndex: Int = 0,
        val shouldShowDialog: Boolean = false
    )

    @Immutable
    internal sealed interface CompletedScreenEvent {
        data object OnInit : CompletedScreenEvent
        data class OnItemClick(val id: Int) : CompletedScreenEvent
        data object OnModalBottomSheetDismissRequest : CompletedScreenEvent
        data class OnLongPress(val index: Int) : CompletedScreenEvent
        data object OnChangeCategoryClick : CompletedScreenEvent
        data object OnDialogDismissRequest : CompletedScreenEvent
        data class OnRadioButtonClick(val key: String, val id: Int) : CompletedScreenEvent
    }

    @Immutable
    internal sealed interface CompletedScreenAction {
        data class NavigateDetails(val id: Int) : CompletedScreenAction
        data class ShowSnackbar(val message: String) : CompletedScreenAction
    }

    fun eventHandler(event: CompletedScreenEvent) {
        when (event) {
            OnInit -> onInit()

            is OnItemClick -> onItemClick(event.id)

            OnModalBottomSheetDismissRequest -> onModalBottomSheetDismissRequest()

            is OnLongPress -> onLongPress(event.index)

            OnChangeCategoryClick -> onChangeCategoryClick()

            OnDialogDismissRequest -> onDialogDismissRequest()

            is OnRadioButtonClick -> onRadioButtonClick(event.key, event.id)
        }
    }

    private fun onInit() = screenModelScope.launch {
        favoritesRepository.getPlannedPaged(1_379_176, UserRatesEnum.COMPLETED.key)
            .flowOn(Dispatchers.IO)
            .cachedIn(screenModelScope)
            .collect { data -> _completed.value = data }
    }

    private fun onItemClick(id: Int) = screenModelScope.launch {
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

    private fun onChangeCategoryClick() = screenModelScope.launch {
        _screenState.emit(
            _screenState.value.copy(
                shouldShowDialog = true,
                shouldShowModalBottomSheet = false
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

    private fun onRadioButtonClick(key: String, id: Int) = screenModelScope.launch {
        updateAnimeStatusUseCase(id = id, status = key)
            .flowOn(Dispatchers.IO)
            .collect { response ->
                val status = response
                    .replace(oldValue = "_", newValue = " ")
                    .replaceFirstChar { it.uppercase() }

                _screenAction.emit(
                    ShowSnackbar(
                        message = "Successfully added to '$status' category")
                )
            }

        _screenState.emit(
            _screenState.value.copy(
                shouldShowDialog = false
            )
        )
    }
}
