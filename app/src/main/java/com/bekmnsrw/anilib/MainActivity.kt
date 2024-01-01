package com.bekmnsrw.anilib

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.bekmnsrw.anilib.splashscreen.SplashScreenModel
import com.bekmnsrw.core.designsystem.theme.AniLibTheme
import com.bekmnsrw.feature.auth.api.AuthConstant
import com.bekmnsrw.feature.auth.impl.presentation.AuthScreenModel
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {

    private val splashScreenModel by inject<SplashScreenModel>()
    private val authScreenModel by inject<AuthScreenModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().setKeepOnScreenCondition { !splashScreenModel.isLoading.value }

        setContent {
            AniLibTheme {
                NavHost()
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        println("onNewIntent")
        val authCode = intent?.data?.getQueryParameter(AuthConstant.RESPONSE_TYPE)
        if (authCode != null) authScreenModel.getAccessToken(authCode = authCode)
    }
}
