package com.bekmnsrw.feature.home.impl.usecase

import com.bekmnsrw.feature.home.api.model.SearchRequest
import com.bekmnsrw.feature.home.api.repository.HomeRepository
import com.bekmnsrw.feature.home.api.usecase.GetAllSearchRequestsUseCase
import kotlinx.coroutines.flow.Flow

class GetAllSearchRequestsUseCaseImpl(
    private val homeRepository: HomeRepository
) : GetAllSearchRequestsUseCase {

    override suspend fun invoke(): Flow<List<SearchRequest>> = homeRepository.getAllSearchRequests()
}
