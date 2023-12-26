package com.bekmnsrw.feature.home.impl.di

import com.bekmnsrw.core.network.qualifier.Qualifiers
import com.bekmnsrw.feature.home.api.repository.HomeRepository
import com.bekmnsrw.feature.home.api.usecase.AddToFavoritesUseCase
import com.bekmnsrw.feature.home.api.usecase.GetAnimeListUseCase
import com.bekmnsrw.feature.home.api.usecase.GetAnimeUseCase
import com.bekmnsrw.feature.home.api.usecase.GetSimilarAnimeListUseCase
import com.bekmnsrw.feature.home.api.usecase.RemoveFromFavoritesUseCase
import com.bekmnsrw.feature.home.impl.HomeConstants.ANIME_ID_KOIN_PROPERTY
import com.bekmnsrw.feature.home.impl.HomeConstants.STATUS_KOIN_PROPERTY
import com.bekmnsrw.feature.home.impl.data.HomeRepositoryImpl
import com.bekmnsrw.feature.home.impl.data.datasource.remote.HomeApi
import com.bekmnsrw.feature.home.impl.presentation.details.DetailsScreenModel
import com.bekmnsrw.feature.home.impl.presentation.home.HomeScreenModel
import com.bekmnsrw.feature.home.impl.presentation.list.MoreAnimeListScreenModel
import com.bekmnsrw.feature.home.impl.usecase.AddToFavoritesUseCaseImpl
import com.bekmnsrw.feature.home.impl.usecase.GetAnimeListUseCaseImpl
import com.bekmnsrw.feature.home.impl.usecase.GetAnimeUseCaseImpl
import com.bekmnsrw.feature.home.impl.usecase.GetSimilarAnimeListUseCaseImpl
import com.bekmnsrw.feature.home.impl.usecase.RemoveFromFavoritesUseCaseImpl
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit

val homeModule = module {
    factory<HomeApi> {
        provideHomeApi(
            retrofit = get(
                qualifier = named(Qualifiers.API_RETROFIT)
            )
        )
    }

    factory<HomeRepository> {
        provideHomeRepository(homeApi = get())
    }

    factory<GetAnimeListUseCase> {
        provideGetAnimeListUseCase(homeRepository = get())
    }

    factory<GetAnimeUseCase> {
        provideGetAnimeUseCase(homeRepository = get())
    }

    factory<AddToFavoritesUseCase> {
        provideAddToFavoritesUseCase(homeRepository = get())
    }

    factory<RemoveFromFavoritesUseCase> {
        provideRemoveFromFavoritesUseCase(homeRepository = get())
    }

    factory<GetSimilarAnimeListUseCase> {
        provideGetSimilarAnimeListUseCase(homeRepository = get())
    }

    factory<HomeScreenModel> {
        provideHomeScreenModel(
            getAnimeListUseCase = get()
        )
    }

    factory<MoreAnimeListScreenModel> {
        provideAnimeListScreenModel(
            homeRepository = get(),
            status = getProperty(STATUS_KOIN_PROPERTY)
        )
    }

    factory<DetailsScreenModel> {
        provideDetailsScreenModel(
            getAnimeUseCase = get(),
            animeId = getProperty(ANIME_ID_KOIN_PROPERTY),
            addToFavoritesUseCase = get(),
            removeFromFavoritesUseCase = get(),
            getSimilarAnimeListUseCase = get()
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
    animeId: Int,
    addToFavoritesUseCase: AddToFavoritesUseCase,
    removeFromFavoritesUseCase: RemoveFromFavoritesUseCase,
    getSimilarAnimeListUseCase: GetSimilarAnimeListUseCase
): DetailsScreenModel = DetailsScreenModel(
    getAnimeUseCase = getAnimeUseCase,
    animeId = animeId,
    addToFavoritesUseCase = addToFavoritesUseCase,
    removeFromFavoritesUseCase = removeFromFavoritesUseCase,
    getSimilarAnimeListUseCase = getSimilarAnimeListUseCase
)

private fun provideAddToFavoritesUseCase(
    homeRepository: HomeRepository
): AddToFavoritesUseCase = AddToFavoritesUseCaseImpl(
    homeRepository = homeRepository
)

private fun provideRemoveFromFavoritesUseCase(
    homeRepository: HomeRepository
): RemoveFromFavoritesUseCase = RemoveFromFavoritesUseCaseImpl(
    homeRepository = homeRepository
)

private fun provideGetSimilarAnimeListUseCase(
    homeRepository: HomeRepository
): GetSimilarAnimeListUseCase = GetSimilarAnimeListUseCaseImpl(
    homeRepository = homeRepository
)
