package com.bekmnsrw.feature.home.api.usecase

interface DeleteSearchRequestByIdUseCase {

    suspend operator fun invoke(id: Int)
}
