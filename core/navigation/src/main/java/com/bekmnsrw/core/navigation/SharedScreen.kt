package com.bekmnsrw.core.navigation

import cafe.adriel.voyager.core.registry.ScreenProvider

sealed class SharedScreen : ScreenProvider {

    data object HomeScreen : SharedScreen()
    data object FavoritesScreen : SharedScreen()
    data object ProfileScreen : SharedScreen()
    data object AuthTab : SharedScreen()
    data class AuthScreen(val source: String) : SharedScreen()
    data class DetailsScreen(val id: Int) : SharedScreen()
}
