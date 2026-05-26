@file:OptIn(ExperimentalCoroutinesApi::class, ExperimentalUuidApi::class)

package dev.koga.deeplinklauncher.home.impl.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.koga.deeplinklauncher.analytics.api.AnalyticsTracker
import dev.koga.deeplinklauncher.date.currentLocalDateTime
import dev.koga.deeplinklauncher.deeplink.api.application.EnrichDeepLinksForList
import dev.koga.deeplinklauncher.deeplink.api.domain.model.DeepLink
import dev.koga.deeplinklauncher.deeplink.api.domain.model.Folder
import dev.koga.deeplinklauncher.deeplink.api.domain.model.LaunchSource
import dev.koga.deeplinklauncher.deeplink.api.domain.repository.DeepLinkRepository
import dev.koga.deeplinklauncher.deeplink.api.domain.usecase.GetAutoSuggestionLinks
import dev.koga.deeplinklauncher.deeplink.api.domain.usecase.GetDeepLinksAndFolderStream
import dev.koga.deeplinklauncher.deeplink.api.domain.usecase.LaunchDeepLink
import dev.koga.deeplinklauncher.deeplink.api.ui.model.DeepLinkListItem
import dev.koga.deeplinklauncher.deeplink.api.ui.navigation.DeepLinkRouteEntryPoint
import dev.koga.deeplinklauncher.deeplink.uicomponent.DeepLinkInputState
import dev.koga.deeplinklauncher.home.impl.analytics.DeeplinkCreated
import dev.koga.deeplinklauncher.home.impl.analytics.DeeplinkDetailsOpened
import dev.koga.deeplinklauncher.home.impl.analytics.DeeplinkLaunchFailed
import dev.koga.deeplinklauncher.home.impl.analytics.DeeplinkLaunched
import dev.koga.deeplinklauncher.home.impl.analytics.FavoriteToggled
import dev.koga.deeplinklauncher.home.impl.analytics.HomeTab
import dev.koga.deeplinklauncher.home.impl.analytics.HomeTabSelected
import dev.koga.deeplinklauncher.home.impl.analytics.OnboardingCompleted
import dev.koga.deeplinklauncher.home.impl.analytics.SearchUsed
import dev.koga.deeplinklauncher.home.impl.analytics.track
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
    private val analyticsTracker: AnalyticsTracker,
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
            is HomeAction.Search -> onSearch(action.text)
            is HomeAction.OnInputChanged -> onDeepLinkTextChanged(action.text)
            is HomeAction.OnSuggestionClicked -> onDeepLinkTextChanged(action.suggestion.text)
            HomeAction.LaunchInputDeepLink -> launchDeepLink()
            HomeAction.OnOnboardingShown -> onboardingShown()
            is HomeAction.Navigate -> navigate(action.route)
            is HomeAction.TabSelected -> onTabSelected(action.tab)
        }
    }

    private fun onSearch(text: String) {
        searchInput.update { text }
        if (text.isNotBlank()) {
            analyticsTracker.track(SearchUsed)
        }
    }

    private fun onTabSelected(tab: HomeTabPage) {
        analyticsTracker.track(
            HomeTabSelected(
                tab = when (tab) {
                    HomeTabPage.HISTORY -> HomeTab.HISTORY
                    HomeTabPage.FAVORITES -> HomeTab.FAVORITES
                    HomeTabPage.FOLDERS -> HomeTab.FOLDERS
                },
            ),
        )
    }

    private fun navigate(route: dev.koga.deeplinklauncher.navigation.AppRoute) {
        if (route is DeepLinkRouteEntryPoint.DeepLinkDetails) {
            analyticsTracker.track(DeeplinkDetailsOpened(entryPoint = "home"))
        }
        appNavigator.navigate(route)
    }

    private fun launchDeepLink() = viewModelScope.launch {
        val link = uiState.value.deepLinkInputState.text

        val deepLink = deepLinkRepository.getDeepLinkByLink(link)

        if (deepLink != null) {
            when (launchDeepLink.launch(deepLink)) {
                is LaunchDeepLink.Result.Success -> {
                    trackLaunchResult(source = LaunchSource.INPUT_BAR)
                    onBottomBarLaunchSuccess(deepLink.id)
                }

                is LaunchDeepLink.Result.Failure -> {
                    trackLaunchFailed(source = LaunchSource.INPUT_BAR)
                    showLaunchError(link)
                }
            }

            return@launch
        }

        when (val result = launchDeepLink.launch(link)) {
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
                analyticsTracker.track(DeeplinkCreated(source = LaunchSource.INPUT_BAR))
                trackLaunchResult(source = LaunchSource.INPUT_BAR)
                onBottomBarLaunchSuccess(id)
            }

            is LaunchDeepLink.Result.Failure -> {
                trackLaunchFailed(source = LaunchSource.INPUT_BAR)
                showLaunchError(link)
            }
        }
    }

    private fun launchDeepLink(deepLink: DeepLink) {
        viewModelScope.launch {
            when (launchDeepLink.launch(deepLink)) {
                is LaunchDeepLink.Result.Success -> {
                    trackLaunchResult(source = LaunchSource.LIST)
                }

                is LaunchDeepLink.Result.Failure -> {
                    trackLaunchFailed(source = LaunchSource.LIST)
                }
            }
        }
    }

    private fun trackLaunchResult(
        source: LaunchSource,
    ) {
        analyticsTracker.track(
            DeeplinkLaunched(source = source),
        )
    }

    private fun trackLaunchFailed(
        source: LaunchSource,
    ) {
        analyticsTracker.track(
            DeeplinkLaunchFailed(source = source),
        )
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
        val isFavorite = !deepLink.isFavorite
        viewModelScope.launch {
            deepLinkRepository.upsertDeepLink(
                deepLink.copy(isFavorite = isFavorite),
            )
            analyticsTracker.track(FavoriteToggled(isFavorite = isFavorite))
        }
    }

    private fun onDeepLinkTextChanged(text: String) {
        errorMessage.update { null }
        launchInput.update { text }
    }

    private fun onboardingShown() {
        viewModelScope.launch {
            preferencesDataSource.setShouldHideOnboarding(true)
            analyticsTracker.track(OnboardingCompleted)
        }
    }

    private data class EnrichedData(
        val deepLinks: List<DeepLinkListItem>,
        val favorites: List<DeepLinkListItem>,
        val folders: List<Folder>,
    )
}
