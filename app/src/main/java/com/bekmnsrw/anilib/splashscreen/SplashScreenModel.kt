package com.bekmnsrw.anilib.splashscreen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.bekmnsrw.feature.auth.api.usecase.local.IsFirstAppLaunchUseCase
import com.bekmnsrw.feature.auth.api.usecase.local.OnFirstAppLaunchUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.launch

class SplashScreenModel(
    private val isFirstAppLaunchUseCase: IsFirstAppLaunchUseCase,
    private val onFirstAppLaunchUseCase: OnFirstAppLaunchUseCase
) : ScreenModel {

    private val _isLoading: MutableState<Boolean> = mutableStateOf(true)
    val isLoading: State<Boolean> = _isLoading

    private val _isFirstAppLaunch: MutableState<Boolean?> = mutableStateOf(null)
    val isFirstAppLaunch: State<Boolean?> = _isFirstAppLaunch

    init { isFirstAppLaunch() }

    private fun isFirstAppLaunch() = screenModelScope.launch {
        isFirstAppLaunchUseCase()
            .flowOn(Dispatchers.IO)
            .onCompletion { _isLoading.value = false }
            .collect {
                if (it == null) {
                    _isFirstAppLaunch.value = true
                    onFirstAppLaunchUseCase()
                } else {
                    _isFirstAppLaunch.value = false
                }
            }
    }

//    private suspend fun isFirstAppLaunch() = suspendCancellableCoroutine { continuation ->
//        screenModelScope.launch {
//            val isFirstAppLaunch = isFirstAppLaunchUseCase()
//                .flowOn(Dispatchers.IO)
//                .onCompletion { _isLoading.value = true }
//                .first()
//
//            continuation.resume(isFirstAppLaunch)
//        }
//    }
}
