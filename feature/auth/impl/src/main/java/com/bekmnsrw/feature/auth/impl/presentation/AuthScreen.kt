package com.bekmnsrw.feature.auth.impl.presentation

import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.registry.rememberScreen
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.bekmnsrw.core.navigation.SharedScreen
import com.bekmnsrw.feature.auth.impl.presentation.AuthScreenModel.*
import com.bekmnsrw.feature.auth.impl.presentation.AuthScreenModel.AuthScreenAction.*
import com.bekmnsrw.feature.auth.impl.presentation.AuthScreenModel.AuthScreenEvent.*

class AuthScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val screenModel = getScreenModel<AuthScreenModel>()
//        val screenState by screenModel.screenState.collectAsStateWithLifecycle()
        val screenAction by screenModel.screenAction.collectAsStateWithLifecycle(initialValue = null)

        AuthScreenContent(
            eventHandler = screenModel::eventHandler
        )

        AuthScreenActions(
            screenAction = screenAction,
            navigator = navigator
        )

        HandleScreenLifecycle(
            onResume = {
                screenModel.eventHandler(OnResume)
                println("AuthScreen (onResume) was called")
            },
            onStart = {
                screenModel.eventHandler(OnStart)
                println("AuthScreen (onStart) was called")
            }
        )
    }
}

@Composable
private fun AuthScreenContent(
    eventHandler: (AuthScreenEvent) -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "AuthScreen")

            Button(onClick = { eventHandler(OnAuthenticateButtonClicked) }) {
                Text(text = "Authenticate")
            }

//            if (isFirstAppLaunch == true) {
//                Button(onClick = { /* Go to HomeScreen */ }) {
//                    Text(text = "Skip")
//                }
//            }
        }
    }
}

@Composable
private fun AuthScreenActions(
    screenAction: AuthScreenAction?,
    navigator: Navigator
) {
    val context = LocalContext.current
    val profileScreen = rememberScreen(provider = SharedScreen.ProfileScreen)

    LaunchedEffect(screenAction) {
        when (screenAction) {
            null -> Unit

            NavigateProfileScreen -> {
                navigator.replaceAll(item = profileScreen)
                println("HERE")
            }

            is OpenChromeCustomTabs -> CustomTabsIntent.Builder()
                .build()
                .launchUrl(context, Uri.parse(screenAction.authUri))
        }
    }
}

// Move to utils???
@Composable
private fun HandleScreenLifecycle(
    onResume: () -> Unit,
    onStart: () -> Unit
) {
    val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
    val currentOnResume by rememberUpdatedState(newValue = onResume)
    val currentOnStart by rememberUpdatedState(newValue = onStart)

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> {
                    currentOnResume()
                    println(event)
                }

                Lifecycle.Event.ON_START -> {
                    currentOnStart()
                    println(event)
                }

                else -> println(event)
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }
}
