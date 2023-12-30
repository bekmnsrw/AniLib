package com.bekmnsrw.feature.home.impl.usecase

import com.bekmnsrw.feature.home.api.repository.HomeRepository
import com.bekmnsrw.feature.home.api.usecase.DeleteSearchRequestByIdUseCase

internal class DeleteSearchRequestByIdUseCaseImpl(
    private val homeRepository: HomeRepository
) : DeleteSearchRequestByIdUseCase {

    override suspend fun invoke(
        id: Int
    ) = homeRepository.deleteSearchRequestById(
        id = id
    )
}
