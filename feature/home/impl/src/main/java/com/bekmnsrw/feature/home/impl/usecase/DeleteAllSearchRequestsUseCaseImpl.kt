package com.bekmnsrw.feature.home.impl.usecase

import com.bekmnsrw.feature.home.api.repository.HomeRepository
import com.bekmnsrw.feature.home.api.usecase.DeleteAllSearchRequestsUseCase

class DeleteAllSearchRequestsUseCaseImpl(
    private val homeRepository: HomeRepository
) : DeleteAllSearchRequestsUseCase {

    override suspend fun invoke() = homeRepository.deleteAllSearchRequests()
}
