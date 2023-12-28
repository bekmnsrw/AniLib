package com.bekmnsrw.feature.favorites.impl.presentation.watching

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
import com.bekmnsrw.feature.favorites.impl.presentation.watching.WatchingScreenModel.WatchingScreenAction.*
import com.bekmnsrw.feature.favorites.impl.presentation.watching.WatchingScreenModel.WatchingScreenEvent.*
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
    private val getUserIdUseCase: GetUserIdUseCase,
    private val updateAnimeStatusUseCase: UpdateAnimeStatusUseCase
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
        val shouldShowBottomSheet: Boolean = false,
        val selectedItemIndex: Int = 0,
        val shouldShowDialog: Boolean = false
    )

    @Immutable
    internal sealed interface WatchingScreenEvent {
        data object OnInit : WatchingScreenEvent
        data class OnItemClick(val id: Int) : WatchingScreenEvent
        data object OnBottomSheetDismissRequest : WatchingScreenEvent
        data class OnLongPress(val index: Int) : WatchingScreenEvent
        data object OnChangeCategoryClick : WatchingScreenEvent
        data object OnDialogDismissRequest : WatchingScreenEvent
        data class OnRadioButtonClick(val status: String, val id: Int) : WatchingScreenEvent
    }

    @Immutable
    internal sealed interface WatchingScreenAction {
        data class NavigateDetails(val id: Int) : WatchingScreenAction
        data class ShowSnackbar(val message: String) : WatchingScreenAction
    }

    fun eventHandler(event: WatchingScreenEvent) {
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
        favoritesRepository.getPlannedPaged(1_379_176, UserRatesEnum.WATCHING.key)
            .flowOn(Dispatchers.IO)
            .cachedIn(screenModelScope)
            .collect { data -> _watching.value = data }
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
}
