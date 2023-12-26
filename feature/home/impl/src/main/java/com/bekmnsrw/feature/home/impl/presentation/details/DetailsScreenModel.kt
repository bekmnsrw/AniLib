package com.bekmnsrw.feature.home.impl.presentation.details

import androidx.compose.runtime.Immutable
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.bekmnsrw.feature.home.api.model.Anime
import com.bekmnsrw.feature.home.api.model.AnimeDetails
import com.bekmnsrw.feature.home.api.usecase.AddToFavoritesUseCase
import com.bekmnsrw.feature.home.api.usecase.GetAnimeUseCase
import com.bekmnsrw.feature.home.api.usecase.GetSimilarAnimeListUseCase
import com.bekmnsrw.feature.home.api.usecase.RemoveFromFavoritesUseCase
import com.bekmnsrw.feature.home.impl.HomeConstants.REQUEST_LIMIT
import com.bekmnsrw.feature.home.impl.presentation.details.DetailsScreenModel.DetailsScreenAction.*
import com.bekmnsrw.feature.home.impl.presentation.details.DetailsScreenModel.DetailsScreenEvent.OnArrowBackClicked
import com.bekmnsrw.feature.home.impl.presentation.details.DetailsScreenModel.DetailsScreenEvent.OnDescriptionClicked
import com.bekmnsrw.feature.home.impl.presentation.details.DetailsScreenModel.DetailsScreenEvent.OnFavouredClicked
import com.bekmnsrw.feature.home.impl.presentation.details.DetailsScreenModel.DetailsScreenEvent.OnInfoIconClicked
import com.bekmnsrw.feature.home.impl.presentation.details.DetailsScreenModel.DetailsScreenEvent.OnModalBottomSheetDismiss
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
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
    private val animeId: Int,
    private val getAnimeUseCase: GetAnimeUseCase,
    private val addToFavoritesUseCase: AddToFavoritesUseCase,
    private val removeFromFavoritesUseCase: RemoveFromFavoritesUseCase,
    private val getSimilarAnimeListUseCase: GetSimilarAnimeListUseCase
) : ScreenModel {

    private companion object {
        const val ERROR_MESSAGE = "Oops, something went wrong. Please try again!"
        const val TYPE = "Anime"
    }

    @Immutable
    internal data class DetailsScreenState(
        val isLoading: Boolean = false,
        val animeDetails: AnimeDetails? = null,
        val isFavoured: Boolean = false,
        val shouldShowModalBottomSheet: Boolean = false,
        val isDescriptionExpanded: Boolean = false,
        val similarAnimeList: PersistentList<Anime> = persistentListOf()
    )

    @Immutable
    internal sealed interface DetailsScreenEvent {
        data object OnArrowBackClicked : DetailsScreenEvent
        data class OnFavouredClicked(val animeId: Int) : DetailsScreenEvent
        data object OnInfoIconClicked : DetailsScreenEvent
        data object OnModalBottomSheetDismiss : DetailsScreenEvent
        data object OnDescriptionClicked : DetailsScreenEvent
        data class OnSimilarAnimeCardClicked(val id: Int) : DetailsScreenEvent
    }

    @Immutable
    internal sealed interface DetailsScreenAction {
        data object NavigateBack : DetailsScreenAction
        data class ShowIsFavouredSnackbar(
            val isFavoured: Boolean,
            val name: String?
        ) : DetailsScreenAction
        data class ShowErrorSnackBar(val message: String) : DetailsScreenAction
        data class NavigateDetailsScreen(val id: Int) : DetailsScreenAction
    }

    private val _screenState = MutableStateFlow(DetailsScreenState())
    val screenState: StateFlow<DetailsScreenState> = _screenState.asStateFlow()

    private val _screenAction = MutableSharedFlow<DetailsScreenAction>()
    val screenAction: SharedFlow<DetailsScreenAction?> = _screenAction.asSharedFlow()

    fun eventHandler(detailsScreenEvent: DetailsScreenEvent) {
        when (detailsScreenEvent) {
            OnArrowBackClicked -> onArrowBacClicked()

            is OnFavouredClicked -> onFavouredClicked()

            OnInfoIconClicked -> onInfoButtonClicked()

            OnModalBottomSheetDismiss -> onModalBottomSheetDismiss()

            OnDescriptionClicked -> onDescriptionClicked()

            is DetailsScreenEvent.OnSimilarAnimeCardClicked -> onSimilarAnimeCardClicked(
                id = detailsScreenEvent.id
            )
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

        getSimilarAnimeListUseCase(
            id = id,
            limit = REQUEST_LIMIT
        )
            .flowOn(Dispatchers.IO)
            .collect {
                _screenState.emit(
                    _screenState.value.copy(
                        similarAnimeList = it.toPersistentList()
                    )
                )
            }
    }

    private fun onArrowBacClicked() = screenModelScope.launch {
        _screenAction.emit(NavigateBack)
    }

    private fun onFavouredClicked() = screenModelScope.launch {
        val isFavoured = _screenState.value.isFavoured
        val name = _screenState.value.animeDetails?.name

        when (isFavoured) {
            true -> removeFromFavoritesUseCase(type = TYPE, id = animeId)
                .flowOn(Dispatchers.IO)
                .collect { response ->
                    when (response.success) {
                        true -> {
                            _screenState.emit(
                                _screenState.value.copy(
                                    isFavoured = false
                                )
                            )
                            _screenAction.emit(
                                ShowIsFavouredSnackbar(
                                    isFavoured = false,
                                    name = name
                                )
                            )
                        }

                        false -> _screenAction.emit(ShowErrorSnackBar(message = ERROR_MESSAGE))
                    }
                }

            false -> addToFavoritesUseCase(type = TYPE, id = animeId)
                .flowOn(Dispatchers.IO)
                .collect { response ->
                    when (response.success) {
                        true -> {
                            _screenState.emit(
                                _screenState.value.copy(
                                    isFavoured = true
                                )
                            )
                            _screenAction.emit(
                                ShowIsFavouredSnackbar(
                                    isFavoured = true,
                                    name = name
                                )
                            )
                        }

                        false -> _screenAction.emit(ShowErrorSnackBar(message = ERROR_MESSAGE))
                    }
                }
        }
    }

    private fun onInfoButtonClicked() = screenModelScope.launch {
        _screenState.emit(
            _screenState.value.copy(
                shouldShowModalBottomSheet = true
            )
        )
    }

    private fun onModalBottomSheetDismiss() = screenModelScope.launch {
        _screenState.emit(
            _screenState.value.copy(
                shouldShowModalBottomSheet = false
            )
        )
    }

    private fun onDescriptionClicked() = screenModelScope.launch {
        val isDescriptionExpanded = _screenState.value.isDescriptionExpanded

        _screenState.emit(
            _screenState.value.copy(
                isDescriptionExpanded = !isDescriptionExpanded
            )
        )
    }

    private fun onSimilarAnimeCardClicked(id: Int) = screenModelScope.launch {
//        _screenAction.emit(
//            NavigateDetailsScreen(
//                id = id
//            )
//        )
    }
}
