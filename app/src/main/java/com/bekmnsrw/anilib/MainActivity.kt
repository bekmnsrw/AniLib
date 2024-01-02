package com.bekmnsrw.anilib

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.bekmnsrw.core.designsystem.theme.AniLibTheme
import com.bekmnsrw.feature.auth.api.AuthConstant
import com.bekmnsrw.feature.auth.impl.presentation.AuthScreenModel
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.logEvent
import com.google.firebase.ktx.Firebase
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {

    private val splashScreenModel by inject<SplashScreenModel>()
    private val authScreenModel by inject<AuthScreenModel>()

    private lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseAnalytics = Firebase.analytics

        installSplashScreen().setKeepOnScreenCondition {
            !splashScreenModel.isLoading.value
        }

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
        if (authCode != null) {
            authScreenModel.getAccessToken(authCode = authCode)

            /* Firebase analytics example */
            firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SIGN_UP) {
                param(FirebaseAnalytics.Param.SUCCESS, "The user has signed up")
                param(FirebaseAnalytics.Param.METHOD, "OAuth2")
            }
        }
    }
}
