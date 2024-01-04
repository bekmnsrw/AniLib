package com.bekmnsrw.feature.home.impl.presentation.details

import android.util.Log
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.bekmnsrw.core.utils.formatStatusString
import com.bekmnsrw.core.widget.UserRatesEnum
import com.bekmnsrw.feature.auth.api.usecase.local.GetUserIdUseCase
import com.bekmnsrw.feature.auth.api.usecase.local.IsAuthenticatedUseCase
import com.bekmnsrw.feature.favorites.api.usecase.UpdateAnimeStatusUseCase
import com.bekmnsrw.feature.home.api.model.Anime
import com.bekmnsrw.feature.home.api.model.AnimeDetails
import com.bekmnsrw.feature.home.api.usecase.AddToFavoritesUseCase
import com.bekmnsrw.feature.home.api.usecase.CreateUserRatesUseCase
import com.bekmnsrw.feature.home.api.usecase.DeleteUserRatesUseCase
import com.bekmnsrw.feature.home.api.usecase.GetAnimeUseCase
import com.bekmnsrw.feature.home.api.usecase.GetSimilarAnimeListUseCase
import com.bekmnsrw.feature.home.api.usecase.RemoveFromFavoritesUseCase
import com.bekmnsrw.feature.home.impl.HomeConstants.REQUEST_LIMIT
import com.bekmnsrw.feature.home.impl.presentation.details.DetailsScreenModel.DetailsScreenAction.*
import com.bekmnsrw.feature.home.impl.presentation.details.DetailsScreenModel.DetailsScreenEvent.*
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
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
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.net.ssl.HttpsURLConnection

