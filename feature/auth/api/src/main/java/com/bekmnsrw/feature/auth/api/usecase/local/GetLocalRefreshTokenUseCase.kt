package com.bekmnsrw.feature.auth.api.usecase.local

import kotlinx.coroutines.flow.Flow

interface GetLocalRefreshTokenUseCase {

    suspend operator fun invoke(): Flow<String?>
}
