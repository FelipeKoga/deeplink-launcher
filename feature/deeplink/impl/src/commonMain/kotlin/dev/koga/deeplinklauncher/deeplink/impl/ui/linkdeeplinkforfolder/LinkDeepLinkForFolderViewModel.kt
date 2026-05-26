@file:OptIn(ExperimentalCoroutinesApi::class, ExperimentalUuidApi::class)

package dev.koga.deeplinklauncher.deeplink.impl.ui.linkdeeplinkforfolder

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dev.koga.deeplinklauncher.analytics.api.AnalyticsTracker
import dev.koga.deeplinklauncher.date.currentLocalDateTime
import dev.koga.deeplinklauncher.deeplink.api.application.EnrichDeepLinksForList
import dev.koga.deeplinklauncher.deeplink.api.domain.model.DeepLink
import dev.koga.deeplinklauncher.deeplink.api.domain.model.LaunchSource
import dev.koga.deeplinklauncher.deeplink.api.domain.repository.DeepLinkRepository
import dev.koga.deeplinklauncher.deeplink.api.domain.repository.FolderRepository
import dev.koga.deeplinklauncher.deeplink.api.domain.usecase.GetAutoSuggestionLinks
import dev.koga.deeplinklauncher.deeplink.api.domain.usecase.LaunchDeepLink
import dev.koga.deeplinklauncher.deeplink.api.domain.usecase.LinkDeepLinkToFolder
import dev.koga.deeplinklauncher.deeplink.api.ui.navigation.DeepLinkRouteEntryPoint
import dev.koga.deeplinklauncher.deeplink.impl.analytics.DeeplinkCreated
import dev.koga.deeplinklauncher.deeplink.impl.analytics.DeeplinkLaunchFailed
import dev.koga.deeplinklauncher.deeplink.impl.analytics.DeeplinkLaunched
import dev.koga.deeplinklauncher.deeplink.impl.analytics.FolderLinkCompleted
import dev.koga.deeplinklauncher.deeplink.impl.analytics.track
import dev.koga.deeplinklauncher.deeplink.impl.ui.linkdeeplinkforfolder.state.LinkDeepLinkForFolderAction
import dev.koga.deeplinklauncher.deeplink.impl.ui.linkdeeplinkforfolder.state.LinkDeepLinkForFolderUiState
import dev.koga.deeplinklauncher.deeplink.uicomponent.DeepLinkInputState
import dev.koga.deeplinklauncher.navigation.AppNavigator
import dev.koga.deeplinklauncher.preferences.repository.PreferencesDataSource
import dev.koga.deeplinklauncher.uievent.SnackBarDispatcher
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

