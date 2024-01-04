package com.bekmnsrw.feature.profile.impl.presentation.profile

import android.util.Log
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.mutableIntStateOf
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
import com.bekmnsrw.feature.profile.impl.presentation.profile.ProfileScreenModel.ProfileScreenAction.*
import com.bekmnsrw.feature.profile.impl.presentation.profile.ProfileScreenModel.ProfileScreenEvent.*
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

    private val userId by lazy { mutableIntStateOf(0) }

    @Immutable
    internal data class ProfileScreenState(
        val isAuthenticated: Boolean? = null,
        val profile: WhoAmI? = null,
        val refreshing: Boolean = false,
        val userAnimeRates: PersistentList<AnimeRates> = persistentListOf(),
        val userAnimeStatuses: PersistentMap<String, Int> = persistentMapOf()
    )

    @Immutable
    internal sealed interface ProfileScreenAction {
        data object NavigateAuthScreen : ProfileScreenAction
        data class NavigateDetailsScreen(val id: Int) : ProfileScreenAction
        data object NavigateFavoritesTab : ProfileScreenAction
        data object NavigateSettingsScreen : ProfileScreenAction
    }

    @Immutable
    internal sealed interface ProfileScreenEvent {
        data object OnInit : ProfileScreenEvent
        data object OnRefresh : ProfileScreenEvent
        data class OnItemClick(val id: Int) : ProfileScreenEvent
        data object OnMoreClick : ProfileScreenEvent
        data object OnSettingsIconClick : ProfileScreenEvent
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
            OnSettingsIconClick -> onSettingsIconClick()
            OnRefresh -> onRefresh()
        }
    }

    private fun onRefresh() = screenModelScope.launch {
        _screenState.emit(
            _screenState.value.copy(
                refreshing = true
            )
        )

        getUserAnimeStatuses()

        _screenState.emit(
            _screenState.value.copy(
                refreshing = false
            )
        )
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
            userId.intValue = it.id
        }

    private suspend fun isAuthenticated() = isAuthenticatedUseCase()
        .flowOn(Dispatchers.IO)
        .first()


    private suspend fun getUserAnimeRates() = getUserAnimeRatesUseCase(id = userId.intValue)
        .flowOn(Dispatchers.IO)
        .collect {
            _screenState.emit(
                _screenState.value.copy(
                    userAnimeRates = it.toPersistentList()
                )
            )
        }

    private suspend fun getUserAnimeStatuses() {
        val userAnimeStatuses = mutableMapOf(
            UserRatesEnum.WATCHING.key to 0,
            UserRatesEnum.DROPPED.key to 0,
            UserRatesEnum.PLANNED.key to 0,
            UserRatesEnum.ON_HOLD.key to 0,
            UserRatesEnum.COMPLETED.key to 0
        )

        getUserAnimeByStatusUseCase(id = userId.intValue, status = UserRatesEnum.WATCHING.key)
            .flowOn(Dispatchers.IO)
            .collect { userAnimeStatuses[UserRatesEnum.WATCHING.key] = it.size }

        getUserAnimeByStatusUseCase(id = userId.intValue, status = UserRatesEnum.DROPPED.key)
            .flowOn(Dispatchers.IO)
            .collect { userAnimeStatuses[UserRatesEnum.DROPPED.key] = it.size }

        getUserAnimeByStatusUseCase(id = userId.intValue, status = UserRatesEnum.PLANNED.key)
            .flowOn(Dispatchers.IO)
            .collect { userAnimeStatuses[UserRatesEnum.PLANNED.key] = it.size }

        getUserAnimeByStatusUseCase(id = userId.intValue, status = UserRatesEnum.ON_HOLD.key)
            .flowOn(Dispatchers.IO)
            .collect { userAnimeStatuses[UserRatesEnum.ON_HOLD.key] = it.size }

        getUserAnimeByStatusUseCase(id = userId.intValue, status = UserRatesEnum.COMPLETED.key)
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
                refreshing = true
            )
        )

        val isAuthenticated = isAuthenticated()

        Log.e("ProfileSM", "isAuthenticated: $isAuthenticated")

        _screenState.emit(
            _screenState.value.copy(
                isAuthenticated = isAuthenticated
            )
        )

        when (isAuthenticated) {
            true -> {
                getProfile()
                getUserAnimeRates()
                getUserAnimeStatuses()
            }
            else -> _screenAction.emit(NavigateAuthScreen)
        }

        _screenState.emit(
            _screenState.value.copy(
                refreshing = false
            )
        )
    }

    private fun onItemClick(id: Int) = screenModelScope.launch {
        _screenAction.emit(NavigateDetailsScreen(id = id))
    }

    private fun onMoreClick() = screenModelScope.launch {
        _screenAction.emit(NavigateFavoritesTab)
    }

    private fun onSettingsIconClick() = screenModelScope.launch {
        _screenAction.emit(NavigateSettingsScreen)
    }
}
