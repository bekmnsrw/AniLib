package com.bekmnsrw.feature.auth.api.usecase.local

import androidx.datastore.preferences.core.Preferences

interface SaveUserIdUseCase {

    suspend operator fun invoke(id: Int): Preferences
}
