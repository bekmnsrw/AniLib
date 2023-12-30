package com.bekmnsrw.feature.home.api.usecase

import com.bekmnsrw.feature.home.api.model.SearchRequest

interface SaveSearchRequestUseCase {

    suspend operator fun invoke(searchRequest: SearchRequest)
}
