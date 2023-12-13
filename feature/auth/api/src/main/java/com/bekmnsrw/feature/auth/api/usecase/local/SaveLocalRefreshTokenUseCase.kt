package com.bekmnsrw.feature.auth.api.usecase.local

import androidx.datastore.preferences.core.Preferences

interface SaveLocalRefreshTokenUseCase {

    suspend operator fun invoke(refreshToken: String): Preferences
}
