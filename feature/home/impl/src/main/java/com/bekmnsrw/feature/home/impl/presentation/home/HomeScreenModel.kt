package com.bekmnsrw.feature.home.impl.presentation.home

import androidx.compose.runtime.Immutable
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.bekmnsrw.feature.home.api.model.list.Anime
import com.bekmnsrw.feature.home.api.usecase.GetAnimeListUseCase
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

internal class HomeScreenModel(
    private val getAnimeListUseCase: GetAnimeListUseCase
) : ScreenModel {

    internal companion object {
        private const val LIMIT = 10

        private const val STATUS_ONGOING = "ongoing"
        private const val STATUS_ANONS = "anons"
        private const val STATUS_RELEASED = "released"

        private const val ORDERED_BY_RANK = "ranked"
        private const val ORDERED_BY_RANDOM = "random"
    }

    @Immutable
    internal data class HomeScreenState(
        val isLoading: Boolean = false,
        val ongoingAnimeList: PersistentList<Anime> = persistentListOf(),
        val anonsAnimeList: PersistentList<Anime> = persistentListOf(),
        val releasedAnimeList: PersistentList<Anime> = persistentListOf()
    )

    @Immutable
    internal sealed interface HomeScreenEvent {
        data class OnMoreClicked(val status: String) : HomeScreenEvent
        data class OnAnimeCardClicked(val id: Int) : HomeScreenEvent
    }

    @Immutable
    internal sealed interface HomeScreenAction {
        data class NavigateMoreAnimeList(val status: String) : HomeScreenAction
        data class NavigateAnimeDetailsScreen(val id: Int) : HomeScreenAction
    }

    private val _screenState = MutableStateFlow(HomeScreenState())
    val screenState: StateFlow<HomeScreenState> = _screenState.asStateFlow()

    private val _screenAction = MutableSharedFlow<HomeScreenAction?>()
    val screenAction: SharedFlow<HomeScreenAction?> = _screenAction.asSharedFlow()

    fun eventHandler(homeScreenEvent: HomeScreenEvent) {
        when (homeScreenEvent) {
            is HomeScreenEvent.OnMoreClicked -> onMoreClicked(homeScreenEvent.status)

            is HomeScreenEvent.OnAnimeCardClicked -> onAnimeCardClicked(homeScreenEvent.id)
        }
    }

    init { loadAllAnime() }

    private fun loadOngoingAnime() = screenModelScope.async {
        getAnimeListUseCase(
            limit = LIMIT,
            status = STATUS_ONGOING,
            order = ORDERED_BY_RANK
        )
    }

    private fun loadAnonsAnime() = screenModelScope.async {
        getAnimeListUseCase(
            limit = LIMIT,
            status = STATUS_ANONS,
            order = ORDERED_BY_RANDOM
        )
    }

    private fun loadReleasedAnime() = screenModelScope.async {
        getAnimeListUseCase(
            limit = LIMIT,
            status = STATUS_RELEASED,
            order = ORDERED_BY_RANK
        )
    }

    private fun loadAllAnime() = screenModelScope.launch {
        _screenState.emit(
            _screenState.value.copy(
                isLoading = true
            )
        )

        val result = awaitAll(
            loadOngoingAnime(),
            loadAnonsAnime(),
            loadReleasedAnime()
        )

        _screenState.emit(
            _screenState.value.copy(
                ongoingAnimeList = result[0].toPersistentList(),
                anonsAnimeList = result[1].toPersistentList(),
                releasedAnimeList = result[2].toPersistentList()
            )
        )

        _screenState.emit(
            _screenState.value.copy(
                isLoading = false
            )
        )
    }

    private fun onMoreClicked(status: String) = screenModelScope.launch {
        _screenAction.emit(
            HomeScreenAction.NavigateMoreAnimeList(
                status = status
            )
        )
    }

    private fun onAnimeCardClicked(id: Int) = screenModelScope.launch {
        _screenAction.emit(
            HomeScreenAction.NavigateAnimeDetailsScreen(
                id = id
            )
        )
    }
}
