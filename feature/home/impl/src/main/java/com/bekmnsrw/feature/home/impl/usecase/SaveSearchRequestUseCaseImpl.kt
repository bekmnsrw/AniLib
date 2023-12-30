package com.bekmnsrw.feature.home.impl.usecase

import com.bekmnsrw.feature.home.api.model.SearchRequest
import com.bekmnsrw.feature.home.api.repository.HomeRepository
import com.bekmnsrw.feature.home.api.usecase.SaveSearchRequestUseCase

internal class SaveSearchRequestUseCaseImpl(
    private val homeRepository: HomeRepository
) : SaveSearchRequestUseCase {

    override suspend fun invoke(
        searchRequest: SearchRequest
    ) = homeRepository.saveSearchRequest(
        searchRequest = searchRequest
    )
}
