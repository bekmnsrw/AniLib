package com.bekmnsrw.feature.profile.impl.presentation

import androidx.compose.runtime.Immutable
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.bekmnsrw.core.widget.UserRatesEnum
import com.bekmnsrw.feature.auth.api.usecase.local.IsAuthenticatedUseCase
import com.bekmnsrw.feature.auth.api.usecase.local.SaveUserIdUseCase
import com.bekmnsrw.feature.profile.api.model.AnimeRates
import com.bekmnsrw.feature.profile.api.model.WhoAmI
import com.bekmnsrw.feature.profile.api.usecase.GetProfileUseCase
import com.bekmnsrw.feature.profile.api.usecase.GetUserAnimeByStatusUseCase
import com.bekmnsrw.feature.profile.api.usecase.GetUserAnimeRatesUseCase
import com.bekmnsrw.feature.profile.impl.presentation.ProfileScreenModel.ProfileScreenAction.NavigateAuthScreen
import com.bekmnsrw.feature.profile.impl.presentation.ProfileScreenModel.ProfileScreenAction.NavigateDetailsScreen
import com.bekmnsrw.feature.profile.impl.presentation.ProfileScreenModel.ProfileScreenAction.NavigateFavoritesTab
import com.bekmnsrw.feature.profile.impl.presentation.ProfileScreenModel.ProfileScreenEvent.OnInit
import com.bekmnsrw.feature.profile.impl.presentation.ProfileScreenModel.ProfileScreenEvent.OnItemClick
import com.bekmnsrw.feature.profile.impl.presentation.ProfileScreenModel.ProfileScreenEvent.OnMoreClick
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.PersistentMap
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentMapOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.collections.immutable.toPersistentMap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

internal class ProfileScreenModel(
    private val isAuthenticatedUseCase: IsAuthenticatedUseCase,
    private val getProfileUseCase: GetProfileUseCase,
    private val saveUserIdUseCase: SaveUserIdUseCase,
    private val getUserAnimeRatesUseCase: GetUserAnimeRatesUseCase,
    private val getUserAnimeByStatusUseCase: GetUserAnimeByStatusUseCase
) : ScreenModel {

    @Immutable
    internal data class ProfileScreenState(
        val isAuthenticated: Boolean? = null,
        val profile: WhoAmI? = null,
        val isLoading: Boolean = false,
        val userAnimeRates: PersistentList<AnimeRates> = persistentListOf(),
        val userAnimeStatuses: PersistentMap<String, Int> = persistentMapOf()
    )

    @Immutable
    internal sealed interface ProfileScreenAction {
        data object NavigateAuthScreen : ProfileScreenAction
        data class NavigateDetailsScreen(val id: Int) : ProfileScreenAction
        data object NavigateFavoritesTab : ProfileScreenAction
    }

    @Immutable
    internal sealed interface ProfileScreenEvent {
        data object OnInit : ProfileScreenEvent
        data class OnItemClick(val id: Int) : ProfileScreenEvent
        data object OnMoreClick : ProfileScreenEvent
    }

    private val _screenState = MutableStateFlow(ProfileScreenState())
    val screenState: StateFlow<ProfileScreenState> = _screenState.asStateFlow()

    private val _screenAction = MutableSharedFlow<ProfileScreenAction>()
    val screenAction: SharedFlow<ProfileScreenAction> = _screenAction.asSharedFlow()

    init {
        eventHandler(OnInit)
    }

    fun eventHandler(event: ProfileScreenEvent) {
        when (event) {
            OnInit -> onInit()
            is OnItemClick -> onItemClick(event.id)
            OnMoreClick -> onMoreClick()
        }
    }

    private suspend fun getProfile() = getProfileUseCase()
        .flowOn(Dispatchers.IO)
        .collect {
            _screenState.emit(
                _screenState.value.copy(
                    profile = it
                )
            )
            saveUserIdUseCase(id = it.id)
        }

    private suspend fun isAuthenticated() = isAuthenticatedUseCase()
        .flowOn(Dispatchers.IO)
        .first()


    private suspend fun getUserAnimeRates(id: Int) = getUserAnimeRatesUseCase(id = id)
        .flowOn(Dispatchers.IO)
        .collect {
            _screenState.emit(
                _screenState.value.copy(
                    userAnimeRates = it.toPersistentList()
                )
            )
        }

    private suspend fun getUserAnimeStatuses(id: Int) {
        val userAnimeStatuses = mutableMapOf(
            UserRatesEnum.WATCHING.key to 0,
            UserRatesEnum.DROPPED.key to 0,
            UserRatesEnum.PLANNED.key to 0,
            UserRatesEnum.ON_HOLD.key to 0,
            UserRatesEnum.COMPLETED.key to 0
        )

        getUserAnimeByStatusUseCase(id = id, status = UserRatesEnum.WATCHING.key)
            .flowOn(Dispatchers.IO)
            .collect { userAnimeStatuses[UserRatesEnum.WATCHING.key] = it.size }

        getUserAnimeByStatusUseCase(id = id, status = UserRatesEnum.DROPPED.key)
            .flowOn(Dispatchers.IO)
            .collect { userAnimeStatuses[UserRatesEnum.DROPPED.key] = it.size }

        getUserAnimeByStatusUseCase(id = id, status = UserRatesEnum.PLANNED.key)
            .flowOn(Dispatchers.IO)
            .collect { userAnimeStatuses[UserRatesEnum.PLANNED.key] = it.size }

        getUserAnimeByStatusUseCase(id = id, status = UserRatesEnum.ON_HOLD.key)
            .flowOn(Dispatchers.IO)
            .collect { userAnimeStatuses[UserRatesEnum.ON_HOLD.key] = it.size }

        getUserAnimeByStatusUseCase(id = id, status = UserRatesEnum.COMPLETED.key)
            .flowOn(Dispatchers.IO)
            .collect { userAnimeStatuses[UserRatesEnum.COMPLETED.key] = it.size }

        _screenState.emit(
            _screenState.value.copy(
                userAnimeStatuses = userAnimeStatuses.toPersistentMap()
            )
        )
    }

    private fun onInit() = screenModelScope.launch {
        _screenState.emit(
            _screenState.value.copy(
                isLoading = true
            )
        )

        val isAuthenticated = isAuthenticated()

        println("ProfileSM (isAuthenticated): $isAuthenticated")

        _screenState.emit(
            _screenState.value.copy(
                isAuthenticated = isAuthenticated
            )
        )

        when (isAuthenticated) {
            true -> {
                getProfile()
                getUserAnimeRates(id = 1_379_176)
                getUserAnimeStatuses(id = 1_379_176)
            }
            else -> _screenAction.emit(NavigateAuthScreen)
        }

        _screenState.emit(
            _screenState.value.copy(
                isLoading = false
            )
        )
    }

    private fun onItemClick(id: Int) = screenModelScope.launch {
        _screenAction.emit(NavigateDetailsScreen(id = id))
    }

    private fun onMoreClick() = screenModelScope.launch {
        _screenAction.emit(NavigateFavoritesTab)
    }
}
