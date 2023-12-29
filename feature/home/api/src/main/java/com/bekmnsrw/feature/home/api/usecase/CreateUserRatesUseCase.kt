package com.bekmnsrw.feature.home.api.usecase

import com.bekmnsrw.feature.home.api.model.UserRates
import kotlinx.coroutines.flow.Flow

interface CreateUserRatesUseCase {

    suspend operator fun invoke(
        userId: Int,
        targetId: Int,
        status: String
    ): Flow<UserRates>
}
