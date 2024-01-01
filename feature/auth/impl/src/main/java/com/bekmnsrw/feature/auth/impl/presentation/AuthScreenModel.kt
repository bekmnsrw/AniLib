package com.bekmnsrw.feature.auth.impl.presentation

import androidx.compose.runtime.Immutable
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.bekmnsrw.feature.auth.api.AuthConstant
import com.bekmnsrw.feature.auth.api.model.AccessToken
import com.bekmnsrw.feature.auth.api.usecase.local.OnAuthenticationUseCase
import com.bekmnsrw.feature.auth.api.usecase.local.SaveLocalAccessTokenUseCase
import com.bekmnsrw.feature.auth.api.usecase.local.SaveLocalRefreshTokenUseCase
import com.bekmnsrw.feature.auth.api.usecase.remote.GetRemoteAccessTokenUseCase
import com.bekmnsrw.feature.auth.impl.presentation.AuthScreenModel.AuthScreenAction.NavigateProfileScreen
import com.bekmnsrw.feature.auth.impl.presentation.AuthScreenModel.AuthScreenAction.OpenChromeCustomTabs
import com.bekmnsrw.feature.auth.impl.presentation.AuthScreenModel.AuthScreenEvent.OnAuthenticateButtonClicked
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class AuthScreenModel(
    private val getRemoteAccessTokenUseCase: GetRemoteAccessTokenUseCase,
    private val onAuthenticationUseCase: OnAuthenticationUseCase,
    private val saveLocalAccessTokenUseCase: SaveLocalAccessTokenUseCase,
    private val saveLocalRefreshTokenUseCase: SaveLocalRefreshTokenUseCase
) : ScreenModel {

    private companion object {
        const val AUTH_URI = "https://shikimori.one/oauth/authorize?" +
                "client_id=${AuthConstant.CLIENT_ID}&" +
                "redirect_uri=${AuthConstant.REDIRECT_URI}&" +
                "response_type=${AuthConstant.RESPONSE_TYPE}&" +
                "scope=${AuthConstant.SCOPE}"
    }

    @Immutable
    data class AuthScreenState(
        val isAuthenticated: Boolean? = null,
        val isFirstAppLaunch: Boolean? = null
    )

    @Immutable
    sealed interface AuthScreenEvent {
        data object OnAuthenticateButtonClicked : AuthScreenEvent
    }

    @Immutable
    sealed interface AuthScreenAction {
        data object NavigateProfileScreen : AuthScreenAction
        data class OpenChromeCustomTabs(val authUri: String) : AuthScreenAction
    }

    private val _screenState = MutableStateFlow(AuthScreenState())
    val screenState: StateFlow<AuthScreenState> = _screenState.asStateFlow()

    private val _screenAction = MutableSharedFlow<AuthScreenAction?>()
    val screenAction: SharedFlow<AuthScreenAction?> = _screenAction.asSharedFlow()

    fun eventHandler(event: AuthScreenEvent) {
        when (event) {
            OnAuthenticateButtonClicked -> onAuthenticateButtonClicked()
        }
    }

    private fun onAuthenticateButtonClicked() = screenModelScope.launch {
        _screenAction.emit(OpenChromeCustomTabs(authUri = AUTH_URI))
    }

    fun getAccessToken(authCode: String) = screenModelScope.launch {
        val accessToken = getRemoteAccessTokenUseCase(
                grantType = AuthConstant.GRANT_TYPE_AUTH_CODE,
                clientId = AuthConstant.CLIENT_ID,
                clientSecret = AuthConstant.CLIENT_SECRET,
                authCode = authCode,
                redirectUri = AuthConstant.REDIRECT_URI
            )
                .flowOn(Dispatchers.IO)
                .first()

        onAuthenticationUseCase()
        saveTokens(accessToken = accessToken)
        _screenAction.emit(NavigateProfileScreen)
    }

    private suspend fun saveTokens(accessToken: AccessToken) = screenModelScope.launch {
        saveLocalAccessTokenUseCase(accessToken = accessToken.accessToken)
        saveLocalRefreshTokenUseCase(refreshToken = accessToken.refreshToken)
        println("Access token was saved!")
    }
}
