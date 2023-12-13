package com.bekmnsrw.feature.auth.api.usecase.local

import kotlinx.coroutines.flow.Flow

interface IsFirstAppLaunchUseCase {

    suspend operator fun invoke(): Flow<Boolean?>
}
