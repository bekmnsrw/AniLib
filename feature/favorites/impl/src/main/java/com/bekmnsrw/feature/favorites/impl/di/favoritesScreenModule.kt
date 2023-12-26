package com.bekmnsrw.feature.favorites.impl.di

import cafe.adriel.voyager.core.registry.screenModule
import com.bekmnsrw.core.navigation.SharedScreen
import com.bekmnsrw.feature.favorites.impl.presentation.FavoritesTab

val favoritesScreenModule = screenModule {
    register<SharedScreen.FavoritesScreen> { FavoritesTab }
}
