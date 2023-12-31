package com.bekmnsrw.feature.auth.impl.di

import android.content.Context
import com.bekmnsrw.core.network.qualifier.Qualifiers
import com.bekmnsrw.feature.auth.api.repository.AuthRepository
import com.bekmnsrw.feature.auth.api.usecase.local.GetLocalAccessTokenUseCase
import com.bekmnsrw.feature.auth.api.usecase.local.GetLocalRefreshTokenUseCase
import com.bekmnsrw.feature.auth.api.usecase.local.GetUserIdUseCase
import com.bekmnsrw.feature.auth.api.usecase.local.IsAuthenticatedUseCase
import com.bekmnsrw.feature.auth.api.usecase.local.OnAuthenticationUseCase
import com.bekmnsrw.feature.auth.api.usecase.local.OnSignOutUseCase
import com.bekmnsrw.feature.auth.api.usecase.local.SaveLocalAccessTokenUseCase
import com.bekmnsrw.feature.auth.api.usecase.local.SaveLocalRefreshTokenUseCase
import com.bekmnsrw.feature.auth.api.usecase.local.SaveUserIdUseCase
import com.bekmnsrw.feature.auth.api.usecase.remote.GetRemoteAccessTokenUseCase
import com.bekmnsrw.feature.auth.api.usecase.remote.RefreshAccessTokenUseCase
import com.bekmnsrw.feature.auth.impl.data.AuthRepositoryImpl
import com.bekmnsrw.feature.auth.impl.data.datasource.local.AuthDataStore
import com.bekmnsrw.feature.auth.impl.data.datasource.remote.AuthApi
import com.bekmnsrw.feature.auth.impl.presentation.AuthScreenModel
import com.bekmnsrw.feature.auth.impl.usecase.local.GetLocalAccessTokenUseCaseImpl
import com.bekmnsrw.feature.auth.impl.usecase.local.GetLocalRefreshTokenUseCaseImpl
import com.bekmnsrw.feature.auth.impl.usecase.local.GetUserIdUseCaseImpl
import com.bekmnsrw.feature.auth.impl.usecase.local.IsAuthenticatedUseCaseImpl
import com.bekmnsrw.feature.auth.impl.usecase.local.OnAuthenticationUseCaseImpl
import com.bekmnsrw.feature.auth.impl.usecase.local.OnSignOutUseCaseImpl
import com.bekmnsrw.feature.auth.impl.usecase.local.SaveLocalAccessTokenUseCaseImpl
import com.bekmnsrw.feature.auth.impl.usecase.local.SaveLocalRefreshTokenUseCaseImpl
import com.bekmnsrw.feature.auth.impl.usecase.local.SaveUserIdUseCaseImpl
import com.bekmnsrw.feature.auth.impl.usecase.remote.GetRemoteAccessTokenUseCaseImpl
import com.bekmnsrw.feature.auth.impl.usecase.remote.RefreshAccessTokenUseCaseImpl
import org.koin.android.ext.koin.androidApplication
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit

val authModule = module {
    factory<AuthRepository> {
        provideAuthRepository(
            authApi = get(),
            authDataStore = get()
        )
    }

    factory<AuthDataStore> {
        provideAuthDataStore(context = androidApplication().applicationContext)
    }

    factory<AuthApi> {
        provideAuthApi(retrofit = get(qualifier = named(Qualifiers.AUTH_RETROFIT)))
    }

    factory<GetRemoteAccessTokenUseCase> {
        provideGetRemoteAccessTokenUseCase(authRepository = get())
    }

    factory<RefreshAccessTokenUseCase> {
        provideRefreshAccessTokenUseCase(authRepository = get())
    }

    factory<IsAuthenticatedUseCase> {
        provideIsAuthenticatedUseCase(authRepository = get())
    }

    factory<OnAuthenticationUseCase> {
        provideOnAuthenticationUseCase(authRepository = get())
    }

    factory<GetLocalRefreshTokenUseCase> {
        provideGetLocalRefreshTokenUseCase(authDataStore = get())
    }

    factory<GetLocalAccessTokenUseCase> {
        provideGetLocalAccessTokenUseCase(authDataStore = get())
    }

    factory<SaveLocalAccessTokenUseCase> {
        provideSaveLocalAccessTokenUseCase(authDataStore = get())
    }

    factory<SaveLocalRefreshTokenUseCase> {
        provideSaveLocalRefreshTokenUseCase(authDataStore = get())
    }

    factory<SaveUserIdUseCase> {
        provideSaveUserIdUseCase(authRepository = get())
    }

    factory<GetUserIdUseCase> {
        provideGetUserIdUseCase(authRepository = get())
    }

    factory<OnSignOutUseCase> {
        provideOnSignOutUseCase(authRepository = get())
    }

    single<AuthScreenModel> {
        provideAuthScreenModel(
            getRemoteAccessTokenUseCase = get(),
            onAuthenticationUseCase = get(),
            saveLocalRefreshTokenUseCase = get(),
            saveLocalAccessTokenUseCase = get()
        )
    }
}

