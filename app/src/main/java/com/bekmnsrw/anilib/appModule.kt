package com.bekmnsrw.anilib

import org.koin.dsl.module

val appModule = module {
    factory<SplashScreenModel> {
        provideSplashScreenModel()
    }
}

private fun provideSplashScreenModel(): SplashScreenModel = SplashScreenModel()
