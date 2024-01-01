package com.bekmnsrw.feature.profile.api.usecase

import kotlinx.coroutines.flow.Flow

interface SignOutUseCase {

    suspend operator fun invoke(): Flow<Int>
}
