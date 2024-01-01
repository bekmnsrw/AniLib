package com.bekmnsrw.feature.auth.api.usecase.local

import androidx.datastore.preferences.core.Preferences

interface OnSignOutUseCase {

    suspend operator fun invoke(): Preferences
}
