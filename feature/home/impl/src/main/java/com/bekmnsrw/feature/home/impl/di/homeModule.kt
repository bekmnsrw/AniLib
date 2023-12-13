package com.bekmnsrw.feature.home.impl.di

import com.bekmnsrw.core.network.qualifier.Qualifiers
import com.bekmnsrw.feature.home.api.repository.HomeRepository
import com.bekmnsrw.feature.home.api.usecase.GetAnimeListUseCase
import com.bekmnsrw.feature.home.api.usecase.GetAnimeUseCase
import com.bekmnsrw.feature.home.impl.data.AnimePagingSource
import com.bekmnsrw.feature.home.impl.data.HomeRepositoryImpl
import com.bekmnsrw.feature.home.impl.data.datasource.remote.HomeApi
import com.bekmnsrw.feature.home.impl.presentation.details.DetailsScreenModel
import com.bekmnsrw.feature.home.impl.presentation.home.HomeScreenModel
import com.bekmnsrw.feature.home.impl.presentation.list.MoreAnimeListScreenModel
import com.bekmnsrw.feature.home.impl.usecase.GetAnimeListUseCaseImpl
import com.bekmnsrw.feature.home.impl.usecase.GetAnimeUseCaseImpl
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit

val homeModule = module {
    factory<HomeApi> {
        provideHomeApi(
            retrofit = get(qualifier = named(Qualifiers.API_RETROFIT))
        )
    }

    factory<HomeRepository> {
        provideHomeRepository(
            homeApi = get()
        )
    }

    factory<AnimePagingSource> {
        provideAnimePagingSource(
            homeApi = get(),
            status = getProperty("status"),
            order = getProperty("order")
        )
    }

    factory<GetAnimeListUseCase> {
        provideGetAnimeListUseCase(
            homeRepository = get()
        )
    }

    factory<GetAnimeUseCase> {
        provideGetAnimeUseCase(
            homeRepository = get()
        )
    }

    factory<HomeScreenModel> {
        provideHomeScreenModel(
            getAnimeListUseCase = get()
        )
    }

    factory<MoreAnimeListScreenModel> {
        provideAnimeListScreenModel(
            homeRepository = get(),
            status = getProperty("status")
        )
    }

    factory<DetailsScreenModel> {
        provideDetailsScreenModel(
            getAnimeUseCase = get(),
            animeId = getProperty("animeId")
        )
    }
}

private fun provideHomeApi(
    retrofit: Retrofit
): HomeApi = retrofit.create(HomeApi::class.java)

private fun provideHomeRepository(
    homeApi: HomeApi
): HomeRepository = HomeRepositoryImpl(
    homeApi = homeApi
)

private fun provideHomeScreenModel(
    getAnimeListUseCase: GetAnimeListUseCase
): HomeScreenModel = HomeScreenModel(
    getAnimeListUseCase = getAnimeListUseCase
)

private fun provideAnimePagingSource(
    homeApi: HomeApi,
    status: String,
    order: String
): AnimePagingSource = AnimePagingSource(
    homeApi = homeApi,
    status = status,
    order = order
)

private fun provideGetAnimeListUseCase(
    homeRepository: HomeRepository
): GetAnimeListUseCase = GetAnimeListUseCaseImpl(
    homeRepository = homeRepository
)

private fun provideAnimeListScreenModel(
    homeRepository: HomeRepository,
    status: String
): MoreAnimeListScreenModel = MoreAnimeListScreenModel(
    homeRepository = homeRepository,
    status = status
)

private fun provideGetAnimeUseCase(
    homeRepository: HomeRepository
): GetAnimeUseCase = GetAnimeUseCaseImpl(
    homeRepository = homeRepository
)

private fun provideDetailsScreenModel(
    getAnimeUseCase: GetAnimeUseCase,
    animeId: Int
): DetailsScreenModel = DetailsScreenModel(
    getAnimeUseCase = getAnimeUseCase,
    animeId = animeId
)
