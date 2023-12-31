package com.bekmnsrw.core.network.di

import com.bekmnsrw.core.network.authenticator.AuthAuthenticator
import com.bekmnsrw.core.network.interceptor.BearerInterceptor
import com.bekmnsrw.core.network.qualifier.Qualifiers.API_OKHTTP_CLIENT
import com.bekmnsrw.core.network.qualifier.Qualifiers.API_RETROFIT
import com.bekmnsrw.core.network.qualifier.Qualifiers.AUTH_OKHTTP_CLIENT
import com.bekmnsrw.core.network.qualifier.Qualifiers.AUTH_RETROFIT
import com.bekmnsrw.core.network.qualifier.Qualifiers.BEARER_INTERCEPTOR
import com.bekmnsrw.core.network.qualifier.Qualifiers.LOGGING_INTERCEPTOR
import com.bekmnsrw.feature.auth.api.usecase.local.GetLocalAccessTokenUseCase
import com.bekmnsrw.feature.auth.api.usecase.local.GetLocalRefreshTokenUseCase
import com.bekmnsrw.feature.auth.api.usecase.local.IsAuthenticatedUseCase
import com.bekmnsrw.feature.auth.api.usecase.local.SaveLocalAccessTokenUseCase
import com.bekmnsrw.feature.auth.api.usecase.local.SaveLocalRefreshTokenUseCase
import com.bekmnsrw.feature.auth.api.usecase.remote.RefreshAccessTokenUseCase
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.Authenticator
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Converter
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

private const val API_BASE_URL = "https://shikimori.one/api/"
private const val AUTH_BASE_URL = "https://shikimori.one/oauth/"
private const val CONNECTION_TIMEOUT = 30L
private const val CONTENT_TYPE = "application/json"

val networkModule = module {
    single<OkHttpClient>(qualifier = named(API_OKHTTP_CLIENT)) {
        provideApiOkHttpclient(
            loggingInterceptor = get(qualifier = named(LOGGING_INTERCEPTOR)),
            bearerInterceptor = get(qualifier = named(BEARER_INTERCEPTOR)),
            authenticator = get()
        )
    }

    single<OkHttpClient>(qualifier = named(AUTH_OKHTTP_CLIENT)) {
        provideAuthOkHttpClient(
            loggingInterceptor = get(qualifier = named(LOGGING_INTERCEPTOR))
        )
    }

    single<Retrofit>(qualifier = named(API_RETROFIT)) {
        provideApiRetrofit(
            okHttpClient = get(qualifier = named(API_OKHTTP_CLIENT)),
            converterFactory = get()
        )
    }

    single<Retrofit>(qualifier = named(AUTH_RETROFIT)) {
        provideAuthRetrofit(
            okHttpClient = get(qualifier = named(AUTH_OKHTTP_CLIENT)),
            converterFactory = get()
        )
    }

    factory<Interceptor>(qualifier = named(LOGGING_INTERCEPTOR)) {
        provideLoggingInterceptor()
    }

    factory<Interceptor>(qualifier = named(BEARER_INTERCEPTOR)) {
        provideBearerInterceptor(getLocalAccessTokenUseCase = get())
    }

    factory<Authenticator> {
        provideAuthenticator(
            getLocalRefreshTokenUseCase = get(),
            refreshAccessTokenUseCase = get(),
            saveLocalRefreshTokenUseCase = get(),
            saveLocalAccessTokenUseCase = get(),
            isAuthenticatedUseCase = get()
        )
    }

    single<Converter.Factory> { provideConverterFactory() }
}

private fun provideApiOkHttpclient(
    loggingInterceptor: Interceptor,
    bearerInterceptor: Interceptor,
    authenticator: Authenticator
): OkHttpClient = OkHttpClient.Builder()
    .addInterceptor(loggingInterceptor)
    .addInterceptor(bearerInterceptor)
    .authenticator(authenticator)
    .connectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
    .build()

private fun provideAuthOkHttpClient(
    loggingInterceptor: Interceptor
): OkHttpClient = OkHttpClient.Builder()
    .addInterceptor(loggingInterceptor)
    .connectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
    .build()

private fun provideApiRetrofit(
    okHttpClient: OkHttpClient,
    converterFactory: Converter.Factory,
): Retrofit = Retrofit.Builder()
    .baseUrl(API_BASE_URL)
    .client(okHttpClient)
    .addConverterFactory(converterFactory)
    .build()

private fun provideAuthRetrofit(
    okHttpClient: OkHttpClient,
    converterFactory: Converter.Factory
): Retrofit = Retrofit.Builder()
    .baseUrl(AUTH_BASE_URL)
    .client(okHttpClient)
    .addConverterFactory(converterFactory)
    .build()

private fun provideLoggingInterceptor(): Interceptor = HttpLoggingInterceptor().apply {
    level = HttpLoggingInterceptor.Level.BODY
}

private fun provideBearerInterceptor(
    getLocalAccessTokenUseCase: GetLocalAccessTokenUseCase
): Interceptor = BearerInterceptor(
    getLocalAccessTokenUseCase = getLocalAccessTokenUseCase
)

private fun provideAuthenticator(
    getLocalRefreshTokenUseCase: GetLocalRefreshTokenUseCase,
    refreshAccessTokenUseCase: RefreshAccessTokenUseCase,
    saveLocalRefreshTokenUseCase: SaveLocalRefreshTokenUseCase,
    saveLocalAccessTokenUseCase: SaveLocalAccessTokenUseCase,
    isAuthenticatedUseCase: IsAuthenticatedUseCase
): Authenticator = AuthAuthenticator(
    getLocalRefreshTokenUseCase = getLocalRefreshTokenUseCase,
    refreshAccessTokenUseCase = refreshAccessTokenUseCase,
    saveLocalRefreshTokenUseCase = saveLocalRefreshTokenUseCase,
    saveLocalAccessTokenUseCase = saveLocalAccessTokenUseCase,
    isAuthenticatedUseCase = isAuthenticatedUseCase
)

private val json = Json { ignoreUnknownKeys = true }

private fun provideConverterFactory(): Converter.Factory = json.asConverterFactory(
    CONTENT_TYPE.toMediaType()
)