internal class DetailsScreenModel(
    private val animeId: Int,
    private val getAnimeUseCase: GetAnimeUseCase,
    private val addToFavoritesUseCase: AddToFavoritesUseCase,
    private val removeFromFavoritesUseCase: RemoveFromFavoritesUseCase,
    private val getSimilarAnimeListUseCase: GetSimilarAnimeListUseCase,
    private val createUserRatesUseCase: CreateUserRatesUseCase,
    private val updateAnimeStatusUseCase: UpdateAnimeStatusUseCase,
    private val deleteUserRatesUseCase: DeleteUserRatesUseCase,
    private val getUserIdUseCase: GetUserIdUseCase,
    private val isAuthenticatedUseCase: IsAuthenticatedUseCase
) : ScreenModel {

    private companion object {
        const val WAS_ADDED_TO_FAVORITES = "was added to favorites"
        const val WAS_REMOVED_FROM_FAVORITES = "was removed from favorites"
        const val ERROR_MESSAGE = "Oops, something went wrong. Please try again!"
        const val WAS_ADDED_TO_CATEGORY = "Successfully added to"
        const val TYPE = "Anime"
        const val WAS_REMOVED_FROM_MY_LIST = "Removed from your list"
    }

    private val userId by lazy { mutableIntStateOf(0) }
    val isAuthenticated by lazy { mutableStateOf<Boolean?>(null) }

    @Immutable
    internal data class DetailsScreenState(
        val isLoading: Boolean = false,
        val animeDetails: AnimeDetails? = null,
        val isFavoured: Boolean = false,
        val shouldShowBottomSheet: Boolean = false,
        val isDescriptionExpanded: Boolean = false,
        val similarAnimeList: PersistentList<Anime> = persistentListOf(),
        val shouldShowStatusDialog: Boolean = false,
        val shouldShowAuthDialog: Boolean = false
    )

    @Immutable
    internal sealed interface DetailsScreenEvent {
        data object OnInit : DetailsScreenEvent
        data object OnStart : DetailsScreenEvent
        data object OnArrowBackClick : DetailsScreenEvent
        data class OnFavouredClick(val animeId: Int) : DetailsScreenEvent
        data object OnInfoIconClick : DetailsScreenEvent
        data object OnModalBottomSheetDismiss : DetailsScreenEvent
        data object OnDescriptionClick : DetailsScreenEvent
        data class OnSimilarAnimeCardClick(val id: Int) : DetailsScreenEvent
        data object OnAnimeStatusClick : DetailsScreenEvent
        data object OnStatusDialogDismissRequest : DetailsScreenEvent
        data object OnAuthDialogDismissRequest : DetailsScreenEvent
        data class OnRadioButtonClick(val status: String, val id: Int?) : DetailsScreenEvent
        data object OnAuthDialogConfirmButtonClick : DetailsScreenEvent
    }

    @Immutable
    internal sealed interface DetailsScreenAction {
        data object NavigateBack : DetailsScreenAction
        data object NavigateAuthScreen : DetailsScreenAction
        data class ShowSnackbar(val message: String) : DetailsScreenAction
        data class NavigateDetailsScreen(val id: Int) : DetailsScreenAction
    }

    private val _screenState = MutableStateFlow(DetailsScreenState())
    val screenState: StateFlow<DetailsScreenState> = _screenState.asStateFlow()

    private val _screenAction = MutableSharedFlow<DetailsScreenAction>()
    val screenAction: SharedFlow<DetailsScreenAction?> = _screenAction.asSharedFlow()

    fun eventHandler(event: DetailsScreenEvent) {
        when (event) {
            OnInit -> onInit()
            OnStart -> checkIfAnimeIsNotFavoured()
            OnArrowBackClick -> onArrowBacClick()
            is OnFavouredClick -> onFavouredClick()
            OnInfoIconClick -> onInfoButtonClick()
            OnModalBottomSheetDismiss -> onBottomSheetDismiss()
            OnDescriptionClick -> onDescriptionClick()
            is OnSimilarAnimeCardClick -> onSimilarAnimeCardClick(id = event.id)
            is OnAnimeStatusClick -> onAnimeStatusClick()
            OnStatusDialogDismissRequest -> onDialogDismissRequest()
            is OnRadioButtonClick -> onRadioButtonClick(event.status, event.id)
            OnAuthDialogDismissRequest -> onAuthDialogDismissRequest()
            OnAuthDialogConfirmButtonClick -> onAuthDialogConfirmButtonClick()
        }
    }

    init {
        getUserId()
        isAuthenticated()
        eventHandler(OnInit)
    }

    private fun onAuthDialogConfirmButtonClick() = screenModelScope.launch {
        _screenAction.emit(NavigateAuthScreen)
        _screenState.emit(
            _screenState.value.copy(
                shouldShowAuthDialog = false
            )
        )
    }

    private fun onAuthDialogDismissRequest() = screenModelScope.launch {
        _screenState.emit(
            _screenState.value.copy(
                shouldShowAuthDialog = false
            )
        )
    }

    private fun getUserId() = screenModelScope.launch {
        getUserIdUseCase()
            .flowOn(Dispatchers.IO)
            .collect {
                userId.intValue = it ?: 0
                Log.e("DetailsSM", "userId: $it")
            }
    }

    private fun isAuthenticated() = screenModelScope.launch {
        isAuthenticatedUseCase()
            .flowOn(Dispatchers.IO)
            .collect {
                isAuthenticated.value = it
                Log.e("DetailsSM", "isAuthenticated: $it")
            }
    }

    private fun onInit() = screenModelScope.launch {
        getAnimeUseCase(id = animeId)
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

        getSimilarAnimeListUseCase(id = animeId, limit = REQUEST_LIMIT)
            .flowOn(Dispatchers.IO)
            .collect {
                _screenState.emit(
                    _screenState.value.copy(
                        similarAnimeList = it.toPersistentList()
                    )
                )
            }
    }

    private fun checkIfAnimeIsNotFavoured() = screenModelScope.launch {
        getAnimeUseCase(id = animeId)
            .flowOn(Dispatchers.IO)
            .collect {
                _screenState.emit(
                    _screenState.value.copy(
                        isFavoured = it.favoured
                    )
                )
            }
    }

    private fun onArrowBacClick() = screenModelScope.launch {
        _screenAction.emit(NavigateBack)
    }

    private fun onFavouredClick() = screenModelScope.launch {
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
                                ShowSnackbar(
                                    message = "'$name' $WAS_REMOVED_FROM_FAVORITES"
                                )
                            )
                        }

                        false -> _screenAction.emit(ShowSnackbar(message = ERROR_MESSAGE))
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
                            ShowSnackbar(
                                message = "'$name' $WAS_ADDED_TO_FAVORITES"
                            )
                        }

                        false -> _screenAction.emit(ShowSnackbar(message = ERROR_MESSAGE))
                    }
                }
        }
    }

    private fun onInfoButtonClick() = screenModelScope.launch {
        _screenState.emit(
            _screenState.value.copy(
                shouldShowBottomSheet = true
            )
        )
    }

    private fun onBottomSheetDismiss() = screenModelScope.launch {
        _screenState.emit(
            _screenState.value.copy(
                shouldShowBottomSheet = false
            )
        )
    }

    private fun onDescriptionClick() = screenModelScope.launch {
        val isDescriptionExpanded = _screenState.value.isDescriptionExpanded

        _screenState.emit(
            _screenState.value.copy(
                isDescriptionExpanded = !isDescriptionExpanded
            )
        )
    }

    private fun onSimilarAnimeCardClick(id: Int) = screenModelScope.launch {
//        _screenAction.emit(
//            NavigateDetailsScreen(
//                id = id
//            )
//        )
    }

    private fun onAnimeStatusClick() = screenModelScope.launch {
        if (isAuthenticated.value == true) {
            _screenState.emit(
                _screenState.value.copy(
                    shouldShowStatusDialog = true
                )
            )
        } else {
            _screenState.emit(
                _screenState.value.copy(
                    shouldShowAuthDialog = true
                )
            )
        }
    }

    private fun onDialogDismissRequest() = screenModelScope.launch {
        _screenState.emit(
            _screenState.value.copy(
                shouldShowStatusDialog = false
            )
        )
    }

    private suspend fun createUserRates(
        userId: Int,
        targetId: Int,
        status: String
    ) {
        val animeDetails = _screenState.value.animeDetails

        createUserRatesUseCase(
            userId = userId,
            targetId = targetId,
            status = status
        )
            .flowOn(Dispatchers.IO)
            .catch {
                _screenAction.emit(ShowSnackbar(message = ERROR_MESSAGE))
                Firebase.crashlytics.recordException(it)
            }
            .collect { response ->
                _screenAction.emit(
                    ShowSnackbar(
                        message = "$WAS_ADDED_TO_CATEGORY '${formatStatusString(response.status)}'"
                    )
                )
                _screenState.emit(
                    _screenState.value.copy(
                        animeDetails = animeDetails?.copy(userRates = response)
                    )
                )
            }
    }

    private suspend fun deleteUserRates(id: Int) {
        val animeDetails = _screenState.value.animeDetails
        val userRates = animeDetails?.userRates

        deleteUserRatesUseCase(id = id)
            .flowOn(Dispatchers.IO)
            .collect { code ->
                when (code) {
                    HttpsURLConnection.HTTP_NO_CONTENT -> {
                        _screenAction.emit(
                            ShowSnackbar(message = WAS_REMOVED_FROM_MY_LIST)
                        )
                        _screenState.emit(
                            _screenState.value.copy(
                                animeDetails = animeDetails?.copy(
                                    userRates = userRates?.copy(
                                        status = UserRatesEnum.NOT_IN_MY_LIST.key
                                    )
                                )
                            )
                        )
                    }

                    else -> _screenAction.emit(
                        ShowSnackbar(message = ERROR_MESSAGE)
                    )
                }
            }
    }

    private suspend fun updateAnimeStatus(id: Int, status: String) {
        val animeDetails = _screenState.value.animeDetails
        val userRates = animeDetails?.userRates

        updateAnimeStatusUseCase(id = id, status = status)
            .flowOn(Dispatchers.IO)
            .catch { _screenAction.emit(ShowSnackbar(message = ERROR_MESSAGE)) }
            .collect { response ->
                _screenAction.emit(
                    ShowSnackbar(
                        message = "$WAS_ADDED_TO_CATEGORY '${formatStatusString(response)}'"
                    )
                )
                _screenState.emit(
                    _screenState.value.copy(
                        animeDetails = animeDetails?.copy(
                            userRates = userRates?.copy(
                                status = response
                            )
                        )
                    )
                )
            }
    }

    private fun onRadioButtonClick(status: String, id: Int?) = screenModelScope.launch {
        _screenState.emit(
            _screenState.value.copy(
                shouldShowStatusDialog = false
            )
        )

        when (id) {
            null -> createUserRates(
                userId = userId.intValue,
                targetId = animeId,
                status = status
            )

            else -> when (status) {
                UserRatesEnum.NOT_IN_MY_LIST.key -> deleteUserRates(id = id)
                else -> updateAnimeStatus(id = id, status = status)
            }
        }
    }
}
