package com.bekmnsrw.feature.home.impl.di

import com.bekmnsrw.core.db.AppDatabase
import com.bekmnsrw.core.network.qualifier.Qualifiers
import com.bekmnsrw.feature.auth.api.usecase.local.GetUserIdUseCase
import com.bekmnsrw.feature.auth.api.usecase.local.IsAuthenticatedUseCase
import com.bekmnsrw.feature.favorites.api.usecase.UpdateAnimeStatusUseCase
import com.bekmnsrw.feature.home.api.repository.HomeRepository
import com.bekmnsrw.feature.home.api.usecase.AddToFavoritesUseCase
import com.bekmnsrw.feature.home.api.usecase.CreateUserRatesUseCase
import com.bekmnsrw.feature.home.api.usecase.DeleteAllSearchRequestsUseCase
import com.bekmnsrw.feature.home.api.usecase.DeleteSearchRequestByIdUseCase
import com.bekmnsrw.feature.home.api.usecase.DeleteUserRatesUseCase
import com.bekmnsrw.feature.home.api.usecase.GetAllSearchRequestsUseCase
import com.bekmnsrw.feature.home.api.usecase.GetAnimeListUseCase
import com.bekmnsrw.feature.home.api.usecase.GetAnimeUseCase
import com.bekmnsrw.feature.home.api.usecase.GetSimilarAnimeListUseCase
import com.bekmnsrw.feature.home.api.usecase.RemoveFromFavoritesUseCase
import com.bekmnsrw.feature.home.api.usecase.SaveSearchRequestUseCase
import com.bekmnsrw.feature.home.api.usecase.SearchAnimeUseCase
import com.bekmnsrw.feature.home.impl.HomeConstants.ANIME_ID_KOIN_PROPERTY
import com.bekmnsrw.feature.home.impl.HomeConstants.STATUS_KOIN_PROPERTY
import com.bekmnsrw.feature.home.impl.data.HomeRepositoryImpl
import com.bekmnsrw.feature.home.impl.data.datasource.remote.HomeApi
import com.bekmnsrw.feature.home.impl.presentation.details.DetailsScreenModel
import com.bekmnsrw.feature.home.impl.presentation.home.HomeScreenModel
import com.bekmnsrw.feature.home.impl.presentation.list.MoreAnimeListScreenModel
import com.bekmnsrw.feature.home.impl.presentation.search.SearchScreenModel
import com.bekmnsrw.feature.home.impl.usecase.AddToFavoritesUseCaseImpl
import com.bekmnsrw.feature.home.impl.usecase.CreateUserRatesUseCaseImpl
import com.bekmnsrw.feature.home.impl.usecase.DeleteAllSearchRequestsUseCaseImpl
import com.bekmnsrw.feature.home.impl.usecase.DeleteSearchRequestByIdUseCaseImpl
import com.bekmnsrw.feature.home.impl.usecase.DeleteUserRatesUseCaseImpl
import com.bekmnsrw.feature.home.impl.usecase.GetAllSearchRequestsUseCaseImpl
import com.bekmnsrw.feature.home.impl.usecase.GetAnimeListUseCaseImpl
import com.bekmnsrw.feature.home.impl.usecase.GetAnimeUseCaseImpl
import com.bekmnsrw.feature.home.impl.usecase.GetSimilarAnimeListUseCaseImpl
import com.bekmnsrw.feature.home.impl.usecase.RemoveFromFavoritesUseCaseImpl
import com.bekmnsrw.feature.home.impl.usecase.SaveSearchRequestUseCaseImpl
import com.bekmnsrw.feature.home.impl.usecase.SearchAnimeUseCaseImpl
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
        provideHomeRepository(
            homeApi = get(),
            appDatabase = get()
        )
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

    factory<CreateUserRatesUseCase> {
        provideCreateUserRatesUseCase(homeRepository = get())
    }

    factory<DeleteUserRatesUseCase> {
        provideDeleteUserRatesUseCase(homeRepository = get())
    }

    factory<SearchAnimeUseCase> {
        provideSearchAnimeUseCase(homeRepository = get())
    }

    factory<DeleteSearchRequestByIdUseCase> {
        provideDeleteSearchRequestByIdUseCase(homeRepository = get())
    }

    factory<DeleteAllSearchRequestsUseCase> {
        provideDeleteAllSearchRequestsUseCase(homeRepository = get())
    }

    factory<SaveSearchRequestUseCase> {
        provideSaveSearchRequestUseCase(homeRepository = get())
    }

    factory<GetAllSearchRequestsUseCase> {
        provideGetAllSearchRequestsUseCase(homeRepository = get())
    }

    factory<HomeScreenModel> {
        provideHomeScreenModel(
            getAnimeListUseCase = get(),
            searchAnimeUseCase = get()
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
            getSimilarAnimeListUseCase = get(),
            createUserRatesUseCase = get(),
            updateAnimeStatusUseCase = get(),
            deleteUserRatesUseCase = get(),
            getUserIdUseCase = get(),
            isAuthenticatedUseCase = get()
        )
    }

    factory<SearchScreenModel> {
        provideSearchScreenModel(
            searchAnimeUseCase = get(),
            status = getProperty(STATUS_KOIN_PROPERTY),
            getAllSearchRequestsUseCase = get(),
            saveSearchRequestUseCase = get(),
            deleteSearchRequestByIdUseCase = get(),
            deleteAllSearchRequestsUseCase = get()
        )
    }
}

