package com.bekmnsrw.feature.profile.impl.presentation.settings

import androidx.compose.runtime.Immutable
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.bekmnsrw.feature.auth.api.usecase.local.OnSignOutUseCase
import com.bekmnsrw.feature.profile.api.usecase.SignOutUseCase
import com.bekmnsrw.feature.profile.impl.presentation.settings.SettingsScreenModel.SettingsScreenAction.*
import com.bekmnsrw.feature.profile.impl.presentation.settings.SettingsScreenModel.SettingsScreenEvent.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import java.net.HttpURLConnection

internal class SettingsScreenModel(
    private val signOutUseCase: SignOutUseCase,
    private val onSignOutUseCase: OnSignOutUseCase
) : ScreenModel {

    @Immutable
    internal data class SettingsScreenState(
        val isLoading: Boolean = false
    )

    @Immutable
    internal sealed interface SettingsScreenEvent {
        data object OnSignOutButtonClick : SettingsScreenEvent
        data object OnArrowBackClick : SettingsScreenEvent
    }

    @Immutable
    internal sealed interface SettingsScreenAction {
        data object NavigateAuthScreen : SettingsScreenAction
        data object NavigateBack : SettingsScreenAction
        data class ShowSnackbar(val message: String) : SettingsScreenAction
    }

    private val _screenState = MutableStateFlow(SettingsScreenState())
    val screenState: StateFlow<SettingsScreenState> = _screenState.asStateFlow()

    private val _screenAction = MutableSharedFlow<SettingsScreenAction?>()
    val screenAction: SharedFlow<SettingsScreenAction?> = _screenAction.asSharedFlow()

    fun eventHandler(event: SettingsScreenEvent) {
        when (event) {
            OnSignOutButtonClick -> onSignOutButtonClick()
            OnArrowBackClick -> onArrowBackClick()
        }
    }

    private fun onSignOutButtonClick() = screenModelScope.launch {
        signOutUseCase()
            .onStart {
                _screenState.emit(
                    _screenState.value.copy(
                        isLoading = true
                    )
                )
            }
            .onCompletion {
                _screenState.emit(
                    _screenState.value.copy(
                        isLoading = false
                    )
                )
            }
            .collect { code ->
                when (code) {
                    HttpURLConnection.HTTP_OK -> {
                        onSignOutUseCase()
                        _screenAction.emit(NavigateAuthScreen)
                    }
                    else -> _screenAction.emit(ShowSnackbar(message = "Oops, please try again!"))
                }
            }
    }

    private fun onArrowBackClick() = screenModelScope.launch {
        _screenAction.emit(NavigateBack)
    }
}
