package com.bekmnsrw.feature.home.api.usecase

import kotlinx.coroutines.flow.Flow

interface DeleteUserRatesUseCase {

    suspend operator fun invoke(id: Int): Flow<Int>
}
