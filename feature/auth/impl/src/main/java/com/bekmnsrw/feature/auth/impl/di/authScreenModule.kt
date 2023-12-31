package com.bekmnsrw.feature.auth.impl.di

import cafe.adriel.voyager.core.registry.screenModule
import com.bekmnsrw.core.navigation.SharedScreen
import com.bekmnsrw.feature.auth.impl.presentation.AuthScreen
import com.bekmnsrw.feature.auth.impl.presentation.AuthTab

val authScreenModule = screenModule {
    register<SharedScreen.AuthTab> { AuthTab }
    register<SharedScreen.AuthScreen> { provider -> AuthScreen(provider.source) }
}
