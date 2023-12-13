package com.bekmnsrw.feature.auth.api.usecase.local

import androidx.datastore.preferences.core.Preferences

interface SaveLocalAccessTokenUseCase {

    suspend operator fun invoke(accessToken: String): Preferences
}
