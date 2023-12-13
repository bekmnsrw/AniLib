package com.bekmnsrw.feature.profile.api.repository

import com.bekmnsrw.feature.profile.api.model.WhoAmI
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {

    suspend fun getProfile(): Flow<WhoAmI>
}
