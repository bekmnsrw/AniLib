package com.bekmnsrw.anilib

import com.bekmnsrw.anilib.splashscreen.SplashScreenModel
import com.bekmnsrw.feature.auth.api.usecase.local.IsFirstAppLaunchUseCase
import com.bekmnsrw.feature.auth.api.usecase.local.OnFirstAppLaunchUseCase
import org.koin.dsl.module

val appModule = module {
    factory<SplashScreenModel> {
        provideSplashScreenModel(
            isFirstAppLaunchUseCase = get(),
            onFirstAppLaunchUseCase = get()
        )
    }
}

private fun provideSplashScreenModel(
    isFirstAppLaunchUseCase: IsFirstAppLaunchUseCase,
    onFirstAppLaunchUseCase: OnFirstAppLaunchUseCase
): SplashScreenModel = SplashScreenModel(
    isFirstAppLaunchUseCase = isFirstAppLaunchUseCase,
    onFirstAppLaunchUseCase = onFirstAppLaunchUseCase
)
