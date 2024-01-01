package com.bekmnsrw.feature.profile.api.repository

import com.bekmnsrw.feature.profile.api.model.AnimeRates
import com.bekmnsrw.feature.profile.api.model.WhoAmI
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {

    suspend fun getProfile(): Flow<WhoAmI>
    suspend fun getUserAnimeRates(id: Int): Flow<List<AnimeRates>>
    suspend fun getUserAnimeByStatus(
        id: Int,
        status: String
    ): Flow<List<AnimeRates>>
    suspend fun signOut(): Flow<Int>
}
