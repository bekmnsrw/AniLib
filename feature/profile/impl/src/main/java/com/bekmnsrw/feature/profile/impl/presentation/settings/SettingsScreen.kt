package com.bekmnsrw.feature.profile.impl.presentation.settings

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.registry.ScreenRegistry
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.bekmnsrw.core.designsystem.icon.AniLibIcons
import com.bekmnsrw.core.navigation.SharedScreen
import com.bekmnsrw.core.widget.indicator.AniLibCircularProgressBar
import com.bekmnsrw.core.widget.button.AniLibIconButton
import com.bekmnsrw.core.widget.AniLibSnackbar
import com.bekmnsrw.feature.profile.impl.presentation.settings.SettingsScreenModel.SettingsScreenAction
import com.bekmnsrw.feature.profile.impl.presentation.settings.SettingsScreenModel.SettingsScreenAction.NavigateAuthScreen
import com.bekmnsrw.feature.profile.impl.presentation.settings.SettingsScreenModel.SettingsScreenAction.NavigateBack
import com.bekmnsrw.feature.profile.impl.presentation.settings.SettingsScreenModel.SettingsScreenAction.ShowSnackbar
import com.bekmnsrw.feature.profile.impl.presentation.settings.SettingsScreenModel.SettingsScreenEvent.OnArrowBackClick
import com.bekmnsrw.feature.profile.impl.presentation.settings.SettingsScreenModel.SettingsScreenEvent.OnSignOutButtonClick
import kotlinx.coroutines.launch

internal class SettingsScreen : Screen {

    @Composable
    override fun Content() {
        val screenModel = getScreenModel<SettingsScreenModel>()
        val screenState by screenModel.screenState.collectAsStateWithLifecycle()
        val screenAction by screenModel.screenAction.collectAsStateWithLifecycle(initialValue = null)

        val snackbarHostState = remember { SnackbarHostState() }

        SettingsScreenContent(
            onSignOutButtonClick = { screenModel.eventHandler(OnSignOutButtonClick) },
            onArrowBackClick = { screenModel.eventHandler(OnArrowBackClick) },
            isLoading = screenState.isLoading,
            snackbarHostState = snackbarHostState
        )

        SettingsScreenActions(
            screenAction = screenAction,
            snackbarHostState = snackbarHostState
        )
    }
}

@Composable
private fun SettingsScreenActions(
    screenAction: SettingsScreenAction?,
    snackbarHostState: SnackbarHostState
) {
    val navigator = LocalNavigator.currentOrThrow
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(screenAction) {
        when (screenAction) {
            null -> Unit

            NavigateAuthScreen -> {
                val authScreen = ScreenRegistry.get(
                    provider = SharedScreen.AuthScreen
                )
                navigator.replaceAll(item = authScreen)
            }

            is ShowSnackbar -> coroutineScope.launch {
                snackbarHostState.showSnackbar(
                    message = screenAction.message,
                    duration = SnackbarDuration.Short
                )
            }

            NavigateBack -> navigator.pop()
        }
    }
}

@Composable
private fun SettingsScreenContent(
    onSignOutButtonClick: () -> Unit,
    onArrowBackClick: () -> Unit,
    isLoading: Boolean,
    snackbarHostState: SnackbarHostState
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Button(
            onClick = onSignOutButtonClick,
            modifier = Modifier.align(Alignment.Center)
        ) {
            Text(text = "SignOut")
        }
        AniLibIconButton(
            modifier = Modifier.align(Alignment.TopStart),
            onClick = onArrowBackClick,
            imageVector =  AniLibIcons.ArrowBack,
        )
    }
    
    if (isLoading) {
        AniLibCircularProgressBar(shouldShow = true)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        AniLibSnackbar(
            modifier = Modifier.align(Alignment.BottomCenter),
            snackbarHostState = snackbarHostState
        )
    }
}