private fun provideHomeApi(
    retrofit: Retrofit
): HomeApi = retrofit.create(HomeApi::class.java)

private fun provideHomeRepository(
    homeApi: HomeApi,
    appDatabase: AppDatabase
): HomeRepository = HomeRepositoryImpl(
    homeApi = homeApi,
    appDatabase = appDatabase
)

private fun provideHomeScreenModel(
    getAnimeListUseCase: GetAnimeListUseCase,
    searchAnimeUseCase: SearchAnimeUseCase
): HomeScreenModel = HomeScreenModel(
    getAnimeListUseCase = getAnimeListUseCase,
    searchAnimeUseCase = searchAnimeUseCase
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
    getSimilarAnimeListUseCase: GetSimilarAnimeListUseCase,
    createUserRatesUseCase: CreateUserRatesUseCase,
    updateAnimeStatusUseCase: UpdateAnimeStatusUseCase,
    deleteUserRatesUseCase: DeleteUserRatesUseCase,
    getUserIdUseCase: GetUserIdUseCase,
    isAuthenticatedUseCase: IsAuthenticatedUseCase
): DetailsScreenModel = DetailsScreenModel(
    getAnimeUseCase = getAnimeUseCase,
    animeId = animeId,
    addToFavoritesUseCase = addToFavoritesUseCase,
    removeFromFavoritesUseCase = removeFromFavoritesUseCase,
    getSimilarAnimeListUseCase = getSimilarAnimeListUseCase,
    createUserRatesUseCase = createUserRatesUseCase,
    updateAnimeStatusUseCase = updateAnimeStatusUseCase,
    deleteUserRatesUseCase = deleteUserRatesUseCase,
    getUserIdUseCase = getUserIdUseCase,
    isAuthenticatedUseCase = isAuthenticatedUseCase
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

private fun provideCreateUserRatesUseCase(
    homeRepository: HomeRepository
): CreateUserRatesUseCase = CreateUserRatesUseCaseImpl(
    homeRepository = homeRepository
)

private fun provideDeleteUserRatesUseCase(
    homeRepository: HomeRepository
): DeleteUserRatesUseCase = DeleteUserRatesUseCaseImpl(
    homeRepository = homeRepository
)

private fun provideSearchAnimeUseCase(
    homeRepository: HomeRepository
): SearchAnimeUseCase = SearchAnimeUseCaseImpl(
    homeRepository = homeRepository
)

private fun provideSearchScreenModel(
    searchAnimeUseCase: SearchAnimeUseCase,
    status: String,
    getAllSearchRequestsUseCase: GetAllSearchRequestsUseCase,
    saveSearchRequestUseCase: SaveSearchRequestUseCase,
    deleteAllSearchRequestsUseCase: DeleteAllSearchRequestsUseCase,
    deleteSearchRequestByIdUseCase: DeleteSearchRequestByIdUseCase
): SearchScreenModel = SearchScreenModel(
    searchAnimeUseCase = searchAnimeUseCase,
    status = status,
    getAllSearchRequestsUseCase = getAllSearchRequestsUseCase,
    saveSearchRequestUseCase = saveSearchRequestUseCase,
    deleteAllSearchRequestsUseCase = deleteAllSearchRequestsUseCase,
    deleteSearchRequestByIdUseCase = deleteSearchRequestByIdUseCase
)

private fun provideDeleteAllSearchRequestsUseCase(
    homeRepository: HomeRepository
): DeleteAllSearchRequestsUseCase = DeleteAllSearchRequestsUseCaseImpl(
    homeRepository = homeRepository
)

private fun provideDeleteSearchRequestByIdUseCase(
    homeRepository: HomeRepository
): DeleteSearchRequestByIdUseCase = DeleteSearchRequestByIdUseCaseImpl(
    homeRepository = homeRepository
)

private fun provideSaveSearchRequestUseCase(
    homeRepository: HomeRepository
): SaveSearchRequestUseCase = SaveSearchRequestUseCaseImpl(
    homeRepository = homeRepository
)

private fun provideGetAllSearchRequestsUseCase(
    homeRepository: HomeRepository
): GetAllSearchRequestsUseCase = GetAllSearchRequestsUseCaseImpl(
    homeRepository = homeRepository
)
