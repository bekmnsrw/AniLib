package com.bekmnsrw.feature.auth.api.usecase.local

import kotlinx.coroutines.flow.Flow

interface GetUserIdUseCase {

    suspend operator fun invoke(): Flow<Int?>
}
