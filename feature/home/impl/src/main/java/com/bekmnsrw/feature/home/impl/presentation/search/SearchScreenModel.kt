package com.bekmnsrw.feature.home.impl.presentation.search

import androidx.compose.runtime.Immutable
import androidx.paging.PagingData
import androidx.paging.cachedIn
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.bekmnsrw.feature.home.api.usecase.SearchAnimeUseCase
import com.bekmnsrw.feature.home.impl.presentation.search.SearchScreenModel.SearchScreenAction.NavigateAnimeDetailsScreen
import com.bekmnsrw.feature.home.impl.presentation.search.SearchScreenModel.SearchScreenAction.NavigateBack
import com.bekmnsrw.feature.home.impl.presentation.search.SearchScreenModel.SearchScreenEvent.OnActiveChange
import com.bekmnsrw.feature.home.impl.presentation.search.SearchScreenModel.SearchScreenEvent.OnAnimeClick
import com.bekmnsrw.feature.home.impl.presentation.search.SearchScreenModel.SearchScreenEvent.OnArrowBackClick
import com.bekmnsrw.feature.home.impl.presentation.search.SearchScreenModel.SearchScreenEvent.OnClearQueryClick
import com.bekmnsrw.feature.home.impl.presentation.search.SearchScreenModel.SearchScreenEvent.OnQueryChange
import com.bekmnsrw.feature.home.impl.presentation.search.SearchScreenModel.SearchScreenEvent.OnSearchIconClick
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

internal class SearchScreenModel(
    private val searchAnimeUseCase: SearchAnimeUseCase
) : ScreenModel {

    @Immutable
    internal data class SearchScreenState(
        val shouldShowSearch: Boolean = false
    )

    @Immutable
    internal sealed interface SearchScreenEvent {
        data class OnQueryChange(val query: String) : SearchScreenEvent
        data object OnSearchIconClick : SearchScreenEvent
        data class OnActiveChange(val isActive: Boolean) : SearchScreenEvent
        data object OnClearQueryClick : SearchScreenEvent
        data object OnArrowBackClick : SearchScreenEvent
        data class OnAnimeClick(val id: Int) : SearchScreenEvent
    }

    @Immutable
    internal sealed interface SearchScreenAction {
        data class NavigateAnimeDetailsScreen(val id: Int) : SearchScreenAction
        data object NavigateBack : SearchScreenAction
    }

    private val _screenState = MutableStateFlow(SearchScreenState())
    val screenState: StateFlow<SearchScreenState> = _screenState.asStateFlow()

    private val _screenAction = MutableSharedFlow<SearchScreenAction?>()
    val screenAction: SharedFlow<SearchScreenAction?> = _screenAction.asSharedFlow()

    private val _searchInput = MutableStateFlow("")
    val searchInput: StateFlow<String> = _searchInput.asStateFlow()

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    val searchResult = _searchInput
        .debounce(500)
        .distinctUntilChanged()
        .flatMapLatest { query ->
            when {
                query.isEmpty() -> flowOf(PagingData.empty())
                else -> searchAnimeUseCase(search = query).flowOn(Dispatchers.IO)
            }
        }.cachedIn(screenModelScope)

    fun eventHandler(event: SearchScreenEvent) {
        when (event) {
            is OnActiveChange -> onActiveChange(event.isActive)
            OnClearQueryClick -> onClearQueryClick()
            is OnQueryChange -> onQueryChange(event.query)
            OnSearchIconClick -> onSearchIconClick()
            OnArrowBackClick -> onArrowBackClick()
            is OnAnimeClick -> onAnimeClick(event.id)
        }
    }

    private fun onSearchIconClick() = screenModelScope.launch {
        _screenState.emit(
            _screenState.value.copy(
                shouldShowSearch = true
            )
        )
    }

    private fun onQueryChange(query: String) = screenModelScope.launch {
        _searchInput.value = query
    }

    private fun onActiveChange(isActive: Boolean) = screenModelScope.launch {
        _screenState.emit(
            _screenState.value.copy(
                shouldShowSearch = isActive
            )
        )
    }

    private fun onClearQueryClick() = screenModelScope.launch {
        _searchInput.value = ""
    }

    private fun onArrowBackClick() = screenModelScope.launch {
        _screenAction.emit(NavigateBack)
    }

    private fun onAnimeClick(id: Int) = screenModelScope.launch {
        _screenAction.emit(NavigateAnimeDetailsScreen(id = id))
    }
}
