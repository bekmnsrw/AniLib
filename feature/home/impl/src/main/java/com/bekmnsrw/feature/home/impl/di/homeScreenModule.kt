package com.bekmnsrw.feature.home.impl.di

import cafe.adriel.voyager.core.registry.screenModule
import com.bekmnsrw.core.navigation.SharedScreen
import com.bekmnsrw.feature.home.impl.presentation.HomeTab
import com.bekmnsrw.feature.home.impl.presentation.details.DetailsScreen

val homeScreenModule = screenModule {
    register<SharedScreen.HomeScreen> { HomeTab }
    register<SharedScreen.DetailsScreen> { provider -> DetailsScreen(id = provider.id) }
}
