package com.bekmnsrw.feature.profile.impl.data

import com.bekmnsrw.feature.profile.api.model.AnimeRates
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

    override suspend fun getUserAnimeRates(id: Int): Flow<List<AnimeRates>> = flow {
        emit(
            profileApi
                .getUserAnimeRates(id = id)
                .toAnimeRatesList()
        )
    }

    override suspend fun getUserAnimeByStatus(
        id: Int,
        status: String
    ): Flow<List<AnimeRates>> = flow {
        emit(
            profileApi
                .getUserAnimeByStatus(id = id, status = status)
                .toAnimeRatesList()
        )
    }
}