private fun provideAuthRepository(
    authApi: AuthApi,
    authDataStore: AuthDataStore
): AuthRepository = AuthRepositoryImpl(
    authApi = authApi,
    authDataStore = authDataStore
)

private fun provideGetRemoteAccessTokenUseCase(
    authRepository: AuthRepository
): GetRemoteAccessTokenUseCase = GetRemoteAccessTokenUseCaseImpl(
    authRepository = authRepository
)

private fun provideAuthApi(
    retrofit: Retrofit
): AuthApi = retrofit.create(AuthApi::class.java)

private fun provideRefreshAccessTokenUseCase(
    authRepository: AuthRepository
): RefreshAccessTokenUseCase = RefreshAccessTokenUseCaseImpl(
    authRepository = authRepository
)

private fun provideAuthDataStore(
    context: Context
): AuthDataStore = AuthDataStore(
    context = context
)

private fun provideIsAuthenticatedUseCase(
    authRepository: AuthRepository
): IsAuthenticatedUseCase = IsAuthenticatedUseCaseImpl(
    authRepository = authRepository
)

private fun provideOnAuthenticationUseCase(
    authRepository: AuthRepository
): OnAuthenticationUseCase = OnAuthenticationUseCaseImpl(
    authRepository = authRepository
)

private fun provideAuthScreenModel(
    getRemoteAccessTokenUseCase: GetRemoteAccessTokenUseCase,
    onAuthenticationUseCase: OnAuthenticationUseCase,
    saveLocalAccessTokenUseCase: SaveLocalAccessTokenUseCase,
    saveLocalRefreshTokenUseCase: SaveLocalRefreshTokenUseCase
): AuthScreenModel = AuthScreenModel(
    getRemoteAccessTokenUseCase = getRemoteAccessTokenUseCase,
    onAuthenticationUseCase = onAuthenticationUseCase,
    saveLocalAccessTokenUseCase = saveLocalAccessTokenUseCase,
    saveLocalRefreshTokenUseCase = saveLocalRefreshTokenUseCase
)

private fun provideGetLocalAccessTokenUseCase(
    authDataStore: AuthDataStore
): GetLocalAccessTokenUseCase = GetLocalAccessTokenUseCaseImpl(
    authDataStore = authDataStore
)

private fun provideGetLocalRefreshTokenUseCase(
    authDataStore: AuthDataStore
): GetLocalRefreshTokenUseCase = GetLocalRefreshTokenUseCaseImpl(
    authDataStore = authDataStore
)

private fun provideSaveLocalAccessTokenUseCase(
    authDataStore: AuthDataStore
): SaveLocalAccessTokenUseCase = SaveLocalAccessTokenUseCaseImpl(
    authDataStore = authDataStore
)

private fun provideSaveLocalRefreshTokenUseCase(
    authDataStore: AuthDataStore
): SaveLocalRefreshTokenUseCase = SaveLocalRefreshTokenUseCaseImpl(
    authDataStore = authDataStore
)

private fun provideGetUserIdUseCase(
    authRepository: AuthRepository
): GetUserIdUseCase = GetUserIdUseCaseImpl(
    authRepository = authRepository
)

private fun provideSaveUserIdUseCase(
    authRepository: AuthRepository
): SaveUserIdUseCase = SaveUserIdUseCaseImpl(
    authRepository = authRepository
)

private fun provideOnSignOutUseCase(
    authRepository: AuthRepository
): OnSignOutUseCase = OnSignOutUseCaseImpl(
    authRepository = authRepository
)
