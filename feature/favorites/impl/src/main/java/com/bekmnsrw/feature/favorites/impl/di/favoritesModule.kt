package com.bekmnsrw.feature.favorites.impl.di

import com.bekmnsrw.core.network.qualifier.Qualifiers
import com.bekmnsrw.feature.auth.api.usecase.local.GetUserIdUseCase
import com.bekmnsrw.feature.auth.api.usecase.local.IsAuthenticatedUseCase
import com.bekmnsrw.feature.favorites.api.repository.FavoritesRepository
import com.bekmnsrw.feature.favorites.api.usecase.GetUserFavoritesUseCase
import com.bekmnsrw.feature.favorites.api.usecase.UpdateAnimeStatusUseCase
import com.bekmnsrw.feature.favorites.impl.data.FavoritesApi
import com.bekmnsrw.feature.favorites.impl.data.FavoritesRepositoryImpl
import com.bekmnsrw.feature.favorites.impl.presentation.completed.CompletedScreenModel
import com.bekmnsrw.feature.favorites.impl.presentation.container.FavoritesTabScreenModel
import com.bekmnsrw.feature.favorites.impl.presentation.dropped.DroppedScreenModel
import com.bekmnsrw.feature.favorites.impl.presentation.favorites.FavoritesScreenModel
import com.bekmnsrw.feature.favorites.impl.presentation.onhold.OnHoldScreenModel
import com.bekmnsrw.feature.favorites.impl.presentation.planned.PlannedScreenModel
import com.bekmnsrw.feature.favorites.impl.presentation.watching.WatchingScreenModel
import com.bekmnsrw.feature.favorites.impl.usecase.GetUserFavoritesUseCaseImpl
import com.bekmnsrw.feature.favorites.impl.usecase.UpdateAnimeStatusUseCaseImpl
import com.bekmnsrw.feature.home.api.usecase.DeleteUserRatesUseCase
import com.bekmnsrw.feature.home.api.usecase.RemoveFromFavoritesUseCase
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit

val favoritesModule = module {
    factory<FavoritesApi> {
        provideFavoritesApi(
            retrofit = get(
                qualifier = named(Qualifiers.API_RETROFIT)
            )
        )
    }

    factory<FavoritesRepository> {
        provideFavoritesRepository(favoritesApi = get())
    }

    factory<GetUserFavoritesUseCase> {
        provideGetUserFavoritesUseCase(favoritesRepository = get())
    }

    factory<UpdateAnimeStatusUseCase> {
        provideUpdateAnimeStatusUseCase(favoritesRepository = get())
    }

    factory<PlannedScreenModel> {
        providePlannedScreenModel(
            favoritesRepository = get(),
            getUserIdUseCase = get(),
            updateAnimeStatusUseCase = get(),
            deleteUserRatesUseCase = get()
        )
    }

    factory<WatchingScreenModel> {
        provideWatchingScreenModel(
            favoritesRepository = get(),
            getUserIdUseCase = get(),
            updateAnimeStatusUseCase = get(),
            deleteUserRatesUseCase = get()
        )
    }

    factory<DroppedScreenModel> {
        provideDroppedScreenModel(
            favoritesRepository = get(),
            getUserIdUseCase = get(),
            updateAnimeStatusUseCase = get(),
            deleteUserRatesUseCase = get()
        )
    }

    factory<OnHoldScreenModel> {
        provideOnHoldScreenModel(
            favoritesRepository = get(),
            getUserIdUseCase = get(),
            updateAnimeStatusUseCase = get(),
            deleteUserRatesUseCase = get()
        )
    }

    factory<CompletedScreenModel> {
        provideCompletedScreenModel(
            favoritesRepository = get(),
            getUserIdUseCase = get(),
            updateAnimeStatusUseCase = get(),
            deleteUserRatesUseCase = get()
        )
    }

    factory<FavoritesScreenModel> {
        provideFavoritesScreenModel(
            getUserFavoritesUseCase = get(),
            removeFromFavoritesUseCase = get(),
            getUserIdUseCase = get()
        )
    }

    factory<FavoritesTabScreenModel> {
        provideFavoritesTabScreenModel(isAuthenticatedUseCase = get())
    }
}

private fun provideFavoritesTabScreenModel(
    isAuthenticatedUseCase: IsAuthenticatedUseCase
): FavoritesTabScreenModel = FavoritesTabScreenModel(
    isAuthenticatedUseCase = isAuthenticatedUseCase
)

