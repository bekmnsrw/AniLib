package com.bekmnsrw.anilib

import com.bekmnsrw.anilib.splash.SplashScreenModel
import org.koin.dsl.module

val appModule = module {
    factory<SplashScreenModel> {
        provideSplashScreenModel()
    }
}

private fun provideSplashScreenModel(): SplashScreenModel = SplashScreenModel()
