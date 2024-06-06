package dev.koga.deeplinklauncher.screen

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import dev.koga.deeplinklauncher.datasource.DeepLinkDataSource
import dev.koga.deeplinklauncher.datasource.FolderDataSource
import dev.koga.deeplinklauncher.datasource.PreferencesDataSource
import dev.koga.deeplinklauncher.model.DeepLink
import dev.koga.deeplinklauncher.model.Folder
import dev.koga.deeplinklauncher.provider.UUIDProvider
import dev.koga.deeplinklauncher.screen.state.HomeEvent
import dev.koga.deeplinklauncher.screen.state.HomeUiState
import dev.koga.deeplinklauncher.usecase.GetAutoSuggestionLinks
import dev.koga.deeplinklauncher.usecase.GetDeepLinksAndFolderStream
import dev.koga.deeplinklauncher.usecase.LaunchDeepLink
import dev.koga.deeplinklauncher.usecase.LaunchDeepLinkResult
import dev.koga.deeplinklauncher.util.ext.currentLocalDateTime
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

class HomeScreenModel(
    getDeepLinksAndFolderStream: GetDeepLinksAndFolderStream,
    private val getAutoSuggestionLinks: GetAutoSuggestionLinks,
    private val deepLinkDataSource: DeepLinkDataSource,
    private val folderDataSource: FolderDataSource,
    private val launchDeepLink: LaunchDeepLink,
    private val preferencesDataSource: PreferencesDataSource,
) : ScreenModel {

    private val searchInput = MutableStateFlow("")
    private val deepLinkInput = MutableStateFlow("")

    @OptIn(ExperimentalCoroutinesApi::class)
    private val dataStream = searchInput.flatMapLatest {
        getDeepLinksAndFolderStream(it)
    }.stateIn(
        scope = screenModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = GetDeepLinksAndFolderStream.Output(
            deepLinks = emptyList(),
            favorites = emptyList(),
            folders = emptyList(),
        ),
    )

    private val errorMessage = MutableStateFlow<String?>(null)

    private val suggestions = combine(
        deepLinkInput,
        preferencesDataSource.preferencesStream.map { it.shouldDisableDeepLinkSuggestions },
    ) { inputText, shouldDisableDeepLinkSuggestions ->
        if (shouldDisableDeepLinkSuggestions) {
            emptyList()
        } else {
            getAutoSuggestionLinks.execute(inputText)
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
            preferencesDataSource.preferencesStream.firstOrNull()?.let {
                if (it.shouldShowOnboarding) {
                    eventDispatcher.send(HomeEvent.ShowOnboarding)
                }
            }
        }
    }

    fun launchDeepLink() = screenModelScope.launch {
        val link = deepLinkInput.value

        val deepLink = deepLinkDataSource.getDeepLinkByLink(link)

        if (deepLink != null) {
            launchDeepLink(deepLink)

            return@launch
        }

        when (launchDeepLink.launch(link)) {
            is LaunchDeepLinkResult.Success -> {
                insertDeepLink(link)
                eventDispatcher.send(HomeEvent.DeepLinksLaunched)
            }

            is LaunchDeepLinkResult.Failure -> {
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
            deepLinkDataSource.upsertDeepLink(
                DeepLink(
                    id = UUIDProvider.get(),
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

    fun addFolder(name: String, description: String) {
        folderDataSource.upsertFolder(
            Folder(
                id = UUIDProvider.get(),
                name = name,
                description = description,
                deepLinkCount = 0,
            ),
        )
    }

    fun onboardingShown() {
        screenModelScope.launch {
            preferencesDataSource.setShouldHideOnboarding(true)
        }
    }

    fun onSearch(value: String) {
        searchInput.update { value }
    }
}
