package com.bekmnsrw.feature.profile.api.usecase.remote

import com.bekmnsrw.feature.profile.api.model.WhoAmI
import kotlinx.coroutines.flow.Flow

interface GetProfileUseCase {

    suspend operator fun invoke(): Flow<WhoAmI>
}
