package com.bekmnsrw.feature.home.impl.di

import cafe.adriel.voyager.core.registry.screenModule
import com.bekmnsrw.core.navigation.SharedScreen
import com.bekmnsrw.feature.home.impl.presentation.HomeTab

val homeScreenModule = screenModule {
    register<SharedScreen.HomeScreen> { HomeTab }
}
