package com.bekmnsrw.feature.home.api.usecase

import com.bekmnsrw.feature.home.api.model.SearchRequest
import kotlinx.coroutines.flow.Flow

interface GetAllSearchRequestsUseCase {

    suspend operator fun invoke(): Flow<List<SearchRequest>>
}
