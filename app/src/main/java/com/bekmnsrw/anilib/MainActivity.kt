package com.bekmnsrw.anilib

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.bekmnsrw.anilib.splash.SplashScreenModel
import com.bekmnsrw.core.designsystem.theme.AniLibTheme
import com.bekmnsrw.feature.auth.api.AuthConstant
import com.bekmnsrw.feature.auth.impl.presentation.AuthScreenModel
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.logEvent
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        when (isGranted) {
            true -> { /* FCM SDK (and your app) can post notifications */ }
            false -> { /* Inform user that that your app will not show notifications */ }
        }
    }

    private fun askNotificationPermission() {
        /* This is only necessary for API level >= 33 (TIRAMISU) */
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> when {
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED -> {
                    /* FCM SDK (and your app) can post notifications */
                }

                shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS) -> {
                    /*
                    * Display an educational UI explaining to the user the features that will be enabled
                    * by them granting the POST_NOTIFICATION permission. This UI should provide the user
                    * "OK" and "No thanks" buttons. If the user selects "OK," directly request the permission.
                    * If the user selects "No thanks," allow the user to continue without notifications.
                    */
                }

                else -> {
                    /* Directly ask for the permission */
                    requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        }
    }

    private val splashScreenModel by inject<SplashScreenModel>()
    private val authScreenModel by inject<AuthScreenModel>()

    private lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseAnalytics = Firebase.analytics

        askNotificationPermission()

        installSplashScreen().setKeepOnScreenCondition {
            !splashScreenModel.isLoading.value
        }

        CoroutineScope(Dispatchers.IO).launch {
            /* Save FCM registration token to backend */
            Log.d("FCM registration token", Firebase.messaging.token.await())
        }

        setContent {
            AniLibTheme {
                NavHost()
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        Log.e("MainActivity", "onNewIntent")
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
