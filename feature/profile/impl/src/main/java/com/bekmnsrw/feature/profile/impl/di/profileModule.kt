package com.bekmnsrw.feature.profile.impl.di

import com.bekmnsrw.core.network.qualifier.Qualifiers
import com.bekmnsrw.feature.auth.api.usecase.local.IsAuthenticatedUseCase
import com.bekmnsrw.feature.auth.api.usecase.local.SaveUserIdUseCase
import com.bekmnsrw.feature.profile.api.repository.ProfileRepository
import com.bekmnsrw.feature.profile.api.usecase.GetProfileUseCase
import com.bekmnsrw.feature.profile.api.usecase.GetUserAnimeByStatusUseCase
import com.bekmnsrw.feature.profile.api.usecase.GetUserAnimeRatesUseCase
import com.bekmnsrw.feature.profile.impl.data.ProfileRepositoryImpl
import com.bekmnsrw.feature.profile.impl.data.datasource.remote.ProfileApi
import com.bekmnsrw.feature.profile.impl.presentation.ProfileScreenModel
import com.bekmnsrw.feature.profile.impl.usecase.GetProfileUseCaseImpl
import com.bekmnsrw.feature.profile.impl.usecase.GetUserAnimeByStatusUseCaseImpl
import com.bekmnsrw.feature.profile.impl.usecase.GetUserAnimeRatesUseCaseImpl
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit

val profileModule = module {
    factory<ProfileScreenModel> {
        provideProfileScreenModel(
            isAuthenticatedUseCase = get(),
            getProfileUseCase = get(),
            saveUserIdUseCase = get(),
            getUserAnimeRatesUseCase = get(),
            getUserAnimeByStatusUseCase = get()
        )
    }

    factory<ProfileRepository> {
        provideProfileRepository(profileApi = get())
    }

    factory<GetProfileUseCase> {
        provideGetProfileUseCase(profileRepository = get())
    }

    factory<GetUserAnimeRatesUseCase> {
        provideGetUserAnimeRatesUseCase(profileRepository = get())
    }

    factory<GetUserAnimeByStatusUseCase> {
        provideGetUserAnimeByStatusUseCase(profileRepository = get())
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
    saveUserIdUseCase: SaveUserIdUseCase,
    getUserAnimeRatesUseCase: GetUserAnimeRatesUseCase,
    getUserAnimeByStatusUseCase: GetUserAnimeByStatusUseCase
): ProfileScreenModel = ProfileScreenModel(
    isAuthenticatedUseCase = isAuthenticatedUseCase,
    getProfileUseCase = getProfileUseCase,
    saveUserIdUseCase = saveUserIdUseCase,
    getUserAnimeRatesUseCase = getUserAnimeRatesUseCase,
    getUserAnimeByStatusUseCase = getUserAnimeByStatusUseCase
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

private fun provideGetUserAnimeRatesUseCase(
    profileRepository: ProfileRepository
): GetUserAnimeRatesUseCase = GetUserAnimeRatesUseCaseImpl(
    profileRepository = profileRepository
)

private fun provideGetUserAnimeByStatusUseCase(
    profileRepository: ProfileRepository
): GetUserAnimeByStatusUseCase = GetUserAnimeByStatusUseCaseImpl(
    profileRepository = profileRepository
)
