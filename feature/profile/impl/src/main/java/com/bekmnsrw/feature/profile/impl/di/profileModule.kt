package com.bekmnsrw.feature.profile.impl.di

import com.bekmnsrw.core.network.qualifier.Qualifiers
import com.bekmnsrw.feature.auth.api.usecase.local.IsAuthenticatedUseCase
import com.bekmnsrw.feature.auth.api.usecase.local.SaveUserIdUseCase
import com.bekmnsrw.feature.profile.api.repository.ProfileRepository
import com.bekmnsrw.feature.profile.api.usecase.remote.GetProfileUseCase
import com.bekmnsrw.feature.profile.impl.data.ProfileRepositoryImpl
import com.bekmnsrw.feature.profile.impl.data.datasource.remote.ProfileApi
import com.bekmnsrw.feature.profile.impl.presentation.ProfileScreenModel
import com.bekmnsrw.feature.profile.impl.usecase.remote.GetProfileUseCaseImpl
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit

val profileModule = module {
    factory<ProfileScreenModel> {
        provideProfileScreenModel(
            isAuthenticatedUseCase = get(),
            getProfileUseCase = get(),
            saveUserIdUseCase = get()
        )
    }

    factory<ProfileRepository> {
        provideProfileRepository(profileApi = get())
    }

    factory<GetProfileUseCase> {
        provideGetProfileUseCase(profileRepository = get())
    }

    factory<ProfileApi> {
        provideProfileApi(
            retrofit = get(qualifier = named(Qualifiers.API_RETROFIT))
        )
    }
}

private fun provideProfileScreenModel(
    isAuthenticatedUseCase: IsAuthenticatedUseCase,
    getProfileUseCase: GetProfileUseCase,
    saveUserIdUseCase: SaveUserIdUseCase
): ProfileScreenModel = ProfileScreenModel(
    isAuthenticatedUseCase = isAuthenticatedUseCase,
    getProfileUseCase = getProfileUseCase,
    saveUserIdUseCase = saveUserIdUseCase
)

private fun provideGetProfileUseCase(
    profileRepository: ProfileRepository
): GetProfileUseCase = GetProfileUseCaseImpl(
    profileRepository = profileRepository
)

private fun provideProfileRepository(
    profileApi: ProfileApi
): ProfileRepository = ProfileRepositoryImpl(
    profileApi = profileApi
)

private fun provideProfileApi(
  retrofit: Retrofit
): ProfileApi = retrofit.create(ProfileApi::class.java)
