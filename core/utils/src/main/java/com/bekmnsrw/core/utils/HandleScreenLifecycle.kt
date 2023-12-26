package com.bekmnsrw.core.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner

@Composable
fun HandleScreenLifecycle(
    onCreate: () -> Unit = {},
    onStart: () -> Unit = {},
    onResume: () -> Unit = {},
    onPause: () -> Unit = {},
    onStop: () -> Unit = {},
    onDestroy: () -> Unit = {}
) {
    val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
    val currentOnCreate by rememberUpdatedState(newValue = onCreate)
    val currentOnStart by rememberUpdatedState(newValue = onStart)
    val currentOnResume by rememberUpdatedState(newValue = onResume)
    val currentOnPause by rememberUpdatedState(newValue = onPause)
    val currentOnStop by rememberUpdatedState(newValue = onStop)
    val currentOnDestroy by rememberUpdatedState(newValue = onDestroy)

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_CREATE -> currentOnCreate()
                Lifecycle.Event.ON_START -> currentOnStart()
                Lifecycle.Event.ON_RESUME -> currentOnResume()
                Lifecycle.Event.ON_PAUSE -> currentOnPause()
                Lifecycle.Event.ON_STOP -> currentOnStop()
                Lifecycle.Event.ON_DESTROY -> currentOnDestroy()
                else -> {}
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }
}
