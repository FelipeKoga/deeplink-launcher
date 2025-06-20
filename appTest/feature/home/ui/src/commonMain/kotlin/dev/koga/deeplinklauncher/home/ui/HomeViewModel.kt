@file:OptIn(ExperimentalCoroutinesApi::class, ExperimentalUuidApi::class)

package dev.koga.deeplinklauncher.home.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.koga.deeplinklauncher.date.currentLocalDateTime
import dev.koga.deeplinklauncher.deeplink.api.model.DeepLink
import dev.koga.deeplinklauncher.deeplink.api.repository.DeepLinkRepository
import dev.koga.deeplinklauncher.deeplink.api.usecase.GetAutoSuggestionLinks
import dev.koga.deeplinklauncher.deeplink.api.usecase.GetDeepLinksAndFolderStream
import dev.koga.deeplinklauncher.deeplink.api.usecase.LaunchDeepLink
import dev.koga.deeplinklauncher.home.ui.state.DeepLinkInputState
import dev.koga.deeplinklauncher.home.ui.state.HomeUiState
import dev.koga.deeplinklauncher.navigation.AppNavigator
import dev.koga.deeplinklauncher.preferences.repository.PreferencesDataSource
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class HomeViewModel(
    getDeepLinksAndFolderStream: GetDeepLinksAndFolderStream,
    private val getAutoSuggestionLinks: GetAutoSuggestionLinks,
    private val deepLinkRepository: DeepLinkRepository,
    private val launchDeepLink: LaunchDeepLink,
    private val preferencesDataSource: PreferencesDataSource,
    private val appNavigator: AppNavigator,
) : ViewModel() {

    private val searchInput = MutableStateFlow("")
    private val launchInput = MutableStateFlow("")
    private val errorMessage = MutableStateFlow<String?>(null)
    private val suggestions = launchInput.mapLatest { getAutoSuggestionLinks(it) }
    private val dataStream = searchInput.flatMapLatest { getDeepLinksAndFolderStream(it) }

    private val deepLinkInputState =
        combine(launchInput, errorMessage, suggestions, ::DeepLinkInputState)

    private val showOnboarding = preferencesDataSource
        .preferencesStream
        .map { it.shouldShowOnboarding }

    val uiState = combine(
        searchInput,
        deepLinkInputState,
        dataStream,
        showOnboarding,
    ) { searchInput, deepLinkInputState, dataStream, showOnboarding ->
        HomeUiState(
            deepLinkInputState = deepLinkInputState,
            searchInput = searchInput,
            deepLinks = dataStream.deepLinks.toPersistentList(),
            favorites = dataStream.favorites.toPersistentList(),
            folders = dataStream.folders.toPersistentList(),
            showOnboarding = showOnboarding,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000),
        initialValue = HomeUiState(),
    )

    fun onAction(action: HomeAction) {
        when (action) {
            is HomeAction.LaunchDeepLink -> launchDeepLink(action.deepLink)
            is HomeAction.Search -> searchInput.update { action.text }
            is HomeAction.OnInputChanged -> onDeepLinkTextChanged(action.text)
            is HomeAction.OnSuggestionClicked -> onDeepLinkTextChanged(action.suggestion.text)
            HomeAction.LaunchInputDeepLink -> launchDeepLink()
            HomeAction.OnOnboardingShown -> onboardingShown()
            is HomeAction.Navigate -> appNavigator.navigate(action.route)
        }
    }

    private fun launchDeepLink() = viewModelScope.launch {
        val link = uiState.value.deepLinkInputState.text

        val deepLink = deepLinkRepository.getDeepLinkByLink(link)

        if (deepLink != null) {
            launchDeepLink(deepLink)

            return@launch
        }

        when (launchDeepLink.launch(link)) {
            is LaunchDeepLink.Result.Success -> {
                insertDeepLink(link)
            }

            is LaunchDeepLink.Result.Failure -> {
                errorMessage.update {
                    "No app found to handle this deep link: $link"
                }
            }
        }
    }

    private fun launchDeepLink(deepLink: DeepLink) {
        viewModelScope.launch {
            launchDeepLink.launch(deepLink)
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    private fun insertDeepLink(link: String) {
        viewModelScope.launch {
            deepLinkRepository.upsertDeepLink(
                DeepLink(
                    id = Uuid.random().toString(),
                    link = link,
                    name = null,
                    description = null,
                    folder = null,
                    isFavorite = false,
                    lastLaunchedAt = currentLocalDateTime,
                ),
            )
        }
    }

    private fun onDeepLinkTextChanged(text: String) {
        errorMessage.update { null }
        launchInput.update { text }
    }

    private fun onboardingShown() {
        viewModelScope.launch {
            preferencesDataSource.setShouldHideOnboarding(true)
        }
    }
}
