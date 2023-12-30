package com.bekmnsrw.feature.home.impl.presentation.list

import androidx.compose.runtime.Immutable
import androidx.paging.cachedIn
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.bekmnsrw.feature.home.api.repository.HomeRepository
import com.bekmnsrw.feature.home.impl.AnimeFilterEnum.BY_NAME
import com.bekmnsrw.feature.home.impl.AnimeFilterEnum.BY_POPULARITY
import com.bekmnsrw.feature.home.impl.AnimeFilterEnum.BY_RANDOM
import com.bekmnsrw.feature.home.impl.AnimeFilterEnum.BY_RANK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

internal class MoreAnimeListScreenModel(
    private val homeRepository: HomeRepository,
    private val status: String
) : ScreenModel {

    internal companion object {
        val dropDownMenuItems: Map<String, String> = mutableMapOf(
            BY_RANK.key to BY_RANK.value,
            BY_POPULARITY.key to BY_POPULARITY.value,
            BY_NAME.key to BY_NAME.value,
            BY_RANDOM.key to BY_RANDOM.value
        )
    }

    @Immutable
    internal data class MoreAnimeListScreenState(
        val isDropDownMenuExpanded: Boolean = false,
        val filteredBy: String = BY_RANK.value
    )

    @Immutable
    internal sealed interface MoreAnimeListScreenEvent {
        data object OnArrowBackClicked : MoreAnimeListScreenEvent
        data class OnAnimeCardClicked(val id: Int) : MoreAnimeListScreenEvent
        data object OnFilterClicked : MoreAnimeListScreenEvent
        data object OnDropDownMenuDismissRequest : MoreAnimeListScreenEvent
        data class OnDropDownMenuItemClicked(val order: String) : MoreAnimeListScreenEvent
    }

    @Immutable
    internal sealed interface MoreAnimeListScreenAction {
        data object NavigateHomeScreen : MoreAnimeListScreenAction
        data class NavigateDetailsScreen(val id: Int) : MoreAnimeListScreenAction
    }

    private val animeParams = MutableStateFlow(Pair(status, BY_RANK.value))

    @OptIn(ExperimentalCoroutinesApi::class)
    val animePaged = animeParams.flatMapLatest { (status, filter) ->
        homeRepository
            .getAnimePaged(status = status, order = filter)
            .flowOn(Dispatchers.IO)
    }.cachedIn(screenModelScope)

    private val _screenState = MutableStateFlow(MoreAnimeListScreenState())
    val screenState: StateFlow<MoreAnimeListScreenState> = _screenState.asStateFlow()

    private val _screenAction = MutableSharedFlow<MoreAnimeListScreenAction?>()
    val screenAction: SharedFlow<MoreAnimeListScreenAction?> = _screenAction.asSharedFlow()

    fun eventHandler(moreAnimeListScreenEvent: MoreAnimeListScreenEvent) {
        when (moreAnimeListScreenEvent) {
            MoreAnimeListScreenEvent.OnArrowBackClicked -> onArrowBackClicked()

            is MoreAnimeListScreenEvent.OnAnimeCardClicked -> onAnimeCardClicked(
                moreAnimeListScreenEvent.id
            )

            MoreAnimeListScreenEvent.OnFilterClicked -> onFilterClicked()

            MoreAnimeListScreenEvent.OnDropDownMenuDismissRequest -> onDropDownMenuDismissRequest()

            is MoreAnimeListScreenEvent.OnDropDownMenuItemClicked -> onDropDownMenuItemClicked(
                order = moreAnimeListScreenEvent.order
            )
        }
    }

    private fun onArrowBackClicked() = screenModelScope.launch {
        _screenAction.emit(
            MoreAnimeListScreenAction.NavigateHomeScreen
        )
    }

    private fun onAnimeCardClicked(id: Int) = screenModelScope.launch {
        _screenAction.emit(
            MoreAnimeListScreenAction.NavigateDetailsScreen(
                id = id
            )
        )
    }

    private fun onFilterClicked() = screenModelScope.launch {
        _screenState.emit(
            _screenState.value.copy(
                isDropDownMenuExpanded = true
            )
        )
    }

    private fun onDropDownMenuDismissRequest() = screenModelScope.launch {
        _screenState.emit(
            _screenState.value.copy(
                isDropDownMenuExpanded = false
            )
        )
    }

    private fun onDropDownMenuItemClicked(order: String) = screenModelScope.launch {
        _screenState.emit(
            _screenState.value.copy(
                isDropDownMenuExpanded = false,
                filteredBy = order
            )
        )

        animeParams.emit(
            animeParams.value.copy(
                second = order
            )
        )
    }
}
