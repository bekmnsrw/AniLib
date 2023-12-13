package com.bekmnsrw.feature.profile.impl.data

import com.bekmnsrw.feature.profile.api.model.WhoAmI
import com.bekmnsrw.feature.profile.api.repository.ProfileRepository
import com.bekmnsrw.feature.profile.impl.data.datasource.remote.ProfileApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

internal class ProfileRepositoryImpl(
    private val profileApi: ProfileApi
) : ProfileRepository {

    override suspend fun getProfile(): Flow<WhoAmI> = flow {
        emit(
            profileApi.getProfile().toWhoAmI()
        )
    }
}
