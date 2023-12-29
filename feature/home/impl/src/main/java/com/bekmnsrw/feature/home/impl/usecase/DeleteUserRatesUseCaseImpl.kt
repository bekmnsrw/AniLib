package com.bekmnsrw.feature.home.impl.usecase

import com.bekmnsrw.feature.home.api.repository.HomeRepository
import com.bekmnsrw.feature.home.api.usecase.DeleteUserRatesUseCase
import kotlinx.coroutines.flow.Flow

class DeleteUserRatesUseCaseImpl(
    private val homeRepository: HomeRepository
) : DeleteUserRatesUseCase {

    override suspend fun invoke(id: Int): Flow<Int> = homeRepository.deleteUserRates(id = id)
}
