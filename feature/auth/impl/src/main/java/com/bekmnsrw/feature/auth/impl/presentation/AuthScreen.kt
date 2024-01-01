package com.bekmnsrw.feature.auth.impl.presentation

import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.registry.ScreenRegistry
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.bekmnsrw.core.designsystem.theme.AniLibTypography
import com.bekmnsrw.core.navigation.SharedScreen
import com.bekmnsrw.feature.auth.impl.presentation.AuthScreenModel.AuthScreenAction
import com.bekmnsrw.feature.auth.impl.presentation.AuthScreenModel.AuthScreenAction.NavigateProfileScreen
import com.bekmnsrw.feature.auth.impl.presentation.AuthScreenModel.AuthScreenAction.OpenChromeCustomTabs
import com.bekmnsrw.feature.auth.impl.presentation.AuthScreenModel.AuthScreenEvent
import com.bekmnsrw.feature.auth.impl.presentation.AuthScreenModel.AuthScreenEvent.OnAuthenticateButtonClicked

internal class AuthScreen : Screen {

    @Composable
    override fun Content() {
        val screenModel = getScreenModel<AuthScreenModel>()
        val screenAction by screenModel.screenAction.collectAsStateWithLifecycle(initialValue = null)

        AuthScreenContent(eventHandler = screenModel::eventHandler)
        AuthScreenActions(screenAction = screenAction,)
    }
}

@Composable
private fun AuthScreenContent(eventHandler: (AuthScreenEvent) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Still not with us?",
                textAlign = TextAlign.Center,
                style = AniLibTypography.titleMedium
            )
            Text(
                text = "Please, authenticate to get access to full app's functionality!",
                textAlign = TextAlign.Center,
                style = AniLibTypography.bodyMedium
            )
            Spacer(modifier = Modifier.padding(vertical = 8.dp))
            Button(onClick = { eventHandler(OnAuthenticateButtonClicked) }) {
                Text(text = "Authenticate")
            }
        }
    }
}

@Composable
private fun AuthScreenActions(screenAction: AuthScreenAction?, ) {
    val context = LocalContext.current
    val navigator = LocalNavigator.currentOrThrow

    LaunchedEffect(screenAction) {
        when (screenAction) {
            null -> Unit

            NavigateProfileScreen -> {
                val profileScreen = ScreenRegistry.get(
                    provider = SharedScreen.ProfileScreen
                )
                navigator.replaceAll(item = profileScreen)
            }

            is OpenChromeCustomTabs -> CustomTabsIntent.Builder()
                .build()
                .launchUrl(context, Uri.parse(screenAction.authUri))
        }
    }
}
