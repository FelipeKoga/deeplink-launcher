@file:OptIn(ExperimentalCoroutinesApi::class, ExperimentalUuidApi::class)

package dev.koga.deeplinklauncher.home.impl.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.koga.deeplinklauncher.date.currentLocalDateTime
import dev.koga.deeplinklauncher.deeplink.api.application.EnrichDeepLinksForList
import dev.koga.deeplinklauncher.deeplink.api.domain.model.DeepLink
import dev.koga.deeplinklauncher.deeplink.api.domain.model.Folder
import dev.koga.deeplinklauncher.deeplink.api.domain.repository.DeepLinkRepository
import dev.koga.deeplinklauncher.deeplink.api.domain.usecase.GetAutoSuggestionLinks
import dev.koga.deeplinklauncher.deeplink.api.domain.usecase.GetDeepLinksAndFolderStream
import dev.koga.deeplinklauncher.deeplink.api.domain.usecase.LaunchDeepLink
import dev.koga.deeplinklauncher.deeplink.api.ui.model.DeepLinkListItem
import dev.koga.deeplinklauncher.deeplink.api.ui.navigation.DeepLinkRouteEntryPoint
import dev.koga.deeplinklauncher.deeplink.uicomponent.DeepLinkInputState
import dev.koga.deeplinklauncher.home.impl.ui.state.HomeUiState
import dev.koga.deeplinklauncher.navigation.AppNavigator
import dev.koga.deeplinklauncher.preferences.repository.PreferencesDataSource
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class HomeViewModel(
    getDeepLinksAndFolderStream: GetDeepLinksAndFolderStream,
    private val enrichDeepLinksForList: EnrichDeepLinksForList,
    private val getAutoSuggestionLinks: GetAutoSuggestionLinks,
    private val deepLinkRepository: DeepLinkRepository,
    private val launchDeepLink: LaunchDeepLink,
    private val preferencesDataSource: PreferencesDataSource,
    private val appNavigator: AppNavigator,
) : ViewModel() {

    private val searchInput = MutableStateFlow("")
    private val launchInput = MutableStateFlow("")
    private val errorMessage = MutableStateFlow<String?>(null)
    private val suggestions = combine(
        launchInput,
        preferencesDataSource.preferencesStream,
    ) { input, _ ->
        getAutoSuggestionLinks(input)
    }
    private val dataStream = searchInput.flatMapLatest { getDeepLinksAndFolderStream(it) }

    private val enrichedDataStream = dataStream.flatMapLatest { data ->
        flow {
            val deepLinks = enrichDeepLinksForList(data.deepLinks)
            emit(
                EnrichedData(
                    deepLinks = deepLinks,
                    favorites = deepLinks.filter { it.deepLink.isFavorite },
                    folders = data.folders,
                ),
            )
        }
    }

    private val deepLinkInputState =
        combine(launchInput, errorMessage, suggestions, ::DeepLinkInputState)

    private val showOnboarding = preferencesDataSource
        .preferencesStream
        .map { it.shouldShowOnboarding }

    val uiState = combine(
        searchInput,
        deepLinkInputState,
        enrichedDataStream,
        showOnboarding,
    ) { searchInput, deepLinkInputState, enrichedData, showOnboarding ->
        HomeUiState(
            deepLinkInputState = deepLinkInputState,
            searchInput = searchInput,
            deepLinks = enrichedData.deepLinks.toPersistentList(),
            favorites = enrichedData.favorites.toPersistentList(),
            folders = enrichedData.folders.toPersistentList(),
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
            is HomeAction.ToggleFavorite -> toggleFavorite(action.deepLink)
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
            when (launchDeepLink.launch(deepLink)) {
                is LaunchDeepLink.Result.Success -> onBottomBarLaunchSuccess(deepLink.id)
                is LaunchDeepLink.Result.Failure -> showLaunchError(link)
            }

            return@launch
        }

        when (launchDeepLink.launch(link)) {
            is LaunchDeepLink.Result.Success -> {
                val id = Uuid.random().toString()
                deepLinkRepository.upsertDeepLink(
                    DeepLink(
                        id = id,
                        link = link,
                        name = null,
                        description = null,
                        folder = null,
                        isFavorite = false,
                        lastLaunchedAt = currentLocalDateTime,
                    ),
                )
                onBottomBarLaunchSuccess(id)
            }

            is LaunchDeepLink.Result.Failure -> showLaunchError(link)
        }
    }

    private fun launchDeepLink(deepLink: DeepLink) {
        viewModelScope.launch {
            launchDeepLink.launch(deepLink)
        }
    }

    private fun onBottomBarLaunchSuccess(deepLinkId: String) {
        launchInput.update { "" }
        errorMessage.update { null }
        appNavigator.navigate(
            DeepLinkRouteEntryPoint.DeepLinkDetails(deepLinkId, showFolder = true),
        )
    }

    private fun showLaunchError(link: String) {
        errorMessage.update {
            "No app found to handle this deep link: $link"
        }
    }

    private fun toggleFavorite(deepLink: DeepLink) {
        viewModelScope.launch {
            deepLinkRepository.upsertDeepLink(
                deepLink.copy(isFavorite = !deepLink.isFavorite),
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

    private data class EnrichedData(
        val deepLinks: List<DeepLinkListItem>,
        val favorites: List<DeepLinkListItem>,
        val folders: List<Folder>,
    )
}
