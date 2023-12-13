package com.bekmnsrw.feature.auth.impl.presentation

import androidx.compose.runtime.Immutable
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.bekmnsrw.feature.auth.api.model.AccessToken
import com.bekmnsrw.feature.auth.api.usecase.remote.GetRemoteAccessTokenUseCase
import com.bekmnsrw.feature.auth.api.usecase.local.IsAuthenticatedUseCase
import com.bekmnsrw.feature.auth.api.usecase.local.OnAuthenticationUseCase
import com.bekmnsrw.feature.auth.api.usecase.local.SaveLocalAccessTokenUseCase
import com.bekmnsrw.feature.auth.api.usecase.local.SaveLocalRefreshTokenUseCase
import com.bekmnsrw.feature.auth.api.AuthConstant
import com.bekmnsrw.feature.auth.impl.presentation.AuthScreenModel.AuthScreenAction.NavigateProfileScreen
import com.bekmnsrw.feature.auth.impl.presentation.AuthScreenModel.AuthScreenAction.OpenChromeCustomTabs
import com.bekmnsrw.feature.auth.impl.presentation.AuthScreenModel.AuthScreenEvent.OnAuthenticateButtonClicked
import com.bekmnsrw.feature.auth.impl.presentation.AuthScreenModel.AuthScreenEvent.OnResume
import com.bekmnsrw.feature.auth.impl.presentation.AuthScreenModel.AuthScreenEvent.OnStart
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
    private val isAuthenticatedUseCase: IsAuthenticatedUseCase,
    private val saveLocalAccessTokenUseCase: SaveLocalAccessTokenUseCase,
    private val saveLocalRefreshTokenUseCase: SaveLocalRefreshTokenUseCase
) : ScreenModel {

    companion object {
        private const val AUTH_URI = "https://shikimori.one/oauth/authorize?" +
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
        data object OnResume : AuthScreenEvent
        data object OnStart : AuthScreenEvent
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

    fun eventHandler(authScreenEvent: AuthScreenEvent) {
        when (authScreenEvent) {
            OnAuthenticateButtonClicked -> onAuthenticateButtonClicked()
            OnResume -> onResume()
            OnStart -> onStart()
        }
    }

    private fun onAuthenticateButtonClicked() = screenModelScope.launch {
        _screenAction.emit(
            OpenChromeCustomTabs(
                authUri = AUTH_URI
            )
        )
    }

    private fun onResume() = screenModelScope.launch {
        val isAuthenticated = isAuthenticatedUseCase()
            .flowOn(Dispatchers.IO)
            .first()

        println("AuthSM (isAuthenticated): $isAuthenticated")

        _screenState.emit(
            _screenState.value.copy(
                isAuthenticated = isAuthenticated
            )
        )

        if (isAuthenticated == true) _screenAction.emit(NavigateProfileScreen)
    }

    private fun onStart() = screenModelScope.launch {
        val isAuthenticated = isAuthenticatedUseCase()
            .flowOn(Dispatchers.IO)
            .first()

        println("AuthSM (isAuthenticated): $isAuthenticated")

        _screenState.emit(
            _screenState.value.copy(
                isAuthenticated = isAuthenticated
            )
        )

        if (isAuthenticated == true) _screenAction.emit(NavigateProfileScreen)
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

        _screenAction.emit(NavigateProfileScreen)

//            .flowOn(Dispatchers.IO)
//            .collect {
////                _screenAction.emit(NavigateProfileScreen)
//                onAuthenticationUseCase()

        // TODO: Save access & refresh tokens
        println("AuthSM (accessToken): ${accessToken.accessToken}")
        println("AuthSM (refreshToken): ${accessToken.refreshToken}")

        saveTokens(accessToken = accessToken)
    }

    private fun saveTokens(accessToken: AccessToken) = screenModelScope.launch {
        saveLocalAccessTokenUseCase(accessToken = accessToken.accessToken)
        saveLocalRefreshTokenUseCase(refreshToken = accessToken.refreshToken)
    }
}
