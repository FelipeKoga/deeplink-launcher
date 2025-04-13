@file:OptIn(ExperimentalCoroutinesApi::class, ExperimentalUuidApi::class)

package dev.koga.deeplinklauncher.home.ui.screen

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import dev.koga.deeplinklauncher.date.currentLocalDateTime
import dev.koga.deeplinklauncher.deeplink.api.model.DeepLink
import dev.koga.deeplinklauncher.deeplink.api.model.Folder
import dev.koga.deeplinklauncher.deeplink.api.model.Suggestion
import dev.koga.deeplinklauncher.deeplink.api.repository.DeepLinkRepository
import dev.koga.deeplinklauncher.deeplink.api.repository.FolderRepository
import dev.koga.deeplinklauncher.deeplink.api.usecase.GetAutoSuggestionLinks
import dev.koga.deeplinklauncher.deeplink.api.usecase.GetDeepLinksAndFolderStream
import dev.koga.deeplinklauncher.deeplink.api.usecase.LaunchDeepLink
import dev.koga.deeplinklauncher.home.ui.screen.state.HomeEvent
import dev.koga.deeplinklauncher.home.ui.screen.state.HomeUiState
import dev.koga.deeplinklauncher.preferences.api.repository.PreferencesRepository
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class HomeScreenModel(
    getDeepLinksAndFolderStream: GetDeepLinksAndFolderStream,
    private val getAutoSuggestionLinks: GetAutoSuggestionLinks,
    private val deepLinkRepository: DeepLinkRepository,
    private val folderRepository: FolderRepository,
    private val launchDeepLink: LaunchDeepLink,
    private val preferencesRepository: PreferencesRepository,
) : ScreenModel {

    private val searchInput = MutableStateFlow("")
    private val deepLinkInput = MutableStateFlow("")

    private val dataStream = searchInput.flatMapLatest {
        getDeepLinksAndFolderStream(it)
    }.stateIn(
        scope = screenModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = GetDeepLinksAndFolderStream.Result(
            deepLinks = emptyList(),
            favorites = emptyList(),
            folders = emptyList(),
        ),
    )

    private val errorMessage = MutableStateFlow<String?>(null)

    private val suggestions = combine(
        deepLinkInput,
        preferencesRepository.preferencesStream.map { it.shouldDisableDeepLinkSuggestions },
    ) { inputText, shouldDisableDeepLinkSuggestions ->
        if (shouldDisableDeepLinkSuggestions) {
            emptyList()
        } else {
            getAutoSuggestionLinks(inputText)
        }
    }

    val uiState = combine(
        searchInput,
        deepLinkInput,
        dataStream,
        suggestions,
        errorMessage,
    ) { searchInput, deepLinkInput, dataStream, suggestions, errorMessage ->
        HomeUiState(
            deepLinkInput = deepLinkInput,
            searchInput = searchInput,
            deepLinks = dataStream.deepLinks.toPersistentList(),
            suggestions = suggestions.toPersistentList(),
            favorites = dataStream.favorites.toPersistentList(),
            folders = dataStream.folders.toPersistentList(),
            errorMessage = errorMessage,
        )
    }.stateIn(
        scope = screenModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = HomeUiState(),
    )

    private val eventDispatcher = Channel<HomeEvent>(Channel.UNLIMITED)
    val events = eventDispatcher.receiveAsFlow()

    init {
        screenModelScope.launch {
            preferencesRepository.preferencesStream.firstOrNull()?.let {
                if (it.shouldShowOnboarding) {
                    eventDispatcher.send(HomeEvent.ShowOnboarding)
                }
            }
        }
    }

    fun launchDeepLink() = screenModelScope.launch {
        val link = deepLinkInput.value

        val deepLink = deepLinkRepository.getDeepLinkByLink(link)

        if (deepLink != null) {
            launchDeepLink(deepLink)

            return@launch
        }

        when (launchDeepLink.launch(link)) {
            is LaunchDeepLink.Result.Success -> {
                insertDeepLink(link)
                eventDispatcher.send(HomeEvent.DeepLinksLaunched)
            }

            is LaunchDeepLink.Result.Failure -> {
                errorMessage.update {
                    "No app found to handle this deep link: $link"
                }
            }
        }
    }

    fun launchDeepLink(deepLink: DeepLink) {
        screenModelScope.launch {
            launchDeepLink.launch(deepLink)

            eventDispatcher.send(HomeEvent.DeepLinksLaunched)
        }
    }

    private fun insertDeepLink(link: String) {
        screenModelScope.launch {
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

    fun onDeepLinkTextChanged(text: String) {
        errorMessage.update { null }
        deepLinkInput.update { text }
    }

    fun onSuggestionClicked(suggestion: Suggestion) {
        onDeepLinkTextChanged(suggestion.text)
    }

    fun addFolder(name: String, description: String) {
        folderRepository.upsertFolder(
            Folder(
                id = Uuid.random().toString(),
                name = name,
                description = description,
                deepLinkCount = 0,
            ),
        )
    }

    fun onboardingShown() {
        screenModelScope.launch {
            preferencesRepository.setShouldHideOnboarding(true)
        }
    }

    fun onSearch(value: String) {
        searchInput.update { value }
    }
}