private fun provideFavoritesApi(
    retrofit: Retrofit
): FavoritesApi = retrofit.create(FavoritesApi::class.java)

private fun provideFavoritesRepository(
    favoritesApi: FavoritesApi
): FavoritesRepository = FavoritesRepositoryImpl(
    favoritesApi = favoritesApi
)

private fun provideGetUserFavoritesUseCase(
    favoritesRepository: FavoritesRepository
): GetUserFavoritesUseCase = GetUserFavoritesUseCaseImpl(
    favoritesRepository = favoritesRepository,
)

private fun providePlannedScreenModel(
    favoritesRepository: FavoritesRepository,
    getUserIdUseCase: GetUserIdUseCase,
    updateAnimeStatusUseCase: UpdateAnimeStatusUseCase,
    deleteUserRatesUseCase: DeleteUserRatesUseCase
): PlannedScreenModel = PlannedScreenModel(
    favoritesRepository = favoritesRepository,
    getUserIdUseCase = getUserIdUseCase,
    updateAnimeStatusUseCase = updateAnimeStatusUseCase,
    deleteUserRatesUseCase = deleteUserRatesUseCase
)

private fun provideWatchingScreenModel(
    favoritesRepository: FavoritesRepository,
    getUserIdUseCase: GetUserIdUseCase,
    updateAnimeStatusUseCase: UpdateAnimeStatusUseCase,
    deleteUserRatesUseCase: DeleteUserRatesUseCase
) : WatchingScreenModel = WatchingScreenModel(
    favoritesRepository = favoritesRepository,
    getUserIdUseCase = getUserIdUseCase,
    updateAnimeStatusUseCase = updateAnimeStatusUseCase,
    deleteUserRatesUseCase = deleteUserRatesUseCase
)

private fun provideDroppedScreenModel(
    favoritesRepository: FavoritesRepository,
    getUserIdUseCase: GetUserIdUseCase,
    updateAnimeStatusUseCase: UpdateAnimeStatusUseCase,
    deleteUserRatesUseCase: DeleteUserRatesUseCase
) : DroppedScreenModel = DroppedScreenModel(
    favoritesRepository = favoritesRepository,
    getUserIdUseCase = getUserIdUseCase,
    updateAnimeStatusUseCase = updateAnimeStatusUseCase,
    deleteUserRatesUseCase = deleteUserRatesUseCase
)

private fun provideCompletedScreenModel(
    favoritesRepository: FavoritesRepository,
    getUserIdUseCase: GetUserIdUseCase,
    updateAnimeStatusUseCase: UpdateAnimeStatusUseCase,
    deleteUserRatesUseCase: DeleteUserRatesUseCase
) : CompletedScreenModel = CompletedScreenModel(
    favoritesRepository = favoritesRepository,
    getUserIdUseCase = getUserIdUseCase,
    updateAnimeStatusUseCase = updateAnimeStatusUseCase,
    deleteUserRatesUseCase = deleteUserRatesUseCase
)

private fun provideOnHoldScreenModel(
    favoritesRepository: FavoritesRepository,
    getUserIdUseCase: GetUserIdUseCase,
    updateAnimeStatusUseCase: UpdateAnimeStatusUseCase,
    deleteUserRatesUseCase: DeleteUserRatesUseCase
) : OnHoldScreenModel = OnHoldScreenModel(
    favoritesRepository = favoritesRepository,
    getUserIdUseCase = getUserIdUseCase,
    updateAnimeStatusUseCase = updateAnimeStatusUseCase,
    deleteUserRatesUseCase = deleteUserRatesUseCase
)

private fun provideFavoritesScreenModel(
    getUserFavoritesUseCase: GetUserFavoritesUseCase,
    removeFromFavoritesUseCase: RemoveFromFavoritesUseCase,
    getUserIdUseCase: GetUserIdUseCase
) : FavoritesScreenModel = FavoritesScreenModel(
    getUserFavoritesUseCase = getUserFavoritesUseCase,
    removeFromFavoritesUseCase = removeFromFavoritesUseCase,
    getUserIdUseCase = getUserIdUseCase
)

private fun provideUpdateAnimeStatusUseCase(
    favoritesRepository: FavoritesRepository
): UpdateAnimeStatusUseCase = UpdateAnimeStatusUseCaseImpl(
    favoritesRepository = favoritesRepository
)
