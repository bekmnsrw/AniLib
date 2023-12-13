package com.bekmnsrw.feature.auth.api.usecase.local

import kotlinx.coroutines.flow.Flow

interface IsAuthenticatedUseCase {

    suspend operator fun invoke(): Flow<Boolean?>
}
