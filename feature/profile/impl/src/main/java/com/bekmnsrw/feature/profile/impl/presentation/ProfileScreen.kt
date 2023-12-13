package com.bekmnsrw.feature.profile.impl.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.registry.rememberScreen
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.bekmnsrw.core.navigation.SharedScreen
import com.bekmnsrw.core.widget.AniLibCircularProgressBar
import com.bekmnsrw.feature.profile.impl.presentation.ProfileScreenModel.*
import com.bekmnsrw.feature.profile.impl.presentation.ProfileScreenModel.ProfileScreenAction
import com.bekmnsrw.feature.profile.impl.presentation.ProfileScreenModel.ProfileScreenEvent
import com.bekmnsrw.feature.profile.impl.presentation.ProfileScreenModel.ProfileScreenEvent.*

internal class ProfileScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val screenModel = getScreenModel<ProfileScreenModel>()
        val screenState by screenModel.screenState.collectAsStateWithLifecycle()
        val screenAction by screenModel.screenAction.collectAsStateWithLifecycle(initialValue = null)

        ProfileScreenContent(
            onButtonClicked = {
                screenModel.eventHandler(OnButtonClicked)
            }
        )

        ProfileScreenActions(
            screenAction = screenAction,
            navigator = navigator
        )

        AniLibCircularProgressBar(shouldShow = screenState.isLoading)
    }
}

@Composable
private fun ProfileScreenContent(
    onButtonClicked: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center)
        ) {
            Text(text = "Profile")
            Button(onClick = { onButtonClicked() }) {
                Text(text = "Who Am I?")
            }
        }
    }
}

@Composable
private fun ProfileScreenActions(
    screenAction: ProfileScreenAction?,
    navigator: Navigator
) {
    val authScreen = rememberScreen(provider = SharedScreen.AuthScreen)

    LaunchedEffect(screenAction) {
        when (screenAction) {
            null -> Unit

            ProfileScreenAction.NavigateAuthScreen -> navigator.push(authScreen)
        }
    }
}
