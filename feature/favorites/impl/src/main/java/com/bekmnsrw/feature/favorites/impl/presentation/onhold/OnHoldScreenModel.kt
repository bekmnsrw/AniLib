package com.bekmnsrw.feature.favorites.impl.presentation.onhold

import androidx.compose.runtime.Immutable
import androidx.paging.PagingData
import androidx.paging.cachedIn
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.bekmnsrw.core.utils.formatStatusString
import com.bekmnsrw.core.widget.UserRatesEnum
import com.bekmnsrw.feature.auth.api.usecase.local.GetUserIdUseCase
import com.bekmnsrw.feature.favorites.api.model.UserRates
import com.bekmnsrw.feature.favorites.api.repository.FavoritesRepository
import com.bekmnsrw.feature.favorites.api.usecase.UpdateAnimeStatusUseCase
import com.bekmnsrw.feature.favorites.impl.FavoritesConstants.ERROR_MESSAGE
import com.bekmnsrw.feature.favorites.impl.FavoritesConstants.WAS_ADDED_TO_CATEGORY
import com.bekmnsrw.feature.favorites.impl.FavoritesConstants.WAS_REMOVED_FROM_MY_LIST
import com.bekmnsrw.feature.favorites.impl.presentation.onhold.OnHoldScreenModel.OnHoldScreenAction.*
import com.bekmnsrw.feature.favorites.impl.presentation.onhold.OnHoldScreenModel.OnHoldScreenEvent.*
import com.bekmnsrw.feature.home.api.usecase.DeleteUserRatesUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.net.ssl.HttpsURLConnection

internal class OnHoldScreenModel(
    private val favoritesRepository: FavoritesRepository,
    private val getUserIdUseCase: GetUserIdUseCase,
    private val updateAnimeStatusUseCase: UpdateAnimeStatusUseCase,
    private val deleteUserRatesUseCase: DeleteUserRatesUseCase
) : ScreenModel {

    private val _screenState = MutableStateFlow(OnHoldScreenState())
    val screenState: StateFlow<OnHoldScreenState> = _screenState.asStateFlow()

    private val _screenAction = MutableSharedFlow<OnHoldScreenAction?>()
    val screenAction: SharedFlow<OnHoldScreenAction?> = _screenAction.asSharedFlow()

    private val _onHold: MutableStateFlow<PagingData<UserRates>> =
        MutableStateFlow(PagingData.empty())
    val onHold: StateFlow<PagingData<UserRates>> = _onHold.asStateFlow()

    init {
        eventHandler(OnInit)
    }

    @Immutable
    internal data class OnHoldScreenState(
        val shouldShowBottomSheet: Boolean = false,
        val selectedItemIndex: Int = 0,
        val shouldShowDialog: Boolean = false
    )

    @Immutable
    internal sealed interface OnHoldScreenEvent {
        data object OnInit : OnHoldScreenEvent
        data class OnItemClick(val id: Int) : OnHoldScreenEvent
        data object OnBottomSheetDismissRequest : OnHoldScreenEvent
        data class OnLongPress(val index: Int) : OnHoldScreenEvent
        data object OnChangeCategoryClick : OnHoldScreenEvent
        data object OnDialogDismissRequest : OnHoldScreenEvent
        data class OnRadioButtonClick(val status: String, val id: Int?) : OnHoldScreenEvent
    }

    @Immutable
    internal sealed interface OnHoldScreenAction {
        data class NavigateDetails(val id: Int) : OnHoldScreenAction
        data class ShowSnackbar(val message: String) : OnHoldScreenAction
    }

    fun eventHandler(event: OnHoldScreenEvent) {
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
        favoritesRepository.getPlannedPaged(1_379_176, UserRatesEnum.ON_HOLD.key)
            .flowOn(Dispatchers.IO)
            .cachedIn(screenModelScope)
            .collect { data -> _onHold.value = data }
    }

    private fun onItemClick(id: Int) = screenModelScope.launch {
        _screenAction.emit(
            NavigateDetails(
                id = id
            )
        )
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

    private suspend fun deleteUserRates(id: Int) = deleteUserRatesUseCase(id = id)
        .flowOn(Dispatchers.IO)
        .collect { code ->
            when (code) {
                HttpsURLConnection.HTTP_NO_CONTENT -> {
                    _screenAction.emit(
                        ShowSnackbar(message = WAS_REMOVED_FROM_MY_LIST)
                    )
                }
                else -> _screenAction.emit(
                    ShowSnackbar(message = ERROR_MESSAGE)
                )
            }
        }

    private suspend fun updateAnimeStatus(
        id: Int,
        status: String
    ) = updateAnimeStatusUseCase(id = id, status = status)
        .flowOn(Dispatchers.IO)
        .catch { _screenAction.emit(ShowSnackbar(message = ERROR_MESSAGE)) }
        .collect { response ->
            _screenAction.emit(
                ShowSnackbar(
                    message = "$WAS_ADDED_TO_CATEGORY '${formatStatusString(response)}'"
                )
            )
        }

    private fun onRadioButtonClick(status: String, id: Int?) = screenModelScope.launch {
        _screenState.emit(
            _screenState.value.copy(
                shouldShowDialog = false
            )
        )

        id?.let {
            when (status) {
                UserRatesEnum.NOT_IN_MY_LIST.key -> deleteUserRates(id = id)
                else -> updateAnimeStatus(id = id, status = status)
            }
        }
    }
}
