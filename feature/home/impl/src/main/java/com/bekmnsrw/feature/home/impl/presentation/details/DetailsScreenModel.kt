package com.bekmnsrw.feature.home.impl.presentation.details

import androidx.compose.runtime.Immutable
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.bekmnsrw.feature.home.api.model.details.AnimeDetails
import com.bekmnsrw.feature.home.api.usecase.GetAnimeUseCase
import com.bekmnsrw.feature.home.impl.presentation.details.DetailsScreenModel.DetailsScreenAction.*
import com.bekmnsrw.feature.home.impl.presentation.details.DetailsScreenModel.DetailsScreenEvent.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

internal class DetailsScreenModel(
    private val getAnimeUseCase: GetAnimeUseCase,
    animeId: Int
) : ScreenModel {

    @Immutable
    internal data class DetailsScreenState(
        val isLoading: Boolean = false,
        val animeDetails: AnimeDetails? = null,
        val isFavoured: Boolean = false
    )

    @Immutable
    internal sealed interface DetailsScreenEvent {
        data object OnArrowBackClicked : DetailsScreenEvent
        data class OnFavouredClicked(val animeId: Int) : DetailsScreenEvent
    }

    @Immutable
    internal sealed interface DetailsScreenAction {
        data object NavigateBack : DetailsScreenAction
        data class ShowIsFavouredSnackbar(
            val isFavoured: Boolean,
            val name: String?
        ) : DetailsScreenAction
    }

    private val _screenState = MutableStateFlow(DetailsScreenState())
    val screenState: StateFlow<DetailsScreenState> = _screenState.asStateFlow()

    private val _screenAction = MutableSharedFlow<DetailsScreenAction>()
    val screenAction: SharedFlow<DetailsScreenAction?> = _screenAction.asSharedFlow()

    fun eventHandler(detailsScreenEvent: DetailsScreenEvent) {
        when (detailsScreenEvent) {
            OnArrowBackClicked -> onArrowBacClicked()
            is OnFavouredClicked -> onFavouredClicked()
        }
    }

    init { loadAnime(id = animeId) }

    private fun loadAnime(id: Int) = screenModelScope.launch {
        getAnimeUseCase(id = id)
            .flowOn(Dispatchers.IO)
            .onStart {
                _screenState.emit(
                    _screenState.value.copy(
                        isLoading = true
                    )
                )
            }
            .onCompletion {
                _screenState.emit(
                    _screenState.value.copy(
                        isLoading = false
                    )
                )
            }
            .collect {
                _screenState.emit(
                    _screenState.value.copy(
                        animeDetails = it,
                        isFavoured = it.favoured
                    )
                )
            }
    }

    private fun onArrowBacClicked() = screenModelScope.launch {
        _screenAction.emit(
            NavigateBack
        )
    }

    private fun onFavouredClicked() = screenModelScope.launch {
        val isFavoured = _screenState.value.isFavoured
        val name = _screenState.value.animeDetails?.name

        _screenState.emit(
            _screenState.value.copy(
                isFavoured = !isFavoured
            )
        )

        _screenAction.emit(
            ShowIsFavouredSnackbar(
                isFavoured = !isFavoured,
                name = name
            )
        )

        // TODO: API request
    }
}
