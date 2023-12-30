package com.bekmnsrw.feature.home.impl.presentation.search

import androidx.compose.runtime.Immutable
import androidx.paging.PagingData
import androidx.paging.cachedIn
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.bekmnsrw.feature.home.api.model.SearchRequest
import com.bekmnsrw.feature.home.api.usecase.DeleteAllSearchRequestsUseCase
import com.bekmnsrw.feature.home.api.usecase.DeleteSearchRequestByIdUseCase
import com.bekmnsrw.feature.home.api.usecase.GetAllSearchRequestsUseCase
import com.bekmnsrw.feature.home.api.usecase.SaveSearchRequestUseCase
import com.bekmnsrw.feature.home.api.usecase.SearchAnimeUseCase
import com.bekmnsrw.feature.home.impl.presentation.search.SearchScreenModel.SearchScreenAction.NavigateAnimeDetailsScreen
import com.bekmnsrw.feature.home.impl.presentation.search.SearchScreenModel.SearchScreenAction.NavigateBack
import com.bekmnsrw.feature.home.impl.presentation.search.SearchScreenModel.SearchScreenEvent.*
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
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
    private val searchAnimeUseCase: SearchAnimeUseCase,
    private val status: String,
    private val getAllSearchRequestsUseCase: GetAllSearchRequestsUseCase,
    private val saveSearchRequestUseCase: SaveSearchRequestUseCase,
    private val deleteSearchRequestByIdUseCase: DeleteSearchRequestByIdUseCase,
    private val deleteAllSearchRequestsUseCase: DeleteAllSearchRequestsUseCase
) : ScreenModel {

    @Immutable
    internal data class SearchScreenState(
        val shouldShowSearch: Boolean = false,
        val searchHistory: PersistentList<SearchRequest> = persistentListOf()
    )

    @Immutable
    internal sealed interface SearchScreenEvent {
        data class OnQueryChange(val query: String) : SearchScreenEvent
        data object OnSearchIconClick : SearchScreenEvent
        data class OnActiveChange(val isActive: Boolean) : SearchScreenEvent
        data object OnClearQueryClick : SearchScreenEvent
        data object OnArrowBackClick : SearchScreenEvent
        data class OnAnimeClick(val id: Int) : SearchScreenEvent
        data class OnDeleteSearchHistoryItemClick(val id: Int) : SearchScreenEvent
        data object OnImeActionSearchClick : SearchScreenEvent
        data object OnDeleteSearchHistoryClick : SearchScreenEvent
        data class OnSearchHistoryItemClick(val id: Int) : SearchScreenEvent
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
                else -> searchAnimeUseCase(
                    query = query,
                    status = status.ifEmpty { null }
                ).flowOn(Dispatchers.IO)
            }
        }.cachedIn(screenModelScope)

    init {
        getSearchHistory()
    }

    fun eventHandler(event: SearchScreenEvent) {
        when (event) {
            is OnActiveChange -> onActiveChange(event.isActive)
            OnClearQueryClick -> onClearQueryClick()
            is OnQueryChange -> onQueryChange(event.query)
            OnSearchIconClick -> onSearchIconClick()
            OnArrowBackClick -> onArrowBackClick()
            is OnAnimeClick -> onAnimeClick(event.id)
            OnDeleteSearchHistoryClick -> onDeleteSearchHistoryClick()
            is OnDeleteSearchHistoryItemClick -> onDeleteSearchHistoryItemClick(event.id)
            OnImeActionSearchClick -> onImeActionSearchClick()
            is OnSearchHistoryItemClick -> onSearchHistoryItemClick(event.id)
        }
    }

    private fun getSearchHistory() = screenModelScope.launch {
        getAllSearchRequestsUseCase()
            .flowOn(Dispatchers.IO)
            .collect {
                _screenState.emit(
                    _screenState.value.copy(
                        searchHistory = it.toPersistentList()
                    )
                )
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

    private fun onImeActionSearchClick() = screenModelScope.launch {
        saveSearchRequestUseCase(
            searchRequest = SearchRequest(
                id = 0,
                query = _searchInput.value
            )
        )
    }

    private fun onDeleteSearchHistoryClick() = screenModelScope.launch {
        deleteAllSearchRequestsUseCase()
    }

    private fun onDeleteSearchHistoryItemClick(id: Int) = screenModelScope.launch {
        deleteSearchRequestByIdUseCase(id = id)
    }

    private fun onSearchHistoryItemClick(id: Int) = screenModelScope.launch {
        val query = _screenState.value.searchHistory.first { it.id == id }.query
        _searchInput.value = query
    }
}
