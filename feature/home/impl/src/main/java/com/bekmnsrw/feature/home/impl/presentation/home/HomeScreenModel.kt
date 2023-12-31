package com.bekmnsrw.feature.home.impl.presentation.home

import androidx.compose.runtime.Immutable
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.bekmnsrw.feature.home.api.model.Anime
import com.bekmnsrw.feature.home.api.usecase.GetAnimeListUseCase
import com.bekmnsrw.feature.home.api.usecase.SearchAnimeUseCase
import com.bekmnsrw.feature.home.impl.AnimeStatusEnum.ANONS
import com.bekmnsrw.feature.home.impl.AnimeStatusEnum.ONGOING
import com.bekmnsrw.feature.home.impl.AnimeStatusEnum.RELEASED
import com.bekmnsrw.feature.home.impl.HomeConstants.REQUEST_LIMIT
import com.bekmnsrw.feature.home.impl.presentation.home.HomeScreenModel.HomeScreenAction.NavigateAnimeDetailsScreen
import com.bekmnsrw.feature.home.impl.presentation.home.HomeScreenModel.HomeScreenAction.NavigateMoreAnimeList
import com.bekmnsrw.feature.home.impl.presentation.home.HomeScreenModel.HomeScreenAction.NavigateSearchScreen
import com.bekmnsrw.feature.home.impl.presentation.home.HomeScreenModel.HomeScreenEvent.OnAnimeCardClick
import com.bekmnsrw.feature.home.impl.presentation.home.HomeScreenModel.HomeScreenEvent.OnMoreClick
import com.bekmnsrw.feature.home.impl.presentation.home.HomeScreenModel.HomeScreenEvent.OnSearchIconClick
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
    private val getAnimeListUseCase: GetAnimeListUseCase,
    private val searchAnimeUseCase: SearchAnimeUseCase
) : ScreenModel {

    private companion object {
        const val ORDERED_BY_RANK = "ranked"
        const val ORDERED_BY_RANDOM = "random"
    }

    @Immutable
    internal data class HomeScreenState(
        val isLoading: Boolean = false,
        val ongoingAnimeListBrief: PersistentList<Anime> = persistentListOf(),
        val anonsAnimeListBrief: PersistentList<Anime> = persistentListOf(),
        val releasedAnimeListBrief: PersistentList<Anime> = persistentListOf(),
    )

    @Immutable
    internal sealed interface HomeScreenEvent {
        data class OnMoreClick(val status: String) : HomeScreenEvent
        data class OnAnimeCardClick(val id: Int) : HomeScreenEvent
        data object OnSearchIconClick : HomeScreenEvent
    }

    @Immutable
    internal sealed interface HomeScreenAction {
        data class NavigateMoreAnimeList(val status: String) : HomeScreenAction
        data class NavigateAnimeDetailsScreen(val id: Int) : HomeScreenAction
        data object NavigateSearchScreen : HomeScreenAction
    }

    private val _screenState = MutableStateFlow(HomeScreenState())
    val screenState: StateFlow<HomeScreenState> = _screenState.asStateFlow()

    private val _screenAction = MutableSharedFlow<HomeScreenAction?>()
    val screenAction: SharedFlow<HomeScreenAction?> = _screenAction.asSharedFlow()

    fun eventHandler(event: HomeScreenEvent) {
        when (event) {
            is OnMoreClick -> onMoreClicked(event.status)
            is OnAnimeCardClick -> onAnimeCardClicked(event.id)
            OnSearchIconClick -> onSearchIconClick()
        }
    }

    init {
        loadAllAnime()
    }

    private fun loadOngoingAnime() = screenModelScope.async {
        getAnimeListUseCase(
            limit = REQUEST_LIMIT,
            status = ONGOING.status,
            order = ORDERED_BY_RANK
        )
    }

    private fun loadAnonsAnime() = screenModelScope.async {
        getAnimeListUseCase(
            limit = REQUEST_LIMIT,
            status = ANONS.status,
            order = ORDERED_BY_RANDOM
        )
    }

    private fun loadReleasedAnime() = screenModelScope.async {
        getAnimeListUseCase(
            limit = REQUEST_LIMIT,
            status = RELEASED.status,
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
                ongoingAnimeListBrief = result[0].toPersistentList(),
                anonsAnimeListBrief = result[1].toPersistentList(),
                releasedAnimeListBrief = result[2].toPersistentList()
            )
        )

        _screenState.emit(
            _screenState.value.copy(
                isLoading = false
            )
        )
    }

    private fun onMoreClicked(status: String) = screenModelScope.launch {
        _screenAction.emit(NavigateMoreAnimeList(status = status))
    }

    private fun onAnimeCardClicked(id: Int) = screenModelScope.launch {
        _screenAction.emit(NavigateAnimeDetailsScreen(id = id))
    }

    private fun onSearchIconClick() = screenModelScope.launch {
        _screenAction.emit(NavigateSearchScreen)
    }
}