internal class LinkDeepLinkForFolderViewModel(
    savedStateHandle: SavedStateHandle,
    private val folderRepository: FolderRepository,
    private val deepLinkRepository: DeepLinkRepository,
    private val enrichDeepLinksForList: EnrichDeepLinksForList,
    private val getAutoSuggestionLinks: GetAutoSuggestionLinks,
    private val launchDeepLink: LaunchDeepLink,
    private val linkDeepLinkToFolder: LinkDeepLinkToFolder,
    private val snackBarDispatcher: SnackBarDispatcher,
    private val appNavigator: AppNavigator,
    private val preferencesDataSource: PreferencesDataSource,
    private val analyticsTracker: AnalyticsTracker,
) : ViewModel() {
    private val folderId =
        savedStateHandle.toRoute<DeepLinkRouteEntryPoint.PickDeepLinkForFolder>().folderId
    private val folder = folderRepository.getFolderById(folderId)!!

    private val query = MutableStateFlow("")
    private val launchInput = MutableStateFlow("")
    private val errorMessage = MutableStateFlow<String?>(null)
    private val pendingLinkConfirmation = MutableStateFlow<DeepLink?>(null)
    private val suggestions = combine(
        launchInput,
        preferencesDataSource.preferencesStream,
    ) { input, _ ->
        getAutoSuggestionLinks(input)
    }

    private val deepLinkInputState =
        combine(launchInput, errorMessage, suggestions, ::DeepLinkInputState)

    private val folderDeepLinkIds = folderRepository.getFolderDeepLinksStream(folderId)
        .map { links -> links.map { it.id }.toSet() }

    private val linkableDeepLinks = combine(
        deepLinkRepository.getDeepLinksStream(),
        folderDeepLinkIds,
        query,
    ) { allDeepLinks, inFolderIds, searchQuery ->
        filterLinkableDeepLinks(allDeepLinks, inFolderIds, searchQuery)
    }.flatMapLatest { links ->
        flow {
            emit(
                if (links.isEmpty()) {
                    emptyList()
                } else {
                    enrichDeepLinksForList(links)
                },
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = emptyList(),
    )

    val uiState = combine(
        query,
        linkableDeepLinks,
        deepLinkInputState,
        pendingLinkConfirmation,
    ) { searchQuery, linkable, inputState, pending ->
        LinkDeepLinkForFolderUiState(
            folderName = folder.name,
            query = searchQuery,
            linkableDeepLinks = linkable.toPersistentList(),
            deepLinkInputState = inputState,
            pendingLinkConfirmation = pending,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = LinkDeepLinkForFolderUiState(folderName = folder.name),
    )

    fun onAction(action: LinkDeepLinkForFolderAction) {
        when (action) {
            is LinkDeepLinkForFolderAction.QueryChanged -> query.update { action.text }
            is LinkDeepLinkForFolderAction.DeepLinkSelected -> linkDeepLinkSelected(action.deepLinkId)
            is LinkDeepLinkForFolderAction.Launch -> launch(action.deeplink)
            LinkDeepLinkForFolderAction.LaunchInputDeepLink -> launchInputDeepLink()
            is LinkDeepLinkForFolderAction.OnInputChanged -> onDeepLinkTextChanged(action.text)
            is LinkDeepLinkForFolderAction.OnSuggestionClicked -> onDeepLinkTextChanged(action.suggestion.text)
            LinkDeepLinkForFolderAction.ConfirmLinkToFolder -> confirmLinkToFolder()
            LinkDeepLinkForFolderAction.DismissLinkConfirmation -> pendingLinkConfirmation.update { null }
        }
    }

    private fun launch(deepLink: DeepLink) {
        viewModelScope.launch {
            val result = launchDeepLink.launch(deepLink)
            trackLaunchResult(result = result)
        }
    }

    private fun launchInputDeepLink() = viewModelScope.launch {
        val link = uiState.value.deepLinkInputState.text
        val existing = deepLinkRepository.getDeepLinkByLink(link)

        if (existing != null) {
            when (val result = launchDeepLink.launch(existing)) {
                is LaunchDeepLink.Result.Success -> {
                    trackLaunchResult(result = result)
                    if (existing.folder?.id != folderId) {
                        pendingLinkConfirmation.update { existing }
                    }
                }

                is LaunchDeepLink.Result.Failure -> {
                    analyticsTracker.track(
                        DeeplinkLaunchFailed(source = LaunchSource.LINK_FLOW),
                    )
                    showLaunchError(link)
                }
            }
            return@launch
        }

        when (launchDeepLink.launch(link)) {
            is LaunchDeepLink.Result.Success -> {
                insertDeepLinkWithFolder(link)
                analyticsTracker.track(DeeplinkCreated(source = LaunchSource.LINK_FLOW))
                appNavigator.popBackStack()
            }

            is LaunchDeepLink.Result.Failure -> {
                analyticsTracker.track(
                    DeeplinkLaunchFailed(source = LaunchSource.LINK_FLOW),
                )

                showLaunchError(link)
            }
        }
    }

    private fun insertDeepLinkWithFolder(link: String) {
        deepLinkRepository.upsertDeepLink(
            DeepLink(
                id = Uuid.random().toString(),
                link = link,
                name = null,
                description = null,
                folder = folder,
                isFavorite = false,
                lastLaunchedAt = currentLocalDateTime,
            ),
        )
    }

    private fun confirmLinkToFolder() {
        val pendingDeepLink = uiState.value.pendingLinkConfirmation ?: return

        viewModelScope.launch {
            when (linkDeepLinkToFolder(pendingDeepLink.id, folderId)) {
                LinkDeepLinkToFolder.Result.Linked,
                LinkDeepLinkToFolder.Result.AlreadyLinked,
                -> {
                    analyticsTracker.track(FolderLinkCompleted)
                    pendingLinkConfirmation.update { null }
                    appNavigator.popBackStack()
                }

                LinkDeepLinkToFolder.Result.NotFound -> {
                    pendingLinkConfirmation.update { null }
                    snackBarDispatcher.show("Deeplink not found")
                }
            }
        }
    }

    private fun linkDeepLinkSelected(deepLinkId: String) {
        viewModelScope.launch {
            when (linkDeepLinkToFolder(deepLinkId, folderId)) {
                LinkDeepLinkToFolder.Result.Linked,
                LinkDeepLinkToFolder.Result.AlreadyLinked,
                -> {
                    analyticsTracker.track(FolderLinkCompleted)
                    appNavigator.popBackStack()
                }

                LinkDeepLinkToFolder.Result.NotFound -> {
                    snackBarDispatcher.show("Deeplink not found")
                }
            }
        }
    }

    private fun onDeepLinkTextChanged(text: String) {
        errorMessage.update { null }
        launchInput.update { text }
    }

    private fun showLaunchError(link: String) {
        errorMessage.update {
            "No app found to handle this deep link: $link"
        }
    }

    private fun trackLaunchResult(
        result: LaunchDeepLink.Result,
    ) {
        when (result) {
            is LaunchDeepLink.Result.Success -> {
                analyticsTracker.track(
                    DeeplinkLaunched(source = LaunchSource.LINK_FLOW),
                )
            }

            is LaunchDeepLink.Result.Failure -> {
                analyticsTracker.track(
                    DeeplinkLaunchFailed(source = LaunchSource.LINK_FLOW),
                )
            }
        }
    }

    private fun filterLinkableDeepLinks(
        allDeepLinks: List<DeepLink>,
        inFolderIds: Set<String>,
        searchQuery: String,
    ): List<DeepLink> {
        val normalizedQuery = searchQuery.trim()
        return allDeepLinks
            .filter { it.id !in inFolderIds }
            .filter { deepLink ->
                normalizedQuery.isBlank() ||
                    deepLink.link.contains(normalizedQuery, ignoreCase = true) ||
                    deepLink.name?.contains(normalizedQuery, ignoreCase = true) == true
            }
    }
}
