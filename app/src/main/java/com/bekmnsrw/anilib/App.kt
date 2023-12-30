package com.bekmnsrw.anilib

import android.app.Application
import cafe.adriel.voyager.core.registry.ScreenRegistry
import com.bekmnsrw.core.db.di.dbModule
import com.bekmnsrw.core.network.di.networkModule
import com.bekmnsrw.feature.auth.impl.di.authModule
import com.bekmnsrw.feature.auth.impl.di.authScreenModule
import com.bekmnsrw.feature.favorites.impl.di.favoritesModule
import com.bekmnsrw.feature.favorites.impl.di.favoritesScreenModule
import com.bekmnsrw.feature.home.impl.di.homeModule
import com.bekmnsrw.feature.home.impl.di.homeScreenModule
import com.bekmnsrw.feature.profile.impl.di.profileModule
import com.bekmnsrw.feature.profile.impl.di.profileScreenModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(
                appModule,
                networkModule,
                authModule,
                homeModule,
                favoritesModule,
                profileModule,
                dbModule
            )
        }

        ScreenRegistry {
            authScreenModule()
            homeScreenModule()
            favoritesScreenModule()
            profileScreenModule()
        }
    }
}
