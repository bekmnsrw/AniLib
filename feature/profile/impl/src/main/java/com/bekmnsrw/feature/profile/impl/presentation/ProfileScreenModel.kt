package com.bekmnsrw.feature.profile.impl.presentation

import androidx.compose.runtime.Immutable
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.bekmnsrw.feature.auth.api.usecase.local.IsAuthenticatedUseCase
import com.bekmnsrw.feature.profile.api.model.WhoAmI
import com.bekmnsrw.feature.profile.api.usecase.remote.GetProfileUseCase
import com.bekmnsrw.feature.profile.impl.presentation.ProfileScreenModel.ProfileScreenAction.NavigateAuthScreen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

internal class ProfileScreenModel(
    private val isAuthenticatedUseCase: IsAuthenticatedUseCase,
    private val getProfileUseCase: GetProfileUseCase
) : ScreenModel {

    @Immutable
    internal data class ProfileScreenState(
        val isAuthenticated: Boolean? = null,
        val profile: WhoAmI? = null,
        val isLoading: Boolean = false
    )

    @Immutable
    internal sealed interface ProfileScreenAction {
        data object NavigateAuthScreen : ProfileScreenAction
    }

    @Immutable
    internal sealed interface ProfileScreenEvent {
        data object OnButtonClicked : ProfileScreenEvent
    }

    private val _screenState = MutableStateFlow(ProfileScreenState())
    val screenState: StateFlow<ProfileScreenState> = _screenState.asStateFlow()

    private val _screenAction = MutableSharedFlow<ProfileScreenAction>()
    val screenAction: SharedFlow<ProfileScreenAction> = _screenAction.asSharedFlow()

    init { isUserAuthenticated() }

    fun eventHandler(profileScreenEvent: ProfileScreenEvent) {
        when (profileScreenEvent) {
            ProfileScreenEvent.OnButtonClicked -> onButtonClicked()
        }
    }

    private fun isUserAuthenticated() = screenModelScope.launch {
        val isAuthenticated = isAuthenticatedUseCase()
            .flowOn(Dispatchers.IO)
            .first()

        println("ProfileSM (isAuthenticated): $isAuthenticated")

        _screenState.emit(
            _screenState.value.copy(
                isAuthenticated = isAuthenticated
            )
        )

        if (isAuthenticated != true) _screenAction.emit(NavigateAuthScreen)
    }

    private fun onButtonClicked() = screenModelScope.launch {
        getProfileUseCase()
            .flowOn(Dispatchers.IO)
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
            .collect {
                _screenState.emit(
                    _screenState.value.copy(
                        profile = it
                    )
                )
            }
    }
}
