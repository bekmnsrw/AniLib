package com.bekmnsrw.feature.profile.impl.di

import cafe.adriel.voyager.core.registry.screenModule
import com.bekmnsrw.core.navigation.SharedScreen
import com.bekmnsrw.feature.profile.impl.presentation.ProfileTab

val profileScreenModule = screenModule {
    register<SharedScreen.ProfileScreen> { ProfileTab }
}
