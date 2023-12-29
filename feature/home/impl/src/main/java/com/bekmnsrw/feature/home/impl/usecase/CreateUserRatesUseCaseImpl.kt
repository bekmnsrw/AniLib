package com.bekmnsrw.feature.home.impl.usecase

import com.bekmnsrw.feature.home.api.model.UserRates
import com.bekmnsrw.feature.home.api.repository.HomeRepository
import com.bekmnsrw.feature.home.api.usecase.CreateUserRatesUseCase
import kotlinx.coroutines.flow.Flow

class CreateUserRatesUseCaseImpl(
    private val homeRepository: HomeRepository
) : CreateUserRatesUseCase {

    override suspend fun invoke(
        userId: Int,
        targetId: Int,
        status: String
    ): Flow<UserRates> = homeRepository.createUserRates(
        userId = userId,
        targetId = targetId,
        status = status
    )
}
